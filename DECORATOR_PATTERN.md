# Decorator Pattern

**Local practice:** [LoggingAiModel](src/decorator/LoggingAiModel.java) and [TimingAiModel](src/decorator/TimingAiModel.java) wrap the `AiModel` interface. Package: `decorator`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 3** · **Module:** `practices_with_springboot/decorator-pattern-class-samples` · **Packages:** `com.chheang.mengheak.decorator.*`

**Read the problem, understand the mechanism, then know that Spring AOP already does this for you.** `@Transactional`, `@Cacheable`, `@Async` and `@Retryable` are decorators generated at runtime. Hand-written decorators are for the cases AOP cannot express.

---

## Intent

> Attach additional responsibilities to an object dynamically. Decorators provide a flexible alternative to subclassing for extending functionality.

The defining property, and the one that separates it from every neighbouring pattern:

> **A decorator implements the same interface as the thing it wraps.**

That is what makes decorators stackable. The caller cannot tell how many layers are present, because every layer looks identical from outside.

---

## The problem: the class explosion

`decorator-pattern-plain-java/.../decorator/inheritance/`:

```java
public class LoggedEmailNotificationSender {
    public void send(String message) {
        System.out.println("[LOG] before");
        System.out.println("EMAIL: " + message);
        System.out.println("[LOG] after");
    }
}
```

```java
public class LoggedEncryptedEmailNotificationSender {
    public void send(String message) {
        System.out.println("[LOG] before");
        System.out.println("EMAIL: ENCRYPTED[" + message + "]");
        System.out.println("[LOG] after");
    }
}
```

Now count. With 2 channels (email, SMS) and 4 optional behaviours (logging, encryption, retry, metrics) you need **2 × 2⁴ = 32 classes** to cover every combination. Add a third channel: 48. Add a fifth behaviour: 96.

And the logging code is copy-pasted into every one of them. Fix a logging bug and you edit 32 files.

This is the **combinatorial explosion of inheritance**, and it is the clearest "before" example in this whole repository. Run `InheritanceExplosionDemo` to see it stated.

---

## The solution

### 1. The interface

```java
public interface NotificationSender {
    void send(String message);
}
```

### 2. The base decorator — holds a delegate of the same type

```java
public abstract class NotificationDecorator implements NotificationSender {

    protected final NotificationSender delegate;

    protected NotificationDecorator(NotificationSender delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate is required");
    }
}
```

**Read that class declaration twice.** It `implements NotificationSender` *and* holds a `NotificationSender`. That recursive shape is the entire pattern — it is what allows a decorator to wrap another decorator.

The `Objects.requireNonNull` matters: a null delegate would fail deep inside the stack with an unhelpful NPE. Fail at construction instead.

### 3. Each decorator adds one behaviour

```java
public final class LoggingNotificationDecorator extends NotificationDecorator {

    public LoggingNotificationDecorator(NotificationSender delegate) {
        super(delegate);
    }

    @Override
    public void send(String message) {
        System.out.println("[LOG] Sending notification");
        delegate.send(message);                        // ← delegate, don't replace
        System.out.println("[LOG] Notification sent");
    }
}
```

A more substantial one:

```java
public final class RetryNotificationDecorator extends NotificationDecorator {

    private final int maxAttempts;

    public RetryNotificationDecorator(NotificationSender delegate, int maxAttempts) {
        super(delegate);
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void send(String message) {
        RuntimeException lastFailure = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("[RETRY] attempt=" + attempt);
                delegate.send(message);
                return;
            } catch (RuntimeException exception) {
                lastFailure = exception;
            }
        }

        throw lastFailure;
    }
}
```

Note that the retry decorator calls `delegate.send(...)` **multiple times**. A decorator is not limited to "before and after" — it controls *whether and how often* the delegate runs. That is what makes retry, caching, circuit-breaking and rate-limiting all expressible as decorators.

### 4. Compose at the composition root

`decorator-pattern-spring-boot/.../config/PaymentConfiguration.java`:

```java
@Configuration
public class PaymentConfiguration {

    @Bean
    public PaymentGateway paymentGateway() {
        PaymentGateway core = new AbaPaymentAdapter(new AbaSdk());

        return new MetricsPaymentDecorator(
                new RetryPaymentDecorator(
                        new LoggingPaymentDecorator(core),
                        3
                )
        );
    }
}
```

**This is the Spring lesson of the module.** The `@Bean` method is the composition root. Everything downstream injects a plain `PaymentGateway` and has no idea it is holding three layers.

Note also that `core` is an **[Adapter](ADAPTER_PATTERN.md)** — the patterns stack naturally. Adapter converts the vendor API; decorators add cross-cutting behaviour on top.

---

## ⭐ Order matters — this is the exam question

```
 MetricsPaymentDecorator
   └── RetryPaymentDecorator (3)
         └── LoggingPaymentDecorator
               └── AbaPaymentAdapter  ← the real work
```

A call travels **outside-in**, and the return travels back **inside-out**. With the wiring above:

- **Metrics** wraps retry, so it times the *entire* operation including all retry attempts
- **Logging** is inside retry, so it logs **once per attempt** — three log pairs for three attempts

Swap retry and logging and the meaning changes completely: logging outside retry logs once total, hiding the fact that two attempts failed.

**Decorator order is a semantic decision, not a stylistic one.** Get it wrong and your metrics lie.

Two real examples to reason about:

| Order | Effect |
|---|---|
| `Cache(Retry(service))` | Retries are cached — a cached success skips retry entirely. Usually right. |
| `Retry(Cache(service))` | Every retry attempt re-checks the cache. Usually pointless. |
| `Metrics(Retry(x))` | Measures total user-perceived latency. |
| `Retry(Metrics(x))` | Measures each attempt separately. |

Run `DecoratorOrderDemo` in the plain-java module — it exists specifically to demonstrate this.

---

## ⚠️ Why you will rarely write these in Spring

**Spring AOP already generates decorators for you at runtime.** When you write:

```java
@Transactional
public void transfer(...) { ... }
```

Spring creates a proxy that implements your interface, wraps your bean, begins a transaction, calls your method, and commits or rolls back. **That proxy is a decorator.** You did not write it; the framework generated it from an annotation.

Same for `@Cacheable`, `@Async`, `@Retryable` (Spring Retry), `@RateLimiter` / `@CircuitBreaker` (Resilience4j), and `@PreAuthorize`.

So the honest guidance:

| Situation | Use |
|---|---|
| Transactions, caching, async, retry, security | **Annotations.** Do not hand-roll. |
| Wrapping a third-party object you don't own | **Explicit decorator** — no annotation can touch it |
| Order must be exact and visible | **Explicit decorator** — `@Order` on aspects is harder to reason about |
| Different wrapping per bean instance | **Explicit decorator** — annotations are per-class |
| Self-invocation (`this.method()`) needs the behaviour | **Explicit decorator** — proxies don't intercept internal calls |

That last row is a real and frequently-hit limitation: calling an `@Transactional` method from another method **in the same class** bypasses the proxy entirely and the annotation does nothing. An explicit decorator has no such hole.

The `PaymentConfiguration` example above is a legitimate case: `AbaSdk` is third-party, and the retry/metrics/logging order is explicit and reviewable.

---

## Decorator vs. the neighbours

| Pattern | Interface after wrapping | Purpose |
|---|---|---|
| **Decorator** | **Same** as the wrapped object | **Add** behaviour |
| **[Adapter](ADAPTER_PATTERN.md)** | **Different** — converted to what the client wants | **Convert** an interface |
| **[Facade](FACADE_PATTERN.md)** | **New**, simpler, over many objects | **Simplify** a subsystem |
| **Proxy** | Same as the wrapped object | **Control access** (lazy load, remote, security) |

Decorator and Proxy are structurally identical; only intent differs. Decorator adds features; Proxy controls access. Spring's AOP proxies genuinely do both, which is why the terms blur in Spring documentation.

**The discriminator:** after wrapping, has the interface changed? No → Decorator or Proxy. Yes → Adapter or Facade.

---

## Where Decorator already exists

| Example | Layers |
|---|---|
| `java.io` | `new BufferedReader(new InputStreamReader(new FileInputStream(f)))` — the textbook example |
| Spring AOP proxies | `@Transactional`, `@Cacheable`, `@Async` |
| `HttpServletRequestWrapper` | wraps a request to modify headers or body |
| Spring Security filter chain | each filter wraps the rest |
| `Collections.unmodifiableList(...)` | same `List` interface, mutation removed |

Run `JavaIoDecoratorDemo` — the `java.io` hierarchy is the clearest real-world instance, and once you see it you cannot unsee it.

---

## When NOT to use it

- **The behaviour applies to every method of every bean.** That's AOP's job. Use an aspect.
- **Only one decorator will ever exist.** Put the code in the class.
- **Debugging cost outweighs the benefit.** A five-layer stack produces enormous stack traces, and stepping through in a debugger is tedious.
- **The interface is wide.** Decorating a 20-method interface means 20 delegating methods per decorator — mostly boilerplate. (Kotlin's `by` delegation solves this; Java does not.)

---

## File map

```
practices_with_springboot/decorator-pattern-class-samples/
├── decorator-pattern-plain-java/.../decorator/
│   ├── demo/DecoratorCourseDemo.java       ← entry point; uncomment one demo
│   ├── inheritance/       ← ⭐ READ FIRST — the class explosion
│   ├── notification/      ← ⭐ the pattern: Sender, Decorator base,
│   │                          Logging / Encryption / Retry / Metrics
│   ├── payment/           ← same shape over an Adapter
│   └── storage/           ← homework; Local + S3 with compression/encryption — skip
└── decorator-pattern-spring-boot/.../decorator/
    ├── config/PaymentConfiguration.java    ← ⭐⭐ READ — the composition root
    ├── payment/           PaymentGateway, PaymentGatewayDecorator,
    │                      Logging / Retry / Metrics, AbaPaymentAdapter
    ├── api/               PaymentController, GlobalExceptionHandler
    └── test/RetryPaymentDecoratorTest.java ← ⭐ read the test
```

**Minimum viable reading:** `inheritance/` → `notification/NotificationDecorator` + `LoggingNotificationDecorator` + `RetryNotificationDecorator` → `config/PaymentConfiguration` → the retry test. That is about twenty minutes and covers everything that matters.

**Skip:** all of `storage/`.

---

## How to run

```bash
cd practices_with_springboot/decorator-pattern-class-samples/decorator-pattern-plain-java && mvn clean package && java -jar target/decorator-pattern-plain-java-1.0.0.jar
```

Edit `DecoratorCourseDemo.java` to select a demo — `DecoratorOrderDemo` and `RetryDecoratorDemo` are the two worth running.

```bash
cd practices_with_springboot/decorator-pattern-class-samples/decorator-pattern-spring-boot && mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/payments -H "Content-Type: application/json" -d "{\"account\":\"001\",\"amount\":25.00}"
```

Watch the console: you will see the metrics, retry and logging output nested in the order the configuration wired them.

---

## Exercises

1. **Count the explosion.** Before reading `notification/`, write down how many classes `inheritance/` would need for 3 channels × 4 behaviours. Then confirm the decorator version needs 3 + 4 + 1.
2. **Reorder and observe.** In `PaymentConfiguration`, swap `RetryPaymentDecorator` and `LoggingPaymentDecorator`. Force a failure. Count the log lines before and after. Explain the difference.
3. **Add a decorator.** Write `TimingPaymentDecorator` that prints elapsed milliseconds. Decide where in the stack it belongs and defend the choice.
4. **Replace it with AOP.** Rewrite `LoggingPaymentDecorator` as a Spring `@Aspect`. Compare: which is easier to read? Which makes the ordering clearer?
5. **Hit the self-invocation trap.** Put `@Retryable` on a method, call it from another method in the same class, and watch the retry not happen. Then fix it with an explicit decorator.

---

## Self-check

- What one property makes decorators stackable?
- Why does `RetryNotificationDecorator` prove decorators aren't just "before and after"?
- Given `Metrics(Retry(Logging(core)))`, how many times does logging fire across three attempts?
- What is the structural difference between Decorator and Proxy? Between Decorator and Adapter?
- Name two situations where an explicit decorator beats a Spring annotation.

---

## Next

→ **[FACTORY_PATTERN.md](FACTORY_PATTERN.md)** — the last document.

## Related

- [ADAPTER_PATTERN.md](ADAPTER_PATTERN.md) — the core object being decorated here is an adapter
- [FACADE_PATTERN.md](FACADE_PATTERN.md) — compare the "what changes about the interface" table
- [README.md](README.md) — full reading order
