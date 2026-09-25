# Chain of Responsibility Pattern

**Local practice:** [QuestionHandler](src/chain_of_responsibility/QuestionHandler.java) links blank, length, and question-mark checks. A failed check throws before the next handler runs. Package: `chain_of_responsibility`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 2 — read after completing all four Tier 1 documents.**
> **Module:** `practices_with_springboot/chain-of-responsibility-pattern-class-samples` · **Packages:** `com.chheang.mengheak.chain.*`

**Read this one for recognition, not for reimplementation.** You will rarely hand-roll a chain in Spring — because Spring already hands you three of them. The value is knowing what you are looking at when you open a `Filter` or configure Spring Security.

---

## Intent

> Avoid coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. Chain the receiving objects and pass the request along the chain until one handles it.

Two distinct uses hide under that definition, and conflating them causes confusion:

1. **Pipeline** — every link runs, each may reject or enrich. *(validation, servlet filters)*
2. **Dispatch** — links are tried in order until one claims the request, then it stops. *(expense approval, exception handlers)*

This module demonstrates both. Keep them separate in your head.

---

## The problem

`chain-pattern-without-cor/.../before/PaymentService.java` — the "before" module exists purely to show the pain:

```java
public final class PaymentService {

    public void pay(PaymentContext context) {
        validateRequest(context);
        authenticate(context);
        authorize(context);
        checkRateLimit(context);
        checkFraud(context);
        processPayment(context);
    }

    private void validateRequest(PaymentContext context) { ... }
    private void authenticate(PaymentContext context)    { ... }
    private void authorize(PaymentContext context)       { ... }
    private void checkRateLimit(PaymentContext context)  { ... }
    private void checkFraud(PaymentContext context)      { ... }
    private void processPayment(PaymentContext context)  { ... }
}
```

**Be fair to this code first.** It is readable. The `pay()` method is a clean table of contents. If your requirements never change, ship it.

Now look at `PaymentServiceWithMoreRequirements.java` in the same package, which is the same class after three more rules arrive. The problems:

| Problem | Detail |
|---|---|
| **One class, many reasons to change** | Security, rate limiting, fraud and payment all live here — an [SRP](SOLID_PRINCIPLES.md) violation |
| **Order is invisible** | The sequence is implicit in method-call order; nothing declares it or enforces it |
| **Not reorderable per context** | Internal transfers should skip fraud. Now you need a flag, and then another flag |
| **Not independently testable** | Testing the fraud rule means constructing the whole service |
| **Not reusable** | The refund flow needs authenticate + authorize + rate limit but not fraud. Copy-paste |
| **Grows without bound** | Ten rules means a 400-line class that four teams edit |

---

## Solution A — the classic linked chain

`chain-pattern-plain-java/.../chain/payment/`:

```java
public interface PaymentHandler {
    void handle(PaymentContext context);
    void setNext(PaymentHandler next);
}
```

```java
public abstract class BasePaymentHandler implements PaymentHandler {

    private PaymentHandler next;

    @Override
    public void setNext(PaymentHandler next) {
        this.next = next;
    }

    protected void next(PaymentContext context) {
        if (next != null) {
            next.handle(context);
        }
    }
}
```

`BasePaymentHandler` is the piece worth noting: it holds the link and null-checks it, so **no concrete handler ever has to**. Every handler then follows one shape:

```java
public final class FraudHandler extends BasePaymentHandler {

    private static final BigDecimal MANUAL_REVIEW_THRESHOLD = new BigDecimal("10000");

    @Override
    public void handle(PaymentContext context) {
        if (context.request().amount().compareTo(MANUAL_REVIEW_THRESHOLD) > 0) {
            throw new IllegalStateException("Payment requires manual review");
        }

        System.out.println("5. Fraud check passed");
        next(context);            // ← explicit hand-off
    }
}
```

**`next(context)` is the whole pattern.** Each handler decides whether the chain continues. Throw, and it stops. Return without calling `next`, and it stops silently. Call `next`, and it proceeds.

### The context object

```java
public final class PaymentContext {
    private final PaymentRequest request;
    private final String userId;
    private final Set<String> permissions;
    private final int requestsToday;
    private boolean processed;

    public void markProcessed() { this.processed = true; }
}
```

A chain needs one parameter that carries everything any handler might need, plus room for handlers to record results. **This is the pattern's main design cost.** Context objects tend to accumulate fields until they become a bag of everything — keep them focused, and prefer immutable inputs with a small mutable result area, as here.

### ⚠️ The trap in `setNext`

Wiring is manual and order-dependent:

```java
validation.setNext(authentication);
authentication.setNext(authorization);
authorization.setNext(rateLimit);
// forget one link and the chain silently stops
```

**A forgotten `setNext` is a silent bug**, not a crash. The request quietly succeeds having skipped fraud checking. This is why the Spring module below abandons linked handlers entirely.

---

## Solution B — the list-based chain (use this in Spring)

`chain-pattern-spring-boot/.../room/validation/`:

```java
public interface RoomPublishingValidator {
    void validate(RoomPublishingContext context);
}
```

```java
@Component
public class OwnershipValidator implements RoomPublishingValidator {

    @Override
    public void validate(RoomPublishingContext context) {
        if (!context.ownerId().equals(context.roomOwnerId())) {
            throw new IllegalStateException("User does not own this room");
        }
    }
}
```

```java
@Component
public class RoomPublishingValidationChain {

    private final List<RoomPublishingValidator> validators;

    public RoomPublishingValidationChain(
            RoomExistsValidator roomExistsValidator,
            OwnershipValidator ownershipValidator,
            PropertyStatusValidator propertyStatusValidator,
            SubscriptionValidator subscriptionValidator,
            RoomCompletenessValidator roomCompletenessValidator,
            MediaValidator mediaValidator
    ) {
        this.validators = List.of(
                roomExistsValidator,
                ownershipValidator,
                propertyStatusValidator,
                subscriptionValidator,
                roomCompletenessValidator,
                mediaValidator
        );
    }

    public void validate(RoomPublishingContext context) {
        for (RoomPublishingValidator validator : validators) {
            validator.validate(context);
        }
    }
}
```

**Compare the two approaches carefully — this is the most useful comparison in the module:**

| | Linked (`setNext`) | List-based |
|---|---|---|
| Handler knows about the next one | Yes | **No** |
| Wiring | Manual, easy to get wrong | Declarative, in one place |
| Order visible | Scattered across setup code | **One readable `List.of(...)`** |
| Forgetting a link | Silent skip | Impossible — it's a compile-time argument |
| Can a handler skip the rest? | Yes, by not calling `next` | Only by throwing |
| Fits Spring DI | Awkwardly | Naturally |

The list version trades one capability (a handler choosing to end the chain quietly) for a large gain in safety and clarity. **For validation pipelines, that trade is always worth it.**

### One design note on this class

The constructor names each validator explicitly rather than taking `List<RoomPublishingValidator>`. That is a deliberate choice: **order is explicit and reviewable**. With `List<T>` injection you would depend on `@Order` annotations scattered across six files.

The cost is that adding a validator means editing this class — an [OCP](SOLID_PRINCIPLES.md) violation. That is the correct trade here, because **in a validation chain the order is business logic**: checking media before checking the room exists would produce a nonsense error message. When order genuinely doesn't matter, switch to `List<T>` injection.

---

## Solution C — collect all errors instead of failing fast

Both versions above throw on the first failure. That is wrong for form validation, where users deserve every error at once.

See `chain-pattern-plain-java/.../chain/validation/`:

```java
// RegistrationValidator collects into List<ValidationError>
// EmailValidator, PasswordValidator, PhoneValidator each contribute
```

**Choose deliberately:**

- **Fail fast** — security checks, expensive steps, anything where later steps are meaningless if an earlier one failed
- **Collect all** — user-facing form validation, import/batch jobs, config validation at startup

---

## Chain of Responsibility in Spring — where you already use it

This is the real payoff of the chapter.

### 1. Servlet filters

`chain-pattern-spring-boot/.../web/RequestIdFilter.java`:

```java
@Component
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader("X-Request-Id");
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        response.setHeader("X-Request-Id", requestId);
        filterChain.doFilter(request, response);     // ← the chain hand-off
    }
}
```

`filterChain.doFilter(...)` **is** `next(context)`. Same pattern, different name. Note `@Order(1)` — that's how you control sequence — and `OncePerRequestFilter`, which guards against the filter running twice on forwards.

Compare with `MaintenanceFilter` in the same package: it can *not* call `doFilter` and write a 503 instead. **That is a handler ending the chain.**

### 2. Spring Security

The entire Spring Security architecture is one long filter chain — roughly fifteen filters in a fixed order, each able to authenticate, reject, or pass through. When you read its docs and see "add your filter before `UsernamePasswordAuthenticationFilter`," you are being asked to insert a link at a specific position.

### 3. `HandlerInterceptor`

`preHandle` returning `false` stops the chain. Same pattern, third API.

### 4. Others

`@ExceptionHandler` resolution (dispatch style — first match wins), `HttpMessageConverter` selection, and Spring Cloud Gateway filters.

---

## When NOT to use it

**Most of the time, in Spring.** Be blunt about this:

- **Bean Validation beats a hand-built chain** for field-level rules. `@NotBlank`, `@Email`, `@Min`, plus a custom `ConstraintValidator` for business rules. You get error collection, i18n, and automatic `400` responses for free. Reach for a validation chain only when rules need cross-field context, external lookups, or a specific order.
- **A few fixed steps that always run in one order** — that's a [Facade](FACADE_PATTERN.md), not a chain. Chains earn their cost when links are reorderable, conditionally skippable, or reusable across flows.
- **Fewer than four steps.** The ceremony exceeds the benefit.
- **Deep chains are hard to debug.** A stack trace through twelve handlers, or worse, a request that silently stopped at link seven, is genuinely painful. Log entry and exit per handler if you build one.

---

## File map

```
practices_with_springboot/chain-of-responsibility-pattern-class-samples/
├── chain-pattern-without-cor/            ← ⭐ START HERE (5 files, the "before")
│   └── .../chain/before/
│       ├── PaymentService.java
│       ├── PaymentServiceWithMoreRequirements.java   ← the same class after 3 new rules
│       └── BeforeChainDemo.java
├── chain-pattern-plain-java/
│   └── .../chain/
│       ├── demo/ChainCourseDemo.java     ← entry point; uncomment one demo
│       ├── payment/     ← ⭐ the classic linked chain + BasePaymentHandler
│       ├── validation/  ← ⭐ collect-all-errors variant
│       ├── expense/     ← dispatch-style chain (TeamLead→Manager→Director→CEO) — skim
│       └── room/        ← homework, same lesson a third time — skip
└── chain-pattern-spring-boot/            ← ⭐ the Spring shape
    └── .../chain/
        ├── room/validation/   RoomPublishingValidationChain + 6 validators
        ├── room/service/      RoomPublishingService
        ├── room/api/          RoomPublishingController
        └── web/               RequestIdFilter, MaintenanceFilter  ← real Spring CoR
```

**Read:** `chain-pattern-without-cor` (all 5 files) → `payment/` → `validation/` → the Spring `room/validation/` + `web/`.

**Skim:** `expense/` — worth five minutes only because it shows dispatch style (each approver handles or passes up by limit).

**Skip:** `room/` in the plain-java module. It repeats the lesson.

---

## How to run

```bash
cd practices_with_springboot/chain-of-responsibility-pattern-class-samples/chain-pattern-without-cor && mvn clean package && java -jar target/chain-pattern-without-cor-1.0.0.jar
```

```bash
cd practices_with_springboot/chain-of-responsibility-pattern-class-samples/chain-pattern-plain-java && mvn clean package && java -jar target/chain-pattern-plain-java-1.0.0.jar
```

```bash
cd practices_with_springboot/chain-of-responsibility-pattern-class-samples/chain-pattern-spring-boot && mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/v1/rooms/R-101/submit -H "X-Owner-Id: OWNER-1"
```

Check the `X-Request-Id` response header — that's `RequestIdFilter`, a real chain link, running. Postman files: `practices_with_springboot/chain-of-responsibility-pattern-class-samples/postman/`.

---

## Exercises

1. **Feel the silent bug.** In the plain-java payment chain, delete one `setNext` call. Run it. Notice the payment succeeds having skipped a check, with no error. Now understand why the Spring module uses a list.
2. **Reorder without editing handlers.** In `RoomPublishingValidationChain`, move `MediaValidator` before `RoomExistsValidator`. Observe that the error message for a nonexistent room becomes nonsense — order is business logic.
3. **Write a filter.** Add a filter that rejects requests missing an `X-Api-Key` header with 401, ordered before `RequestIdFilter`. You are writing a CoR link.
4. **Convert to fail-slow.** Change `RoomPublishingValidationChain` to collect all failures into a list and throw once at the end with every message.
5. **Compare with Bean Validation.** Reimplement two of the six validators as JSR-380 constraints. Decide which approach you'd actually ship.

---

## Self-check

- What is the difference between pipeline-style and dispatch-style CoR?
- Why is a forgotten `setNext` worse than a crash?
- Why does `RoomPublishingValidationChain` name its validators explicitly instead of injecting `List<T>`?
- Which Spring API is CoR that you use every single day?
- When should you use Bean Validation instead of building a chain?

---

## Next

→ **[TEMPLATE_METHOD_PATTERN.md](TEMPLATE_METHOD_PATTERN.md)**

## Related

- [FACADE_PATTERN.md](FACADE_PATTERN.md) — the other "sequence of steps" pattern; know the difference
- [SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md) — chains are SRP applied to a pipeline
- [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md) — the `List<T>` injection mechanism, introduced
- [README.md](README.md) — full reading order
