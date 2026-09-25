# Strategy Pattern

**Local practice:** [PromptStrategy](src/strategy/PromptStrategy.java) has concise and teaching implementations selected when constructing `AnswerService`. Package: `strategy`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 1 — read second, after [SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md).**
> **Module:** `practices_with_springboot/strategy-pattern-class-samples` · **Packages:** `com.chheang.mengheak.strategy_pattern_springboot.{bad,good}`

**The highest payoff-per-line pattern in this repository.** It is 23 files, it takes about ninety minutes, and it will change how you write Spring services permanently.

---

## Intent

> Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy lets the algorithm vary independently from the clients that use it.

In plain terms: **same goal, different ways of achieving it, chosen at runtime.**

Send a notification — by email, SMS, Telegram, or Slack. Calculate shipping — by weight, by zone, or flat rate. Export a report — as CSV, PDF, or JSON. One outcome, many implementations.

---

## The problem

`bad/BadNotificationService.java`:

```java
@Service
public class BadNotificationService {

    public String send(String type, String message) {
        if ("EMAIL".equalsIgnoreCase(type)) {
            return "Email sent: " + message;
        } else if ("SMS".equalsIgnoreCase(type)) {
            return "SMS sent: " + message;
        } else if ("TELEGRAM".equalsIgnoreCase(type)) {
            return "Telegram sent: " + message;
        } else if ("SLACK".equalsIgnoreCase(type)) {
            return "SLACK sent: " + message;
        }

        throw new IllegalArgumentException("Unsupported notification type: " + type);
    }
}
```

You have written this method. Everyone has. Here is what is actually wrong with it — and note that "it's ugly" is not on the list:

| Problem | Consequence |
|---|---|
| **Violates OCP** | Every new channel edits a tested, deployed class |
| **Merge conflicts** | Four developers adding four channels all edit the same lines |
| **Untestable in isolation** | To test SMS logic you instantiate the whole service, including email and Slack |
| **Unbounded growth** | In real code each branch is not one line — it is 40 lines of API calls, retry logic, and error mapping. At six channels this file is 300 lines. |
| **Shared blast radius** | A bug in the Slack branch requires redeploying and re-testing email |

The one-line bodies in the sample hide the last point. Mentally replace each `return` with thirty lines of real SDK code before you judge the pattern's value.

---

## The solution

### 1. The interface carries its own identity

`good/NotificationStrategy.java`:

```java
/**
 * Strategy interface.
 * Same goal: send notification.
 * Different behavior: email, sms, telegram, slack, etc.
 */
public interface NotificationStrategy {

    String type();

    String send(String message);
}
```

**`type()` is the design decision worth studying.** Each strategy declares its own routing key. Nothing external holds a registry mapping `"EMAIL"` to a class — the strategy owns that fact. This is what makes the service never need updating.

### 2. Each algorithm is a `@Component`

```java
@Component
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public String type() {
        return "EMAIL";
    }

    @Override
    public String send(String message) {
        return "Email sent: " + message;
    }
}
```

And the one added later, whose javadoc states the payoff explicitly:

```java
/**
 * NEW REQUIREMENT
 * Add Slack notification.
 * We only add this class.
 * We do not modify NotificationService.
 */
@Component
public class SlackNotificationStrategy implements NotificationStrategy { ... }
```

### 3. The service resolves and delegates — and nothing else

`good/NotificationService.java`:

```java
@Service
public class NotificationService {

    private final Map<String, NotificationStrategy> strategies;

    public NotificationService(List<NotificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.type().toUpperCase(),
                        Function.identity()
                ));
    }

    public String send(String type, String message) {
        NotificationStrategy strategy = strategies.get(type.toUpperCase());

        if (strategy == null) {
            throw new UnsupportedNotificationTypeException(type);
        }

        return strategy.send(message);
    }
}
```

---

## The mechanism — understand this exactly

This is the part to internalize. Three things happen:

1. **Spring collects every bean implementing `NotificationStrategy`** and injects them as a `List`. You wrote no registration code. No `@Configuration`, no manual wiring. Spring scanned the classpath, found four `@Component`s that implement the interface, and handed them over.

2. **You index the list once, at startup.** The `Collectors.toMap` call converts a list into a lookup table. This runs exactly one time, when the bean is constructed — not per request.

3. **The if-chain became `Map.get()`.** Dispatch is now O(1) and, more importantly, *data-driven*. The set of supported types is derived from what exists on the classpath, not from what someone remembered to add to a `switch`.

```
      Spring startup                         Runtime request
┌──────────────────────────┐          ┌───────────────────────────┐
│ scans for @Component     │          │ POST /good/notifications  │
│   implements Strategy    │          │        ?type=SLACK        │
│                          │          │            │              │
│  ┌────────────────────┐  │          │            ▼              │
│  │ Email              │  │          │  strategies.get("SLACK")  │
│  │ Sms       ──────────────────────► │            │              │
│  │ Telegram           │  │  List<T> │            ▼              │
│  │ Slack              │  │          │  strategy.send(message)   │
│  └────────────────────┘  │          └───────────────────────────┘
└──────────────────────────┘
```

Adding a fifth channel means creating one file. `NotificationService` is finished forever.

---

## Four things to take with you

### 1. `List<T>` injection is the core idiom

```java
public SomeService(List<SomeInterface> impls) { ... }
```

**This one constructor replaces most if-chains you will ever write.** Commit it to memory. It reappears in [ADAPTER_PATTERN.md](ADAPTER_PATTERN.md), [FACTORY_PATTERN.md](FACTORY_PATTERN.md), [CHAIN_OF_RESPONSIBILITY_PATTERN.md](CHAIN_OF_RESPONSIBILITY_PATTERN.md) and [TEMPLATE_METHOD_PATTERN.md](TEMPLATE_METHOD_PATTERN.md) — four more patterns, same mechanism.

If the list must be ordered, add `@Order(1)` / `@Order(2)` to the components or implement `Ordered`. Spring respects it.

### 2. Spring can build the map for you — but don't

Spring will inject `Map<String, NotificationStrategy>` directly, keyed by **bean name**:

```java
public NotificationService(Map<String, NotificationStrategy> strategies) { ... }
// keys: "emailNotificationStrategy", "smsNotificationStrategy", ...
```

Fewer lines, but the routing key is now the class name. Rename the class and you silently break the API. You can force the name with `@Component("EMAIL")` — the [factory](FACTORY_PATTERN.md) and [template method](TEMPLATE_METHOD_PATTERN.md) modules in this repo both do exactly that, and it works.

**The explicit `type()` method used here is still the better design.** It decouples the routing key from the class name, it is visible in the interface contract, and it survives refactoring. Prefer it.

### 3. `Collectors.toMap` throws on duplicates — that's a feature

If two strategies both return `"EMAIL"`, `Collectors.toMap` throws `IllegalStateException` (its default merge function refuses duplicate keys). Your application **fails at startup**, not silently in production at 2am with notifications going to the wrong channel.

Compare with `PaymentGatewayRegistry` in the adapter module, which detects the same problem explicitly and throws a message naming the offending provider. That version is friendlier. Both are correct; neither fails silently.

### 4. Use an enum, not a `String`

This is the sample's weakest point. `type()` returns `String` and the controller accepts `@RequestParam String type`, so `"EMIAL"` compiles fine and fails at runtime.

The adapter module gets this right:

```java
public enum PaymentProvider { ABA, STRIPE, WING }
```

With an enum you get compile-time safety, IDE autocomplete, exhaustive `switch` checking, a natural `/providers` endpoint that enumerates valid values, and `EnumMap` for the lookup. **Upgrade `type()` to return an enum in your own code.**

---

## Where Strategy already exists in Spring

You use it daily:

| Spring type | Strategies |
|---|---|
| `PasswordEncoder` | `BCryptPasswordEncoder`, `Argon2PasswordEncoder`, `Pbkdf2PasswordEncoder` |
| `HttpMessageConverter` | Jackson JSON, XML, form, String — Spring picks by `Content-Type` |
| `ViewResolver` | Thymeleaf, JSP, FreeMarker |
| `CacheManager` | Caffeine, Redis, simple in-memory |
| `AuthenticationProvider` | DAO, LDAP, OAuth2 |
| `Comparator<T>` (JDK) | The textbook example — `List.sort(comparator)` |

Recognizing this is the point: when you see a Spring interface with multiple implementations and a resolution step, you are looking at Strategy.

---

## When NOT to use Strategy

Be honest about this, because pattern courses rarely are:

- **Two branches that will never grow.** An interface plus two `@Component`s to choose between "log to console" and "log to file" is strictly worse than an `if`.
- **The branches share most of their logic.** If email and SMS are 90% identical with one differing step, you want [Template Method](TEMPLATE_METHOD_PATTERN.md), not Strategy.
- **The choice is made at compile time, not runtime.** If the implementation is fixed per deployment, use a Spring profile or `@ConditionalOnProperty` and a single bean.
- **The "algorithms" are really just data.** Five branches that differ only by a rate or a label? Use a `Map<String, BigDecimal>` or a config property, not five classes.

**The test:** if a new branch arrives roughly as often as a new release, use Strategy. If a new branch has arrived twice in three years, leave the `if`.

---

## Strategy vs. the neighbours

| Pattern | What varies | Who owns the implementations |
|---|---|---|
| **Strategy** | The algorithm, chosen at runtime | You |
| **[Adapter](ADAPTER_PATTERN.md)** | The external API being wrapped | A third-party vendor |
| **[Factory](FACTORY_PATTERN.md)** | Which object gets *created* | You — and it often *returns* a strategy |
| **[Template Method](TEMPLATE_METHOD_PATTERN.md)** | A few steps inside a fixed sequence | You, via subclassing |

Strategy and Adapter look nearly identical in code — both are `List<T>` injection into a registry. **The difference is intent: Strategy chooses between behaviors you wrote; Adapter hides APIs you didn't.**

Strategy and Factory are frequently confused because a factory usually returns a strategy. The `practices_with_springboot/factory-pattern-class-samples/plain-java/lesson03_strategy` package is literally both at once.

---

## File map

```
practices_with_springboot/strategy-pattern-class-samples/
├── strategy-pattern-plan-java/            ← plain Java (note: "plan" is a typo in the repo)
│   └── src/com/chheang/mengheak/
│       ├── bad/BadNotificationService.java
│       ├── good/  NotificationStrategy, Email/Sms/Telegram/Slack, Factory, Service
│       └── Main.java
└── strategy-pattern-springboot/           ← START HERE
    └── .../strategy_pattern_springboot/
        ├── bad/   BadNotificationService, BadNotificationController
        └── good/  NotificationStrategy, 4 strategies,
                   NotificationService, NotificationController,
                   UnsupportedNotificationTypeException
```

Skip the plain-java module. Go straight to the Spring one — the `List<T>` injection *is* the lesson, and plain Java can't show it.

---

## How to run

```bash
cd practices_with_springboot/strategy-pattern-class-samples/strategy-pattern-springboot
mvn spring-boot:run
```

Then compare the two endpoints. They behave identically:

```bash
curl "http://localhost:8080/bad/notifications/send?type=SLACK&message=hello"
```

```bash
curl "http://localhost:8080/good/notifications/send?type=SLACK&message=hello"
```

**That identical behavior is the whole point.** The pattern buys you nothing today. It buys you the fifth, sixth and seventh channel.

---

## Exercises

1. **Add a channel.** Create `WhatsAppNotificationStrategy`. Confirm you touched exactly one file and that `/good/notifications/send?type=WHATSAPP` works. Then add the same channel to the `bad` package and compare the diffs.
2. **Break it deliberately.** Make two strategies both return `"EMAIL"` from `type()`. Start the app. Read the `IllegalStateException`. Understand why failing at startup beats failing in production.
3. **Convert to an enum.** Change `type()` to return a `NotificationType` enum, update the map to `EnumMap`, and change the controller parameter. Notice that a typo is now a compile error.
4. **Add cross-cutting behavior.** Make every strategy log its duration — *without* editing the four strategy classes. (Hint: this is [Decorator](DECORATOR_PATTERN.md), and it composes with Strategy cleanly.)

---

## Self-check

- What exactly does Spring do with `List<NotificationStrategy>` at startup?
- Why does `type()` on the interface beat `@Component("EMAIL")` + bean-name map?
- Name two situations where you should *not* use Strategy.
- What is the one-sentence difference between Strategy and Adapter?

---

## Next

→ **[ADAPTER_PATTERN.md](ADAPTER_PATTERN.md)** — the same mechanism, aimed at third-party APIs instead of your own code.

## Related

- [SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md) — Strategy is OCP made concrete
- [FACTORY_PATTERN.md](FACTORY_PATTERN.md) — how strategies get selected and created
- [TEMPLATE_METHOD_PATTERN.md](TEMPLATE_METHOD_PATTERN.md) — the alternative when branches share most of their logic
- [README.md](README.md) — full reading order
