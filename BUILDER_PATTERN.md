# Builder Pattern

**Local practice:** [AiRequest](src/builder/AiRequest.java) builds immutable requests with defaults and validation in `build()`. Package: `builder`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 2** · **Module:** `practices_with_springboot/builder-pattern-class-samples` · **Packages:** `com.chheang.mengheak.builder.*`

**Know why it exists. Skip most of the ceremony.** In practice you will type `@Builder` from Lombok or use a `record` and never hand-write a builder. But the *one* thing Lombok does not give you — validation at `build()` — is the thing worth learning here.

This module has nine lesson packages. **Read three.**

---

## Intent

> Separate the construction of a complex object from its representation, so the same construction process can create different representations.

The practical framing: **a readable, safe way to construct an object with many optional fields, ending in a valid immutable instance.**

---

## The problem, in three stages

### Stage 1 — the huge constructor

`lesson01_problem/Room.java` — one constructor with many parameters:

```java
Room room = new Room("R-101", "Deluxe", new BigDecimal("120"), "USD", true, false, true, 2, "SEA_VIEW");
```

What is the second `true`? Is `2` the floor or the guest capacity? You cannot tell without opening the class. Worse, swap two adjacent parameters of the same type and **it still compiles** — a silent bug.

### Stage 2 — telescoping constructors

`lesson02_telescoping/Room.java`:

```java
public Room(String roomCode) {
    this(roomCode, BigDecimal.ZERO);
}

public Room(String roomCode, BigDecimal price) {
    this(roomCode, price, "USD");
}

public Room(String roomCode, BigDecimal price, String currency) {
    this(roomCode, price, currency, false);
}

public Room(String roomCode, BigDecimal price, String currency, boolean wifi) {
    this.roomCode = roomCode;
    this.price = price;
    this.currency = currency;
    this.wifi = wifi;
}
```

This is the classic anti-pattern, and it fails in a specific way: **you can only skip parameters from the right.** Want `roomCode` and `wifi` but the default currency? There is no constructor for that. Add one and it clashes with an existing signature. With four optional fields you need up to sixteen constructors to cover every combination.

### Stage 3 — JavaBeans setters

`lesson03_javabeans/Room.java` — a no-arg constructor plus setters:

```java
Room room = new Room();
room.setRoomCode("R-101");
room.setPrice(new BigDecimal("120"));
// forgot setCurrency()
```

Readable, but two fatal flaws:

1. **The object is invalid between construction and the last setter.** If another thread reads it, or an exception is thrown midway, you have a half-built `Room` in your system.
2. **It cannot be immutable.** No `final` fields, so the object can be mutated by anyone, forever.

---

## The solution

### Classic builder

`lesson04_classic` and `lesson05_immutable` show the full shape. The essentials:

- a `private` constructor taking the builder, so nothing else can construct the object
- `final` fields on the product
- a static nested `Builder` class with one method per field, each returning `this`
- `build()` as the single exit point

### ⭐ The lesson that actually matters: validation at `build()`

`lesson06_validation/Room.java`:

```java
public final class Room {

    private final String roomCode;
    private final BigDecimal price;
    private final String currency;

    private Room(Builder builder) {                 // ← private
        this.roomCode = builder.roomCode;
        this.price = builder.price;
        this.currency = builder.currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String roomCode;
        private BigDecimal price;
        private String currency;

        public Builder roomCode(String roomCode) { this.roomCode = roomCode; return this; }
        public Builder price(BigDecimal price)   { this.price = price;       return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }

        public Room build() {
            if (roomCode == null || roomCode.isBlank()) {
                throw new IllegalArgumentException("Room code is required");
            }
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero");
            }
            if (currency == null || currency.isBlank()) {
                throw new IllegalArgumentException("Currency is required");
            }
            return new Room(this);
        }
    }
}
```

**This is the whole point of the pattern, and it is the one thing `@Builder` will not do for you.**

Three properties combine here:

1. **The constructor is `private`** — the *only* path to a `Room` is `build()`.
2. **`build()` validates before constructing** — so an invalid `Room` cannot be created.
3. **All fields are `final`** — so it cannot become invalid later.

Together these give you a type-level guarantee: **if you are holding a `Room`, it is valid.** No null checks at the call site. No defensive validation in every service that receives one. The type itself carries the promise.

That is a genuinely powerful idea and it is why the builder survives even though Lombok automates the boring parts.

---

## What you will actually use in Spring

### Lombok `@Builder`

`spring-boot/.../builder/lombok/Room.java`:

```java
@ToString
@Builder
public class Room {
    private String roomCode;
    private String roomName;
    private BigDecimal price;
}
```

```java
Room room = Room.builder()
        .roomCode("R-101")
        .roomName("Deluxe")
        .price(new BigDecimal("120"))
        .build();
```

Three annotations replace about sixty lines. **Use this.** Hand-writing `lesson04`-style builders in production is wasted effort.

**Two things to fix in that sample, though:**

- The fields are not `final` and there is no `@Value` or `@AllArgsConstructor(access = PRIVATE)`, so the object is **mutable** — you lose half the benefit. Prefer `@Builder` together with `@Value`, or put `@Builder` on a `record`.
- There is no validation. See the recipe below.

### Getting validation back with Lombok

Lombok honours a hand-written `build()` if you declare the builder class yourself:

```java
@Builder
public record Room(String roomCode, BigDecimal price, String currency) {

    public static class RoomBuilder {
        public Room build() {
            if (roomCode == null || roomCode.isBlank())
                throw new IllegalArgumentException("Room code is required");
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Price must be greater than zero");
            return new Room(roomCode, price, currency);
        }
    }
}
```

Alternatively, put the check in a **compact constructor** on the record — simpler, and it guards *every* construction path, not just the builder:

```java
public record Room(String roomCode, BigDecimal price, String currency) {
    public Room {
        if (roomCode == null || roomCode.isBlank())
            throw new IllegalArgumentException("Room code is required");
    }
}
```

**Prefer the compact constructor.** It's less code and impossible to bypass.

### Records vs. builders

Since Java 16, a `record` gives you immutability, `equals`, `hashCode` and `toString` for free. **A record with three or four fields does not need a builder** — the canonical constructor is clear enough. Reach for a builder when you have **five or more fields**, or **several optional ones**.

This repo's own [adapter module](ADAPTER_PATTERN.md) uses plain records (`PaymentCommand`, `PaymentResult`) with no builders, and that is the right call at four fields.

### Builders you already use

`spring-boot/.../builder/WebClientConfig.java`:

```java
@Bean
public WebClient roomWebClient() {
    return WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader("X-App-Name", "Builder Pattern Demo")
            .build();
}
```

You have been using builders all along:

| Builder | Where |
|---|---|
| `WebClient.builder()` | Spring WebFlux |
| `RestClient.builder()` | Spring 6.1+ |
| `HttpSecurity` (the `http.authorizeHttpRequests(...)` chain) | Spring Security |
| `UriComponentsBuilder` | Spring Web |
| `Stream.Builder`, `Calendar.Builder`, `HttpRequest.newBuilder()` | JDK |
| `StringBuilder` | JDK — the original, though it predates the name |

---

## When NOT to use a builder

- **Fewer than four fields.** A constructor or record is clearer.
- **All fields required, no optionals.** Use the constructor — the compiler already enforces completeness, and a builder *weakens* that (you can forget a field and only find out at runtime).
- **The object is mutable by design.** Builders exist to produce immutable values. A JPA `@Entity` that Hibernate mutates does not want one.
- **You want compile-time completeness checking.** This is the builder's one real weakness: forgetting `.price(...)` compiles fine and fails at runtime. A constructor would not compile. (A "staged builder" fixes this but the ceremony is rarely worth it.)

---

## File map

```
practices_with_springboot/builder-pattern-class-samples/
├── plain-java/.../builder/
│   ├── lesson01_problem/       ← huge constructor          — skim
│   ├── lesson02_telescoping/   ← ⭐ READ — the classic anti-pattern
│   ├── lesson03_javabeans/     ← setters; note the two flaws — skim
│   ├── lesson04_classic/       ← hand-written builder      — skim once
│   ├── lesson05_immutable/     ← adds final fields         — skim
│   ├── lesson06_validation/    ← ⭐⭐ READ — the real payoff
│   ├── lesson07_reusable/      ← reusing a builder instance — skip
│   ├── lesson08_java_examples/ ← JDK builders              — skim
│   └── lesson09_homework/      ← Guest/Reservation/Room    — skip
└── spring-boot/.../builder/
    ├── lombok/Room.java        ← ⭐ READ — what you'll actually write
    ├── WebClientConfig.java    ← ⭐ READ — a builder you already use
    ├── RoomResponse.java
    └── DemoController.java
```

**Read three packages: `lesson02_telescoping` (the problem), `lesson06_validation` (the payoff), and the `lombok` + `WebClientConfig` files (the reality).** The other six lessons hand-write what one annotation does.

---

## How to run

```bash
cd practices_with_springboot/builder-pattern-class-samples/plain-java && mvn clean package
```

```bash
mvn exec:java -Dexec.mainClass="com.chheang.mengheak.builder.lesson06_validation.BuilderValidationDemo"
```

Other demos follow the same pattern: `lesson02_telescoping.TelescopingConstructorDemo`, `lesson04_classic.ClassicBuilderDemo`, `lesson08_java_examples.JavaBuilderExamplesDemo`.

Spring Boot module (Java 17, Spring Boot 3.5.3 — note it differs from the Java 21 modules):

```bash
cd practices_with_springboot/builder-pattern-class-samples/spring-boot && mvn spring-boot:run
```

```bash
curl http://localhost:8080/demo/room
```

---

## Exercises

1. **Feel the telescoping failure.** Using `lesson02_telescoping.Room`, construct a room with a `roomCode` and `wifi = true` but the default currency. You cannot. Understand exactly why.
2. **Break the invariant.** In `lesson06_validation`, try to create an invalid `Room` any way you can — reflection aside, you cannot. That guarantee is what the private constructor buys.
3. **Modernize it.** Rewrite `lesson06_validation.Room` as a `record` with a compact constructor. Compare line counts: roughly 60 → 8.
4. **Fix the Lombok sample.** Make `spring-boot/.../lombok/Room` immutable and validating. Try both approaches (custom `build()` and compact constructor) and decide which you prefer.
5. **Find one in the wild.** Open `WebClient.builder()` in your IDE and trace what `build()` actually constructs.

---

## Self-check

- Why can telescoping constructors only omit parameters from the right?
- What are the two fatal flaws of the JavaBeans setter approach?
- What three things must be true for `build()` to guarantee a valid object?
- What is the one thing Lombok's `@Builder` does *not* give you?
- When is a `record` the better answer than a builder?

---

## Next

**End of Tier 2.** → Tier 3 begins at **[DECORATOR_PATTERN.md](DECORATOR_PATTERN.md)**.

## Related

- [ADAPTER_PATTERN.md](ADAPTER_PATTERN.md) — uses plain records where a builder isn't warranted
- [TEMPLATE_METHOD_PATTERN.md](TEMPLATE_METHOD_PATTERN.md) — `PaymentTransaction.builder()` appears there
- [README.md](README.md) — full reading order
