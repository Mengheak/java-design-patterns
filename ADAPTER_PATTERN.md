# Adapter Pattern

**Local practice:** [VendorAiAdapter](src/adapter/VendorAiAdapter.java) implements `dependency_injection.AiModel` and delegates to `VendorAiClient.chat`. Package: `adapter`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 1 — read third, after [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md).**
> **Module:** `practices_with_springboot/adapter-pattern-class-samples` · **Packages:** `com.chheang.mengheak.adapter.*`

**The pattern that saves you actual money.** [Strategy](STRATEGY_PATTERN.md) picks between *your* implementations. Adapter defends you from *other people's*. That difference is why Adapter matters more in production systems.

---

## Intent

> Convert the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.

The practical framing that matters more than the textbook one: **an adapter is an anti-corruption layer.** It is the wall that stops a vendor's data model from leaking into your domain.

---

## The problem — three vendors, three kinds of ugly

The Spring Boot module simulates three payment providers. Look at what they actually expose:

```java
// Stripe — money as a long, in cents. Success as a boolean.
public StripeResponse charge(long c, String u, String t);
public record StripeResponse(String chargeId, boolean paid, String failureMessage) {}

// ABA — money as a double (!). Success as their own enum, with a third state.
public AbaResponse submit(String a, double n, String c, String r);
public record AbaResponse(String transactionCode, AbaStatus status, String description) {}
public enum AbaStatus { APPROVED, DECLINED, PROCESSING }

// Wing — money as BigDecimal. Success as an int response code.
public WingResponse transfer(String p, BigDecimal a, String n);
public record WingResponse(int responseCode, String transferId, String message) {}
```

Line them up:

| | Stripe | ABA | Wing |
|---|---|---|---|
| **Money type** | `long` (cents) | `double` | `BigDecimal` |
| **Success signal** | `boolean paid` | `enum APPROVED` | `int == 0` |
| **ID field name** | `chargeId` | `transactionCode` | `transferId` |
| **Pending state** | none | `PROCESSING` | none |
| **Method name** | `charge` | `submit` | `transfer` |

Three names for the same ID. Three representations of "it worked." And `double` for money, which is a bug waiting to happen.

### Now imagine no adapter

Your `PaymentService` grows an if-chain, and inside each branch it juggles a different money type and a different success convention. Then:

- `StripeResponse` appears in your controller's return type, so your public API now mirrors Stripe's field names
- someone writes `if (response.paid())` in a service that also handles ABA, and it doesn't compile, so they add another branch
- Stripe releases v2 and renames `paid` to `status` — you edit forty files
- your unit tests need to construct vendor response objects, so your tests depend on Stripe's SDK
- switching providers to cut fees becomes a six-week project instead of a config change

This is not hypothetical. This is the single most common way a codebase becomes unchangeable.

---

## The solution

### 1. The port — written in *your* language

`gateway/PaymentGateway.java`:

```java
public interface PaymentGateway {
    PaymentProvider provider();
    PaymentResult pay(PaymentCommand command);
}
```

`domain/`:

```java
public enum PaymentProvider { ABA, STRIPE, WING }

public record PaymentCommand(String account, BigDecimal amount, String currency, String reference) {}

public record PaymentResult(PaymentProvider provider, String transactionId, PaymentStatus status, String message) {}

public enum PaymentStatus { SUCCESS, FAILED, PENDING }
```

Study what was chosen here:

- **`BigDecimal` for money** — never `double`, never `float`. Currency arithmetic in binary floating point loses cents.
- **An enum for status** — three named states, not a boolean and not an int.
- **One name for the ID** — `transactionId`, regardless of what the vendor calls it.
- **`PENDING` exists** — because ABA has it, even though two providers don't. The domain models the *union* of what your business must handle.

**The test of a good port:** this interface would be identical if all three vendors vanished tomorrow. It describes *your* need, not any vendor's API. If your port has a method called `charge()` because that's what Stripe calls it, the abstraction has already failed.

### 2. The adapters — where all the ugliness is contained

**Stripe**, converting `BigDecimal` to cents correctly:

```java
@Component
public class StripePaymentAdapter implements PaymentGateway {

    private final StripeSdk sdk;

    public StripePaymentAdapter(StripeSdk s) { sdk = s; }

    public PaymentProvider provider() { return PaymentProvider.STRIPE; }

    public PaymentResult pay(PaymentCommand c) {
        long cents = c.amount().movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
        StripeResponse r = sdk.charge(cents, c.currency().toLowerCase(), c.account());
        return new PaymentResult(provider(), r.chargeId(),
                r.paid() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED,
                r.paid() ? "Paid by Stripe" : r.failureMessage());
    }
}
```

That money conversion line deserves attention: `movePointRight(2)` shifts the decimal, `setScale(0, HALF_UP)` rounds to a whole number of cents, `longValueExact()` throws rather than silently truncating if the value doesn't fit. This is how you convert currency to minor units. The vendor forced this on you; the adapter absorbs it.

**ABA**, mapping enum to enum:

```java
public PaymentResult pay(PaymentCommand c) {
    AbaResponse r = sdk.submit(c.account(), c.amount().doubleValue(), c.currency(), c.reference());
    PaymentStatus s = switch (r.status()) {
        case APPROVED   -> PaymentStatus.SUCCESS;
        case DECLINED   -> PaymentStatus.FAILED;
        case PROCESSING -> PaymentStatus.PENDING;
    };
    return new PaymentResult(provider(), r.transactionCode(), s, r.description());
}
```

**Note the missing `default` branch.** That is deliberate and correct. If ABA adds a fourth status, this stops compiling and you are *forced* to decide what it means. A `default -> PaymentStatus.FAILED` would silently mishandle it forever.

**Wing**, mapping an int:

```java
public PaymentResult pay(PaymentCommand c) {
    WingResponse r = client.transfer(c.account(), c.amount(), c.reference());
    return new PaymentResult(provider(), r.transferId(),
            r.responseCode() == 0 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED, r.message());
}
```

**Study these three side by side — that is the lesson.** Each adapter is about fifteen lines. All vendor weirdness lives in exactly one file per vendor. Nothing downstream knows Stripe uses cents or that Wing signals success with zero.

### ⚠️ The judgment call hiding in Wing

Wing has no `PENDING` concept, so the adapter flattens every non-zero code to `FAILED`. If Wing ever returns a code meaning "transfer accepted, settling overnight," this code reports a failed payment for money that will actually move.

**Adapters are where impedance mismatches get resolved, and that is exactly where real bugs hide.** That line deserves a comment and a conversation with the Wing integration docs. When you write an adapter, the fields you *cannot* map cleanly are the ones to escalate — not quietly default.

### 3. The registry — Strategy again, done better

`gateway/PaymentGatewayRegistry.java`:

```java
@Component
public class PaymentGatewayRegistry {

    private final Map<PaymentProvider, PaymentGateway> gateways;

    public PaymentGatewayRegistry(List<PaymentGateway> list) {
        Map<PaymentProvider, PaymentGateway> m = new EnumMap<>(PaymentProvider.class);
        for (PaymentGateway g : list) {
            if (m.put(g.provider(), g) != null)
                throw new IllegalStateException("Duplicate " + g.provider());
        }
        gateways = Map.copyOf(m);
    }

    public PaymentGateway get(PaymentProvider p) {
        PaymentGateway g = gateways.get(p);
        if (g == null) throw new UnsupportedPaymentProviderException(p);
        return g;
    }

    public List<PaymentProvider> supported() {
        return gateways.keySet().stream().sorted().toList();
    }
}
```

Same `List<T>` injection as the notification service in [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md), but sharper on four counts:

| Detail | Why it matters |
|---|---|
| **Enum key** | Typo-proof; a bad provider is a compile error, not a 500 |
| **`EnumMap`** | Backed by an array indexed by ordinal — faster and smaller than `HashMap` |
| **Explicit duplicate check** | Fails at startup with a message naming the offending provider |
| **`Map.copyOf`** | Genuinely immutable, not just a `final` reference to a mutable map |
| **`supported()`** | The API can advertise its own capabilities — see the `/providers` endpoint |

**Copy this class as your template.** It is the best-written class in this repository.

### 4. Thin service, thin controller

```java
@Service
public class PaymentService {
    private final PaymentGatewayRegistry registry;

    public PaymentResult pay(PaymentProvider p, PaymentCommand c) {
        return registry.get(p).pay(c);
    }

    public List<PaymentProvider> providers() { return registry.supported(); }
}
```

The service is four lines of real code. All the complexity moved to where it belongs.

---

## The Spring piece you actually need

`config/ExternalSdkConfig.java`:

```java
@Configuration
public class ExternalSdkConfig {
    @Bean AbaSdk    abaSdk()    { return new AbaSdk(); }
    @Bean StripeSdk stripeSdk() { return new StripeSdk(); }
    @Bean WingClient wingClient(){ return new WingClient(); }
}
```

**This is the most practically useful Spring lesson in the repo.** Third-party classes cannot be annotated `@Component` — you do not own the source. `@Bean` methods inside a `@Configuration` class are how you pull them into the container so they can be injected.

In real code these read credentials from configuration:

```java
@Bean
StripeSdk stripeSdk(StripeProperties props) {
    return new StripeSdk(props.apiKey(), props.timeout());
}
```

…with `StripeProperties` bound via `@ConfigurationProperties("payment.stripe")` from `application.yml`. Secrets never touch your source, and each environment gets its own values.

---

## Object adapter vs. class adapter

The plain-java module demonstrates both.

**Object adapter (composition) — use this.** The adapter *holds* the adaptee:

```java
public class StripePaymentAdapter implements PaymentGateway {
    private final StripeSdk sdk;   // ← holds it
}
```

**Class adapter (inheritance) — avoid.** The adapter *extends* the adaptee. See `classadapter/TemperatureClassAdapter`. It requires the adaptee to be non-final, gives you no control over the adaptee's public surface (its methods leak through your adapter), can only adapt one class, and is impossible when the vendor ships final classes or interfaces.

Java's single inheritance makes this a dead end quickly. **Always compose.**

---

## Where Adapter already exists

| Example | Adapts |
|---|---|
| `InputStreamReader` (JDK) | byte stream → character stream |
| `Arrays.asList()` | array → `List` |
| `HandlerAdapter` (Spring MVC) | various handler types → one dispatch interface |
| `slf4j` | one logging API → Logback, Log4j2, JUL |
| Spring Data repositories | your interface → JPA / Mongo / Redis |

The last one is worth dwelling on: `JpaRepository` is Spring adapting your domain-language interface onto Hibernate. That is why you can declare `findByEmail` and never write SQL.

---

## When NOT to use Adapter

- **The vendor's interface is already what you want.** Don't wrap `java.util.List` in `MyListAdapter` for symmetry.
- **Exactly one vendor, forever, and it's a commodity.** A wrapper around `java.time` earns nothing.
- **You're adapting your own code.** If you control both sides, fix the interface instead of bridging it.

But note the asymmetry in the risk: an unnecessary adapter costs you one small class. A missing adapter costs you a rewrite. **When a third-party SDK is involved, default to writing the adapter.**

---

## File map

```
practices_with_springboot/adapter-pattern-class-samples/
├── adapter-pattern-plain-java/           ← 8 demos, read selectively
│   └── .../adapter/
│       ├── demo/AdapterCourseDemo.java   ← entry point; uncomment ONE demo at a time
│       ├── payment/
│       │   ├── target/PaymentGateway.java      ← the port
│       │   ├── domain/                          ← your types
│       │   ├── external/{stripe,aba,wing}/      ← three ugly SDKs
│       │   ├── adapter/                         ← the translation layer
│       │   └── service/PaymentService.java
│       ├── classadapter/                 ← inheritance version (read, then avoid)
│       └── storage/                      ← same lesson again as homework — skip
└── adapter-pattern-spring-boot/          ← ⭐ THE MAIN EVENT
    └── .../adapter/
        ├── domain/       PaymentProvider, PaymentCommand, PaymentResult, PaymentStatus
        ├── external/     {stripe,aba,wing} — the vendor SDKs
        ├── gateway/      PaymentGateway, PaymentGatewayRegistry, exception
        │   └── adapter/  Aba/Stripe/Wing PaymentAdapter
        ├── config/       ExternalSdkConfig    ← the @Bean lesson
        ├── service/      PaymentService
        ├── api/          PaymentController + request/response DTOs
        └── test/         PaymentGatewayRegistryTest, AbaPaymentAdapterTest
```

**Reading order:** `external/` (see the mess) → `domain/` + `gateway/PaymentGateway` (see the clean port) → `gateway/adapter/` (see the translation) → `PaymentGatewayRegistry` → `config/` → the two tests.

**Skip:** `classadapter/` after one glance, and all of `storage/` — it repeats the payment lesson.

---

## How to run

Spring Boot module:

```bash
cd practices_with_springboot/adapter-pattern-class-samples/adapter-pattern-spring-boot && mvn spring-boot:run
```

```bash
curl http://localhost:8080/api/v1/payments/providers
```

```bash
curl -X POST http://localhost:8080/api/v1/payments -H "Content-Type: application/json" -d "{\"provider\":\"STRIPE\",\"account\":\"acct_1\",\"amount\":25.50,\"currency\":\"USD\",\"reference\":\"ORD-1\"}"
```

Change `"provider"` to `ABA` or `WING`. **The response shape never changes.** That is the adapter working.

Plain Java module:

```bash
cd practices_with_springboot/adapter-pattern-class-samples/adapter-pattern-plain-java && mvn clean package && java -jar target/adapter-pattern-plain-java-1.0.0.jar
```

Edit `AdapterCourseDemo.java` to uncomment a different demo first — only one runs at a time.

Postman collection and environment are in `practices_with_springboot/adapter-pattern-class-samples/postman/`.

---

## Exercises

1. **Add a fourth provider.** Add `PIPAY` to the `PaymentProvider` enum, write a fake SDK with yet another response shape (try: success as the string `"OK"`, money as a `String`), write the adapter, register the `@Bean`. **`PaymentService`, `PaymentController` and `PaymentGatewayRegistry` must stay untouched.** Run the existing tests afterwards.
2. **Feel the duplicate check.** Make a second adapter also return `PaymentProvider.STRIPE`. Start the app. Read the error. Compare it to what `Collectors.toMap` gives you in the strategy module.
3. **Handle the Wing gap.** Give `WingResponse` a code that means "pending settlement" and decide how the adapter should map it. Notice you cannot answer without a business decision — that is the real lesson.
4. **Test without vendors.** Write a test for `PaymentService` using a fake `PaymentGateway`. Observe that you never touch Stripe, ABA or Wing code. That is what the port bought you.

---

## Self-check

- What is the one test for whether a port is well designed?
- Why does the ABA adapter's `switch` deliberately omit `default`?
- Why does `ExternalSdkConfig` exist at all — why not `@Component` on `StripeSdk`?
- Name three ways `PaymentGatewayRegistry` improves on the strategy module's map.
- What is the difference in *intent* between Adapter and Strategy, given they share a mechanism?

---

## Next

→ **[FACADE_PATTERN.md](FACADE_PATTERN.md)** — the last Tier 1 pattern: simplifying a *sequence* rather than a single call.

## Related

- [SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md) — Adapter is DIP at a system boundary
- [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md) — same `List<T>` mechanism, different intent
- [DECORATOR_PATTERN.md](DECORATOR_PATTERN.md) — also wraps an object, but keeps the same interface
- [README.md](README.md) — full reading order
