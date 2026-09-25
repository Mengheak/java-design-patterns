# Facade Pattern

**Local practice:** [AiAssistantFacade](src/facade/AiAssistantFacade.java) exposes `ask(question)` to validate a question and call `AnswerService`. Package: `facade`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 1 — read fourth. Last of the Tier 1 set.**
> **Module:** `practices_with_springboot/facade-pattern-class-samples` · **Packages:** `com.chheang.mengheak.facade.*`

This is the pattern that answers the question every Spring developer eventually asks: **"how thick should my `@Service` be, and why does my controller have eight dependencies?"**

---

## Intent

> Provide a unified interface to a set of interfaces in a subsystem. Facade defines a higher-level interface that makes the subsystem easier to use.

In plain terms: **one call replaces a sequence of calls across many collaborators.**

---

## The demonstration hiding in plain sight

Open these two files side by side:

- `facade-pattern-plain-java/.../order/client/BeforeFacadeOrderController.java`
- `facade-pattern-plain-java/.../order/facade/OrderFacade.java`

```java
public PlaceOrderResult placeOrder(OrderRequest r) {
    c.validateCustomer(r.customerId());
    v.validate(r);
    i.checkAvailability(r);
    i.reserve(r);
    var pay = p.processPayment(r);
    var ship = s.createShipment(r);
    var inv = n.generateInvoice(r);
    notify.sendConfirmation(r.customerId(), inv);
    a.recordOrderCreated(r);
    return new PlaceOrderResult(r.orderNumber(), pay.transactionId(),
            ship.shipmentNumber(), inv.invoiceNumber(), "ORDER_CREATED");
}
```

**The method bodies are identical.** Same eight dependencies, same nine calls, same return statement. The author renamed the class and changed nothing else.

That is the most important thing in this folder, and the course never states it out loud:

> **A facade does not reduce complexity. It relocates it.**

The nine-step orchestration still exists. It just no longer lives in your HTTP layer.

### What actually changed: the client

```java
public final class OrderController {

    private final OrderFacade facade;

    public OrderController(OrderFacade f) { facade = f; }

    public PlaceOrderResult placeOrder(OrderRequest r) {
        return facade.placeOrder(r);
    }
}
```

**Eight dependencies → one.** And that single change buys you four concrete things:

| Benefit | Why |
|---|---|
| **Testable without HTTP** | `OrderFacade` is a plain object; test the whole order flow with no MockMvc, no web context |
| **Reusable** | A scheduled job, a Kafka listener, and a gRPC endpoint can all call `placeOrder` |
| **Transactional as a unit** | `@Transactional` on the facade wraps the whole sequence; on a controller it usually doesn't work (no proxy boundary you control) |
| **One place to change the flow** | Insert a fraud check between payment and shipping in one file, not in three callers |

---

## The Spring version

`facade-pattern-spring-boot/.../booking/facade/BookingFacade.java` — nine subsystems behind one `book()` call:

```java
@Service
public class BookingFacade {

    // GuestService, RoomAvailabilityService, PricingService, RoomReservationService,
    // PaymentService, BookingWriter, NotificationService, AuditService, LoyaltyService

    public BookingResult book(BookingCommand command) {
        guestService.validate(command.guestId());
        availabilityService.check(command.roomId(), command.checkIn(), command.checkOut());

        BigDecimal totalPrice  = pricingService.calculate(command);
        String reservationId   = reservationService.reserve(command);
        String transactionId   = paymentService.pay(command.paymentToken(), totalPrice);
        String bookingId       = bookingWriter.create(command);

        notificationService.sendConfirmation(command.guestId(), bookingId);
        auditService.record(bookingId);
        loyaltyService.award(command.guestId(), totalPrice);

        return new BookingResult(bookingId, reservationId, transactionId, totalPrice, "CONFIRMED");
    }
}
```

And the controller, which does **nothing but DTO ↔ domain mapping**:

```java
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingFacade bookingFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse book(@Valid @RequestBody CreateBookingRequest request) {
        BookingResult result = bookingFacade.book(new BookingCommand(
                request.guestId(), request.roomId(),
                request.checkIn(), request.checkOut(), request.paymentToken()));

        return new BookingResponse(result.bookingId(), result.reservationId(),
                result.paymentTransactionId(), result.totalPrice(), result.status());
    }
}
```

**That is the right controller size.** Validate input, map to domain, call one thing, map the result back. Nothing else.

> **Rule of thumb:** if a controller has more than two or three injected dependencies, or contains any `if` that isn't about HTTP concerns, a facade is the fix.

Note also the separation of `CreateBookingRequest` (HTTP DTO) from `BookingCommand` (domain type). This looks like duplication and is not: it lets your API evolve independently of your domain, and stops Jackson annotations from colonising your business objects.

---

## The advanced part: compensation

This is where the module earns its keep. `facade-pattern-plain-java/.../booking/facade/CompensatingBookingFacade.java`:

```java
public BookingResult book(BookingRequest x) {
    Reservation res = null;
    BookingPayment payment = null;
    try {
        g.validate(x.guestId());
        a.check(x.roomId(), x.checkIn(), x.checkOut());
        var total = p.calculate(x);
        res = r.reserve(x);
        payment = pay.pay(x.paymentToken(), total);
        var id = w.create(x);
        n.sendConfirmation(x.guestId(), id);
        audit.record(id);
        l.awardPoints(x.guestId(), total);
        return new BookingResult(id, res.id(), payment.transactionId(), total, "CONFIRMED");
    }
    catch (RuntimeException e) {
        if (payment != null && payment.successful()) pay.refund(payment.transactionId());
        if (res != null) r.release(res.id());
        throw e;
    }
}
```

### Why this must live in the facade

The facade is **the only object that knows the whole sequence**, so it is the only object that can undo it. `PaymentService` cannot release a room reservation — it has never heard of rooms. No individual subsystem has enough context to clean up.

### Why `@Transactional` would not save you

This is the line between tutorial Spring and production Spring:

> **A database rollback cannot un-charge a credit card.**

Once the payment API returns success, the money has moved. It is in another company's system. No `@Transactional` annotation reaches across that boundary. You must explicitly **compensate** — call `refund`.

This is a **saga** in miniature. The same reasoning applies to any external side effect: sent emails, published Kafka messages, files written to S3, calls to a partner API. `@Transactional` covers your database. Everything else needs compensation.

### Three details worth copying

1. **Null-initialised trackers.** `res` and `payment` start `null` and are only set after the step succeeds — so the `catch` knows exactly how far it got.
2. **Reverse order of acquisition.** Refund (last acquired) before release (first acquired). Unwind the stack the way you built it.
3. **Rethrow, don't swallow.** `throw e;` — the caller must still learn the booking failed. Compensation cleans up; it does not hide.

### ⚠️ Now compare the Spring version

`facade-pattern-spring-boot`'s `BookingFacade` has **no try/catch at all**. Trace it: if `loyaltyService.award(...)` throws, the guest has already been charged and the booking already written. No rollback happens. The reservation stays held and the money stays taken.

In the plain-java module that's a teaching simplification. **If you ship that shape, it is a real bug.**

Which is the actual lesson: **the facade is where you must think about partial failure**, because it is the only layer that can see it. Every facade you write should make you ask "what if step six fails?"

---

## Facade vs. the neighbours

| Pattern | Shape | Interface |
|---|---|---|
| **Facade** | 1 call → *many* collaborators | **New**, simpler interface |
| **[Adapter](ADAPTER_PATTERN.md)** | 1 call → *one* adaptee | Converts to an interface the client **expects** |
| **[Decorator](DECORATOR_PATTERN.md)** | 1 call → *one* delegate, plus behavior | **Same** interface as the thing it wraps |
| **[Chain of Responsibility](CHAIN_OF_RESPONSIBILITY_PATTERN.md)** | 1 call → a *pipeline*, may stop early | Same interface per link |

The quick discriminator: **Facade simplifies, Adapter converts, Decorator adds, Chain sequences with the option to stop.**

Facade and CoR both handle "a sequence of things." Use CoR when steps are uniform, independently ordered, and any one may halt the flow (validation). Use Facade when steps are heterogeneous and all of them must run (orchestration).

---

## Where Facade already exists

| Example | Hides |
|---|---|
| `JdbcTemplate` | `Connection`, `PreparedStatement`, `ResultSet`, exception translation, resource cleanup |
| `RestTemplate` / `WebClient` | connection pooling, serialization, error handling |
| Spring Boot auto-configuration | the entire manual bean setup of every starter |
| `@SpringBootApplication` | three annotations and a component-scan configuration |

Any `@Service` you have written that coordinates three repositories is already a facade. You just didn't call it one.

---

## When NOT to use Facade

- **The subsystem has one class.** Wrapping one collaborator in a "facade" is just an extra file.
- **Callers genuinely need fine-grained control.** A facade that grows fifteen boolean parameters to accommodate every caller has failed. Offer the facade *and* leave the subsystem accessible.
- **You're using it to dodge a design problem.** If `placeOrder` needs eight subsystems because the domain model is wrong, a facade hides the smell rather than fixing it.

### The real risk: the god facade

A facade that starts at nine subsystems and grows to thirty methods becomes exactly the [SRP violation](SOLID_PRINCIPLES.md) you were trying to escape. **One facade per use case, not one per module.** `BookingFacade.book()` is right; `HotelFacade` with `book`, `cancel`, `refund`, `search`, `review`, `report` is a god object with a pattern name.

---

## File map

```
practices_with_springboot/facade-pattern-class-samples/
├── README.md                              ← the repo's best subfolder README
├── facade-pattern-plain-java/
│   └── .../facade/
│       ├── demo/FacadeCourseDemo.java     ← entry point
│       ├── order/
│       │   ├── subsystem/                 ← 8 services: Customer, Validator, Inventory,
│       │   │                                 Payment, Shipping, Invoice, Notification, Audit
│       │   ├── facade/OrderFacade.java             ← ⭐ compare these two
│       │   ├── client/BeforeFacadeOrderController  ← ⭐ byte-for-byte identical body
│       │   ├── client/OrderController.java         ← 1 dependency
│       │   ├── payment/                   ← a small Adapter, for contrast
│       │   └── domain/
│       └── booking/
│           ├── facade/BookingFacade.java
│           ├── facade/CompensatingBookingFacade.java   ← ⭐ the advanced lesson
│           └── subsystem/  (9 services)
├── facade-pattern-spring-boot/            ← the Spring shape
│   └── .../booking/{api,domain,facade,subsystem}
│       └── test/BookingFacadeTest.java
└── facade-pattern-webflux/                ← reactive version; skip unless you use WebFlux
    └── .../facade/room/ReactiveRoomPublishingFacade.java
```

**Reading order:** `order/subsystem/` → `BeforeFacadeOrderController` → `OrderFacade` (notice they're identical) → `OrderController` → Spring `BookingFacade` + `BookingController` → `CompensatingBookingFacade`.

The WebFlux module shows the same pattern with `Mono`/`Flux` composition. Worth ten minutes if you do reactive; skip it otherwise.

---

## How to run

```bash
cd practices_with_springboot/facade-pattern-class-samples/facade-pattern-plain-java && mvn clean package && java -jar target/facade-pattern-plain-java-1.0.0.jar
```

```bash
cd practices_with_springboot/facade-pattern-class-samples/facade-pattern-spring-boot && mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/v1/bookings -H "Content-Type: application/json" -d "{\"guestId\":\"G-1\",\"roomId\":\"R-101\",\"checkIn\":\"2026-10-01\",\"checkOut\":\"2026-10-03\",\"paymentToken\":\"tok_visa\"}"
```

WebFlux module (different shape, `/api/v1/rooms/{roomId}/submit` with an `X-Owner-Id` header):

```bash
cd practices_with_springboot/facade-pattern-class-samples/facade-pattern-webflux && mvn spring-boot:run
```

Postman collection and environment: `practices_with_springboot/facade-pattern-class-samples/postman/`.

---

## Exercises

1. **Prove the identity.** Diff `BeforeFacadeOrderController` against `OrderFacade`. Confirm for yourself that only the class name differs, then articulate in one sentence what the pattern actually bought.
2. **Fix the real bug.** Add try/catch compensation to the Spring `BookingFacade`, modeled on `CompensatingBookingFacade`. Then write a test where `loyaltyService.award` throws, and assert that a refund and a release both happened.
3. **Insert a step.** Add a fraud check between payment and booking creation. Notice you edit one file, and no caller changes.
4. **Find the boundary.** In the Spring module, ask which steps *must* be compensated and which are fire-and-forget. (Hint: is failing to award loyalty points worth refunding a whole booking? Maybe the answer is to make step nine not throw.)

---

## Self-check

- Why are `BeforeFacadeOrderController` and `OrderFacade` identical, and what does that prove?
- Why can't `@Transactional` replace `CompensatingBookingFacade`?
- In what order does compensation run, and why?
- What distinguishes a facade from a god object?
- When would you use Chain of Responsibility instead of a facade?

---

## Next

**You have finished Tier 1.** Before moving on, do at least one exercise from each of the four documents — reading these patterns is not the same as having used them.

→ Tier 2 begins at **[CHAIN_OF_RESPONSIBILITY_PATTERN.md](CHAIN_OF_RESPONSIBILITY_PATTERN.md)**.

## Related

- [SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md) — Facade is SRP applied to orchestration
- [ADAPTER_PATTERN.md](ADAPTER_PATTERN.md) — the order module contains a small adapter for contrast
- [CHAIN_OF_RESPONSIBILITY_PATTERN.md](CHAIN_OF_RESPONSIBILITY_PATTERN.md) — the other "sequence of steps" pattern
- [README.md](README.md) — full reading order
