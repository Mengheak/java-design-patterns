# SOLID Principles

**Local practice:** [AnswerService](src/dependency_injection/AnswerService.java) receives `AiModel` and `PromptStrategy` through its constructor. This demonstrates dependency inversion with manual dependency injection. Package: `dependency_injection`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 1 — read this first.** Nothing else in this repository makes sense until DIP clicks.
> **Module:** `practices_with_springboot/solid-sample-project` · **Package:** `com.chheang.mengheak.solid`

---

## Why this comes before every pattern

Design patterns are *consequences* of SOLID, not additions to it. Strategy is Open/Closed made concrete. Adapter is Dependency Inversion applied at a system boundary. Facade is Single Responsibility applied to orchestration.

If you learn the patterns without the principles, you learn five shapes and no judgment about when to use them. That's how codebases end up with an `AbstractNotificationStrategyFactoryProvider` that has exactly one implementation.

Read the five principles in this order: **D → I → S → O**. (L is last and matters least in day-to-day Spring work.) That is not the order of the acronym, and it is deliberate — DIP is the one Spring is built on.

---

## Module layout

Every principle has a `bad` and a `good` package side by side. **Always open `bad` first** and sit with it until the pain is obvious. If you read `good` first, the fix looks like pointless ceremony.

```
practices_with_springboot/solid-sample-project/src/main/java/com/chheang/mengheak/solid/
├── Main.java
├── common/          ← shared model: User, UserRepository, EmailService, ReportService
├── s_srp/{bad,good}
├── o_ocp/{bad,good}
├── l_lsp/{bad,good}
├── i_isp/{bad,good}
└── d_dip/{bad,good}
```

Read `common/` first. The `bad`/`good` pairs all reference `User`, `UserRegisterRequest`, `UserRepository`, `EmailService` and `ReportService`, so none of them will make sense in isolation.

---

## D — Dependency Inversion Principle

> High-level modules should not depend on low-level modules. Both should depend on abstractions.

### The problem

`d_dip/bad/UserService.java`:

```java
public class UserService {

    private final MySqlUserRepository repository = new MySqlUserRepository();

    public User register(UserRegisterRequest request) {
        User user = new User(request.getName(), request.getEmail(), "ACTIVE");
        return repository.save(user);
    }
}
```

One line causes three separate failures:

1. **You cannot swap the database.** MySQL is welded into the business logic.
2. **You cannot unit test.** There is no seam. Testing `register()` requires a real MySQL connection.
3. **Ownership is wrong.** `UserService` now controls the *lifecycle* of a thing it should merely *use*. It decides when the repository is constructed, with what settings, and how many exist.

### The fix

`d_dip/good/UserRepository.java`:

```java
public interface UserRepository {
    User save(User user);
}
```

`d_dip/good/UserService.java`:

```java
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(UserRegisterRequest request) {
        User user = new User(request.getName(), request.getEmail(), "ACTIVE");
        return repository.save(user);
    }
}
```

Notice what the author did quietly: the `good` package ships `PostgresUserRepository`, not `MySqlUserRepository`. **The database changed and `UserService` did not change a single character.** That is the entire point of the principle, demonstrated rather than explained.

### The Spring translation

This *is* Spring. `@Service` plus constructor injection means you have been doing DIP whether or not you knew the name.

But the principle is not "use Spring." It is **which direction the dependency arrow points**:

```
❌  UserService ──────────► MySqlUserRepository      (domain depends on infrastructure)

✅  UserService ──────────► UserRepository           (domain depends on its own abstraction)
                                  ▲
                                  │
                    PostgresUserRepository           (infrastructure depends on domain)
```

The interface belongs to your **domain**. The implementation lives in **infrastructure** and depends inward. This is why the interface is named `UserRepository` (a business concept) and not `DatabaseAccessor` (a technical one).

### Practical test

Grep any service class for `new `:

```bash
grep -rn "new [A-Z]" --include=*.java src/main/java
```

Every `new SomeService()` or `new SomeRepository()` inside a class body is a DIP violation. `new User(...)` and `new PaymentCommand(...)` are fine — data objects are values, not dependencies.

### Where you will get this wrong

- Injecting a **concrete class** and calling it DI. `private final MySqlUserRepository repo;` with constructor injection is still a violation. The type must be the abstraction.
- Field injection with `@Autowired`. It works, but it hides dependencies and makes the class untestable without a Spring context. **Always use constructor injection.** A constructor with nine parameters is not a reason to switch to field injection — it is a signal that the class does too much (see SRP).
- Letting JPA or Jackson annotations into your domain objects, then claiming the domain is independent. It isn't; it depends on Hibernate.

---

## I — Interface Segregation Principle

> No client should be forced to depend on methods it does not use.

### The problem

`i_isp/bad/Worker.java`:

```java
public interface Worker {
    void work();
    void eat();
    void drive();
}
```

`i_isp/bad/Developer.java`:

```java
@Override
public void drive() {
    throw new UnsupportedOperationException("Developer does not drive for this job");
}
```

**Memorize this smell.** Any time you write `UnsupportedOperationException` to satisfy an interface, the interface is too fat. The compiler forced you to write a lie, and you complied.

The real cost is not ugliness — it is that callers can't trust the type. Anything holding a `Worker` reference must assume `drive()` might blow up, so the contract is meaningless.

### The fix

Split by capability:

```java
public interface Workable { void work(); }
public interface Eatable  { void eat(); }
public interface Drivable { void drive(); }
```

```java
public class Developer implements Workable, Eatable { ... }
public class Driver    implements Workable, Drivable { ... }
```

Nobody lies. A method that accepts `Drivable` is guaranteed something that actually drives.

### Where this bites you in Spring

- **God service interfaces.** `UserService` with 40 methods, where three controllers each use four of them. Split by use case: `UserRegistration`, `UserProfileQuery`, `UserAdministration`.
- **Leaky repositories.** Extending `JpaRepository<User, Long>` exposes ~20 methods — including `deleteAll()` — to every caller. If your domain only permits `save` and `findById`, declare a narrow interface and have your Spring Data repository sit behind it.
- **Fat client interfaces** where a REST client interface declares every endpoint of a vendor API, but your code calls two.

---

## S — Single Responsibility Principle

> A class should have one, and only one, reason to change.

### The problem

`s_srp/bad/UserService.java` — one `register()` method that validates three fields, logs, maps request to entity, saves, sends a welcome email, generates a report, and logs again.

```java
public void register(UserRegisterRequest request) {
    if (request.getName() == null || request.getName().isBlank()) {
        throw new IllegalArgumentException("Name is required");
    }
    // ... two more validation blocks ...

    System.out.println("Start registering user: " + request.getEmail());

    User user = new User(request.getName(), request.getEmail(), "ACTIVE");
    User savedUser = userRepository.save(user);

    emailService.send(savedUser.getEmail(), "Welcome", "Hello " + savedUser.getName());
    reportService.generateNewUserReport(savedUser);

    System.out.println("Finished registering user: " + savedUser.getId());
}
```

Count the reasons this class will change: new validation rule, new email copy, new report column, a schema change, a logging-format change. Five teams can all be forced to edit the same method.

### The fix

`s_srp/good/` splits it into `UserRegisterValidator`, `UserMapper`, `UserWriter`, `WelcomeEmailSender`, `NewUserReportGenerator`, and a `UserService` that only orchestrates:

```java
public void register(UserRegisterRequest request) {
    System.out.println("Start registering user: " + request.getEmail());

    validator.validate(request);
    User user = userMapper.toEntity(request);
    User savedUser = userWriter.save(user);
    welcomeEmailSender.send(savedUser);
    reportGenerator.generate(savedUser);

    System.out.println("Finished registering user: " + savedUser.getId());
}
```

The method now reads like a table of contents.

### ⚠️ What this sample gets wrong — read this part

Open `s_srp/good/UserWriter.java`:

```java
public class UserWriter {

    private final UserRepository userRepository;

    public UserWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);   // ← does nothing
    }
}
```

That class adds **zero behavior**. It is a pass-through wrapper that exists to make the diagram symmetrical. `NewUserReportGenerator` has the same problem.

**This is SRP over-applied** — the failure mode nobody warns you about. Taken too far, you get thirty one-method classes and have to open six files to follow one request. Indirection has a real cost: every hop is a file you must open and a name you must hold in your head.

The splits that genuinely earn their place here are:

- **`UserRegisterValidator`** — real logic, independently testable, will change for its own reasons.
- **`WelcomeEmailSender`** — owns subject line and body formatting; marketing will change it without touching registration.

The ones that don't:

- **`UserWriter`** — delete it, inject `UserRepository` into `UserService` directly.
- **`NewUserReportGenerator`** — delete it, inject `ReportService`.

**SRP means "one reason to change," not "one line per class."** Ask *who* would request the change. If two responsibilities always change together and are always requested by the same person, they are one responsibility.

---

## O — Open/Closed Principle

> Software entities should be open for extension, but closed for modification.

### The problem

`o_ocp/bad/DiscountCalculator.java`:

```java
public BigDecimal calculate(String membership, BigDecimal amount) {
    if ("SILVER".equals(membership))   return amount.multiply(BigDecimal.valueOf(0.05));
    if ("GOLD".equals(membership))     return amount.multiply(BigDecimal.valueOf(0.10));
    if ("PLATINUM".equals(membership)) return amount.multiply(BigDecimal.valueOf(0.15));
    return BigDecimal.ZERO;
}
```

Every new membership tier edits a class that is already tested, reviewed, and deployed. Every edit risks breaking the tiers that already worked, so every edit requires re-testing all of them.

### The fix

`o_ocp/good/DiscountPolicy.java`:

```java
public interface DiscountPolicy {
    boolean supports(String membership);
    BigDecimal calculate(BigDecimal amount);
}
```

The interface carries its own applicability test — `supports()` — so the calculator never needs to know the list of tiers.

`o_ocp/good/DiscountCalculator.java`:

```java
public class DiscountCalculator {

    private final List<DiscountPolicy> policies;

    public DiscountCalculator(List<DiscountPolicy> policies) {
        this.policies = policies;
    }

    public BigDecimal calculate(String membership, BigDecimal amount) {
        return policies.stream()
                .filter(policy -> policy.supports(membership))
                .findFirst()
                .map(policy -> policy.calculate(amount))
                .orElse(BigDecimal.ZERO);
    }
}
```

### 🔍 Spot the deliberate gap

The `good` package contains `GoldDiscountPolicy` and `SilverDiscountPolicy` — **but no Platinum.** The `bad` version had three tiers; the good version ships two.

That is your exercise. Add `PlatinumDiscountPolicy` and confirm you modified **zero** existing files. That feeling — adding behavior without editing anything — is what OCP buys you.

### The Spring translation

Annotate each policy `@Component` and Spring fills that `List<DiscountPolicy>` automatically at startup. Adding a tier becomes: create one file, deploy.

```java
@Component
public class PlatinumDiscountPolicy implements DiscountPolicy { ... }
```

This `List<T>` injection is the single most useful Spring idiom in this repository. You will see it again in **Strategy**, **Adapter**, **Factory**, and **Chain of Responsibility** — it is the same mechanism every time.

### The honest caveat

OCP is not free. Three `if` branches become four files, and following the logic now requires an IDE. Apply it where change is *likely*:

- payment providers, notification channels, export formats, discount tiers — **yes**
- a two-branch `if` that has not changed in two years — **no**

Premature OCP is how you get an interface with one implementation and a factory that returns it.

---

## L — Liskov Substitution Principle

> Subtypes must be substitutable for their base types without breaking correctness.

See `l_lsp/{bad,good}`. Read it once for the concept, then move on — it matters least in typical Spring work because composition is preferred over inheritance almost everywhere.

The practical version: **if a subclass throws on a method the parent supports, narrows what inputs it accepts, or strengthens what the caller must do first, it is not really a subtype.** The classic violation is `Square extends Rectangle` — setting width on a Square silently changes height, so code written against `Rectangle` breaks.

Note the overlap with ISP: `Developer.drive()` throwing `UnsupportedOperationException` violates *both*. That is normal — the principles are five views of one idea.

---

## How to run

```bash
cd practices_with_springboot/solid-sample-project
mvn clean package
mvn exec:java -Dexec.mainClass="com.chheang.mengheak.solid.Main"
```

The interesting work is reading, not running — the demos print lines to prove the flow executed. Read `bad` then `good`, in the order D → I → S → O → L.

---

## Exercises

1. **OCP** — Add `PlatinumDiscountPolicy` to `o_ocp/good`. Touch no existing file.
2. **SRP** — Delete `UserWriter` from `s_srp/good` and inject `UserRepository` into `UserService` directly. Decide for yourself whether the code got better. (It did.)
3. **DIP** — Write a `UserServiceTest` for `d_dip/good` using a hand-written fake `UserRepository` that records what was saved. Then try to write the same test for `d_dip/bad`. Notice that you cannot.
4. **ISP** — Find one interface in your own current project where an implementation throws `UnsupportedOperationException`. Split it.

---

## Self-check

Before moving to Tier 1's patterns, you should be able to answer without looking:

- Which way does the dependency arrow point in DIP, and which package owns the interface?
- What is the one-line smell that proves an ISP violation?
- Why is `UserWriter` in the `good` package a mistake?
- What does `supports()` buy you over a `switch` in `DiscountCalculator`?

---

## Next

→ **[STRATEGY_PATTERN.md](STRATEGY_PATTERN.md)** — OCP turned into a concrete, reusable Spring idiom.

## Related

- [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md) — OCP with a runtime lookup
- [ADAPTER_PATTERN.md](ADAPTER_PATTERN.md) — DIP applied at a vendor boundary
- [FACADE_PATTERN.md](FACADE_PATTERN.md) — SRP applied to orchestration
- [README.md](README.md) — full reading order
