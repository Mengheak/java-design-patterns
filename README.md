# Design Patterns for Spring Boot Developers

A study guide over nine standalone Maven projects demonstrating SOLID principles and seven design patterns in plain Java and Spring Boot.

**Each pattern has its own guide in this folder.** Read them in tier order — that ordering is the most important thing on this page.

---

## How to use this repository

### 📖 Read in tier order. Do not skip ahead.

The tiers are not difficulty levels. They are **payoff levels for a Spring Boot developer**, and each tier depends on the one before it.

> **Tier 1 must be read before Tier 2. Tier 2 must be read before Tier 3.**

This matters because the patterns build on each other mechanically, not just conceptually:

- **SOLID** teaches dependency inversion → without it, every other document is cargo-culting.
- **Strategy** introduces `List<T>` injection → **four later patterns reuse that exact mechanism.** If you meet it first in the Factory document it looks like a trick; met in Strategy it looks like a principle.
- **Adapter** builds directly on Strategy's registry and adds the `@Bean` configuration idiom.
- **Facade** assumes you already understand SRP from the SOLID document.
- **Tier 2** patterns are constantly compared against Tier 1 ones ("use Template Method instead of Strategy when…"). Those comparisons are meaningless if you haven't read the Tier 1 side.
- **Tier 3** patterns are largely *replaced* by Spring features. You can only judge that trade-off once you know what the pattern costs.

---

## 🥇 Tier 1 — read properly, do the exercises

These four change how you write Spring code. Budget a few evenings.

| # | Guide | Module | Why it matters |
|---|---|---|---|
| 1 | **[SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md)** | `solid-sample-project` | The foundation. Read D → I → S → O. Every pattern below is a consequence of these. |
| 2 | **[STRATEGY_PATTERN.md](STRATEGY_PATTERN.md)** | `strategy-pattern-class-samples` | Highest payoff per line. Kills the if-chain forever. Teaches `List<T>` injection. |
| 3 | **[ADAPTER_PATTERN.md](ADAPTER_PATTERN.md)** | `adapter-pattern-class-samples` | Anti-corruption layers. The pattern that saves real money when a vendor changes. |
| 4 | **[FACADE_PATTERN.md](FACADE_PATTERN.md)** | `facade-pattern-class-samples` | Answers "how thick should my `@Service` be?" Plus compensation/saga basics. |

**Before moving to Tier 2, do at least one exercise from each.** Reading a pattern is not the same as having used it.

---

## 🥈 Tier 2 — read for recognition, skim the repetition

These appear constantly in Spring's own source. You'll read them more often than you'll write them.

| # | Guide | Module | Why it matters |
|---|---|---|---|
| 5 | **[CHAIN_OF_RESPONSIBILITY_PATTERN.md](CHAIN_OF_RESPONSIBILITY_PATTERN.md)** | `chain-of-responsibility-pattern-class-samples` | Servlet filters, `HandlerInterceptor`, and the entire Spring Security filter chain are this. |
| 6 | **[TEMPLATE_METHOD_PATTERN.md](TEMPLATE_METHOD_PATTERN.md)** | `template-method-pattern-class-samples` | `JdbcTemplate`, `RestTemplate`, `TransactionTemplate` — the name is literal. |
| 7 | **[BUILDER_PATTERN.md](BUILDER_PATTERN.md)** | `builder-pattern-class-samples` | You'll type `@Builder`. Learn the one thing Lombok doesn't give you: validation at `build()`. |

---

## 🥉 Tier 3 — know they exist, let Spring do the work

Spring largely replaces these. Read them so you can recognise what the framework is doing for you.

| # | Guide | Module | Why it's low priority |
|---|---|---|---|
| 8 | **[DECORATOR_PATTERN.md](DECORATOR_PATTERN.md)** | `decorator-pattern-class-samples` | Spring AOP already generates decorators: `@Transactional`, `@Cacheable`, `@Async`. |
| 9 | **[FACTORY_PATTERN.md](FACTORY_PATTERN.md)** | `factory-pattern-class-samples` | The container *is* your factory. `@Bean` + `@Conditional` + `ObjectProvider` cover it. |

---

## Two rules that apply to every guide

**1. Always open the `bad` / `before` package first.** Every module ships the painful version next to the fixed one. Sit with the bad version until the pain is obvious. If you read `good` first, the pattern looks like pointless ceremony — and you'll go on to apply it where it isn't needed.

**2. Patterns are a cost, not a virtue.** Every one trades simplicity for flexibility. The skill worth building is knowing when a plain `if` with two branches is the correct answer and an interface with two implementations is over-engineering. Each guide has a **"When NOT to use it"** section. Those sections are the most valuable part.

---

## The one mechanism to take away

If you remember nothing else from this repository, remember this constructor:

```java
public SomeService(List<SomeInterface> implementations) {
    this.byKey = /* index them by a key the interface itself exposes */;
}
```

Spring finds every bean implementing the interface and injects them as a list. You index it once, at startup. The if-chain becomes a `Map.get()`, and adding a case becomes adding a file.

**Five of the nine guides use this exact mechanism.** It is introduced in [Strategy](STRATEGY_PATTERN.md) and refined in [Adapter](ADAPTER_PATTERN.md#3-the-registry--strategy-again-done-better).

---

## Quick reference — telling them apart

Most confusion between these patterns comes from the fact that several share a mechanism. The intent is what differs:

| Pattern | One-line discriminator |
|---|---|
| **Strategy** | Many implementations **you wrote**, picked at runtime |
| **Adapter** | Many APIs **someone else wrote**, translated to your interface |
| **Facade** | One call replacing a **sequence** across many collaborators |
| **Decorator** | Wraps one object, **same interface**, adds behaviour |
| **Chain of Responsibility** | A **pipeline** where any link may stop the flow |
| **Template Method** | Fixed **sequence**, subclass fills in a few steps |
| **Factory** | Decides **which object to create** |
| **Builder** | Decides **how to construct** one complex object safely |

The subtlest pair: **Strategy and Adapter look nearly identical in code** — both are `List<T>` injection into a registry. The difference is intent. *Strategy chooses between behaviors you wrote; Adapter hides APIs you didn't.*

The second subtlest: **Decorator vs. Adapter vs. Facade.** Ask *what happened to the interface?*
Unchanged → Decorator. Converted → Adapter. Replaced with something simpler → Facade.

---

## Repository layout

```
design-pattern-main/
├── README.md                              ← you are here
├── SOLID_PRINCIPLES.md                    ← Tier 1
├── STRATEGY_PATTERN.md                    ← Tier 1
├── ADAPTER_PATTERN.md                     ← Tier 1
├── FACADE_PATTERN.md                      ← Tier 1
├── CHAIN_OF_RESPONSIBILITY_PATTERN.md     ← Tier 2
├── TEMPLATE_METHOD_PATTERN.md             ← Tier 2
├── BUILDER_PATTERN.md                     ← Tier 2
├── DECORATOR_PATTERN.md                   ← Tier 3
├── FACTORY_PATTERN.md                     ← Tier 3
│
├── solid-sample-project/                  bad/good pairs for all five principles
├── strategy-pattern-class-samples/        plain-java + spring-boot
├── adapter-pattern-class-samples/         plain-java + spring-boot + postman
├── facade-pattern-class-samples/          plain-java + spring-boot + webflux + postman
├── chain-of-responsibility-pattern-class-samples/
│                                          without-cor + plain-java + spring-boot + postman
├── template-method-pattern-class-samples/ plain-java + spring-boot + postman
├── builder-pattern-class-samples/         plain-java + spring-boot
├── decorator-pattern-class-samples/       plain-java + spring-boot
└── factory-pattern-class-samples/         plain-java + spring-boot
```

Each project is an **independent Maven build**. There is no parent POM and no multi-module aggregator — `cd` into a module before running anything.

---

## Running the code

**Plain Java modules** package to an executable jar:

```bash
cd adapter-pattern-class-samples/adapter-pattern-plain-java && mvn clean package && java -jar target/adapter-pattern-plain-java-1.0.0.jar
```

Most plain-Java modules have a single `*CourseDemo` entry point with all but one demo commented out — **uncomment the one you want before packaging.**

**Builder, factory, template-method and solid** use `exec-maven-plugin` instead, so name the class:

```bash
mvn exec:java -Dexec.mainClass="com.pisethjava.factory.lesson05_registry.RegistryFactoryDemo"
```

**Spring Boot modules:**

```bash
cd strategy-pattern-class-samples/strategy-pattern-springboot && mvn spring-boot:run
```

Postman collections and environments live in the `postman/` folder of the adapter, chain, facade and template-method modules.

---

## Known quirks

Worth knowing before they confuse you:

- **Spring Boot versions differ across modules** — 3.3.5, 3.5.0, 3.5.3, 4.0.0 and 4.1.0 are all present, on Java 17 or 21 depending on the module. Copy the *ideas*, not the POM config. Check the module's own `pom.xml` before assuming.
- **`target/` is committed in several modules** (builder, decorator, facade-webflux). Search results will look duplicated — ignore anything under `target/classes`.
- **`strategy-pattern-plan-java`** is a typo for "plain" in the repo's own folder name. Left as-is so paths still match.
- **`facade-pattern-plain-java/pom.xml`** is minified onto a few lines, unlike every other POM.
- **Test coverage is thin** — only the adapter, decorator, facade and strategy modules have any tests at all.
- **Several modules teach the same lesson twice** (a `payment` version and a `storage`/`room` homework version). Each guide's *File map* section says which to skip.

---

## Suggested project work

Once you've finished Tier 1, the fastest way to make it stick is to apply it to a real service:

1. Find the longest if-chain in your own codebase. Convert it to [Strategy](STRATEGY_PATTERN.md) with `List<T>` injection.
2. Find any place a third-party SDK type appears in a controller or service signature. Put an [Adapter](ADAPTER_PATTERN.md) in front of it.
3. Find a controller with more than three dependencies. Extract a [Facade](FACADE_PATTERN.md).
4. Find one multi-step flow that calls an external API and then writes to the database. Ask what happens if step three fails. Add compensation.

That fourth one will probably find a real bug.
