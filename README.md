# Java Design Patterns — Self-Teaching

A self-teaching workspace with plain Java AI assistant exercises in `src/` and nine sample project folders covering SOLID principles and eight design patterns in `practices_with_springboot/`.

Start with [src/Main.java](src/Main.java) to see the local exercises run together. The pattern guides also explain the Maven samples under `practices_with_springboot/`. The sample sources use the `com.chheang.mengheak` package prefix; the local exercises use packages such as `adapter`, `strategy`, and `dependency_injection`.

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

# When to use each — a decision guide

The tier order above tells you what to **learn** first. This section tells you what to **reach for** while building.

## 1. Start from the symptom, not the pattern

You should never start with "which pattern should I use here?" You start with something concrete you are about to write, or something already in the file that hurts. Find the row that matches:

| What you're writing / what you see | Reach for | Guide |
|---|---|---|
| An `if`/`switch` over a type code, **3+ branches**, that will keep growing | **Strategy** | [→](STRATEGY_PATTERN.md) |
| A third-party SDK type appearing in your service or controller signature | **Adapter** | [→](ADAPTER_PATTERN.md) |
| A vendor API whose money/status/naming conventions don't match your domain | **Adapter** | [→](ADAPTER_PATTERN.md) |
| A controller with **4+ injected dependencies** | **Facade** | [→](FACADE_PATTERN.md) |
| A service method calling **5+ collaborators** in a fixed sequence | **Facade** | [→](FACADE_PATTERN.md) |
| A flow that charges a card *and* writes to the DB, and step 6 might fail | **Facade + compensation** | [→](FACADE_PATTERN.md#the-advanced-part-compensation) |
| A sequence of checks where any one can reject the request | **Chain of Responsibility** | [→](CHAIN_OF_RESPONSIBILITY_PATTERN.md) |
| Cross-cutting request concerns: request IDs, auth, maintenance mode | **Chain** (a servlet `Filter`) | [→](CHAIN_OF_RESPONSIBILITY_PATTERN.md) |
| Two+ classes with the **same step sequence**, differing in 1–2 steps | **Template Method** | [→](TEMPLATE_METHOD_PATTERN.md) |
| A constructor with **5+ parameters**, or several optional ones | **Builder** | [→](BUILDER_PATTERN.md) |
| An object that must never exist in an invalid state | **Builder** with validating `build()` | [→](BUILDER_PATTERN.md) |
| The same retry / logging / metrics wrapper needed around several implementations | **Decorator** — or AOP first | [→](DECORATOR_PATTERN.md) |
| Needing a **fresh instance** per call inside a singleton | `ObjectProvider<T>` | [→](FACTORY_PATTERN.md) |
| Choosing an implementation per environment or config flag | `@ConditionalOnProperty` / `@Profile` — **not** a factory | [→](FACTORY_PATTERN.md) |
| `new ConcreteThing()` inside a service class | **Inject it** (DIP) | [→](SOLID_PRINCIPLES.md#d--dependency-inversion-principle) |
| A method throwing `UnsupportedOperationException` to satisfy an interface | **Split the interface** (ISP) | [→](SOLID_PRINCIPLES.md#i--interface-segregation-principle) |
| One class that validates *and* saves *and* emails *and* reports | **Split it** (SRP) | [→](SOLID_PRINCIPLES.md#s--single-responsibility-principle) |

## 2. Where each one lives in a Spring Boot app

Building a feature outside-in, this is roughly where each pattern belongs:

```
┌─ Controller / API ─────────────────────────────────────────────┐
│  Facade      keep the controller to: map → call once → map     │
│  Chain       servlet Filters / HandlerInterceptor              │
│  Builder     assembling response DTOs (or just use records)    │
└────────────────────────────────────────────────────────────────┘
┌─ Application / Service ────────────────────────────────────────┐
│  Facade      orchestration, transactions, compensation         │
│  Strategy    branching on a business type                      │
│  Chain       validation pipelines                              │
│  Template    a shared workflow skeleton across variants        │
└────────────────────────────────────────────────────────────────┘
┌─ Integration / Infrastructure  ← the boundary ─────────────────┐
│  Adapter     ALWAYS, for every third-party SDK or API          │
│  Decorator   retry / metrics / logging around the adapter      │
│  Factory     the registry that selects among adapters          │
└────────────────────────────────────────────────────────────────┘
┌─ Domain ───────────────────────────────────────────────────────┐
│  Builder     immutable value objects with invariants           │
│  Strategy    pluggable business rules (pricing, discounts)     │
│  (mostly)    no patterns — plain records and methods           │
└────────────────────────────────────────────────────────────────┘
```

**The density is deliberate.** Patterns cluster at the **boundary** (where other people's code meets yours) and at **orchestration** (where many things must happen in order). Your domain layer should be the *least* patterned part of the app — if it isn't, the patterns are probably compensating for a weak model.

## 3. When in the project to introduce them

| Phase | Adopt | Leave alone |
|---|---|---|
| **First version of a feature** | Constructor injection (DIP), one responsibility per class (SRP) — these are free and cost nothing to keep | Everything else. Write the straightforward thing. |
| **Second variant arrives** | Adapter — **immediately**, if a third party is involved | Strategy — wait for the third branch |
| **Third variant arrives** | Strategy, Template Method, Factory registry | — |
| **Feature grows** | Facade (controller bloat), Chain (validation grows), Decorator (repeated wrapping) | — |
| **Going to production** | Compensation in the facade, retry/timeout at the boundary | — |

### The rule of three

> Don't extract a pattern on the **first** occurrence. Extract on the **third**.

Two implementations of an interface is usually a coincidence. Three is a pattern. Extracting too early means you abstract along the wrong axis — and a wrong abstraction is harder to remove than duplication.

### The exceptions — adopt these on day one

Three things should never wait for a third occurrence, because the cost of retrofitting them is far higher than the cost of having them:

1. **Adapter at any third-party boundary.** The moment a vendor type crosses into your service layer, the cost of removing it later grows with every file that touches it. One class, written on day one, is cheap insurance.
2. **Constructor injection against interfaces (DIP).** Retrofitting testability into a codebase full of `new` is a multi-week project. Doing it from the start is free.
3. **Validation in the constructor or `build()`.** An object that can exist in an invalid state spreads null checks through every consumer. Guard it at the single point of construction.

## 4. Patterns that arrive together

Real features rarely use one pattern. A typical payment integration — which is exactly what the adapter and decorator modules in this repo demonstrate — converges on this stack:

```
Checkout flow
  └── Facade                  orchestrates + compensates on failure
        └── Factory/Registry  selects the provider           ← List<T> injection
              └── Decorator   retry → metrics → logging
                    └── Adapter   Stripe / ABA / Wing
                          └── vendor SDK
```

Each layer does one thing and none of them knows about the others. Recognising that this is **one design**, not six patterns bolted together, is the point at which the material has landed.

Other common pairings:

- **Strategy + Template Method** — a registry selects the processor; each processor is a template. See [`template-method-pattern-class-samples/spring-boot`](TEMPLATE_METHOD_PATTERN.md#template-method-meets-strategy).
- **Facade + Chain** — the facade orchestrates; the first step is a validation chain.
- **Adapter + Decorator** — the adapter normalises the vendor; decorators add resilience.

## 5. When the answer is "no pattern"

This is the section most pattern material omits, and it is the one that keeps codebases readable.

| Situation | Do this instead |
|---|---|
| Two branches that haven't changed in two years | Leave the `if` |
| An interface with exactly one implementation | Delete the interface |
| "Variants" that differ only by a rate, label or threshold | A `Map` or a config property — not classes |
| The implementation is fixed per deployment | `@Profile` / `@ConditionalOnProperty`, one bean |
| Field-level validation (`@NotBlank`, `@Email`, ranges) | Bean Validation — not a chain |
| Transactions, caching, async, retry on **your own** beans | `@Transactional`, `@Cacheable`, `@Async`, `@Retryable` — not hand-written decorators |
| A value object with 3–4 required fields | A `record` — not a builder |
| You can't name the axis of variation | Stop. You don't have a pattern yet, you have duplication. Wait. |

**The real failure mode of pattern courses is over-application.** Every pattern here trades simplicity for flexibility you may never need. A codebase with an `AbstractNotificationStrategyFactoryProvider` and one implementation is worse — harder to read, harder to change — than the if-chain it replaced.

If you can't articulate *what is likely to change*, the pattern is premature.

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
self-teaching/
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
├── SPRING_AI.md                          ← further study; no matching runnable module
├── src/                                  ← local plain Java AI assistant exercises
│   ├── Main.java                          ← entry point
│   ├── dependency_injection/              AiModel, AnswerService, FakeAiModel
│   ├── adapter/                          VendorAiAdapter, VendorAiClient
│   ├── strategy/                         concise and teaching prompts
│   ├── decorator/                        logging and timing wrappers
│   ├── factory/                          AiModelFactory
│   ├── observer/                         answer logging, counting, length
│   ├── chain_of_responsibility/          question validation
│   ├── builder/                          AiRequest
│   ├── facade/                           AiAssistantFacade
│   └── template_method/                  answer workflows
└── practices_with_springboot/             ← independent reference projects below
```

Inside `practices_with_springboot/`:

```text
practices_with_springboot/
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

The local `src/` exercises use the JDK directly. The reference modules are **independent Maven builds**. There is no root POM or multi-module aggregator — `cd` into a module before running Maven.

---

## Running the code

Run the local AI assistant exercises from this repository root in PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$sources = Get-ChildItem src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac -d out $sources
java -cp out Main
```

The fake model and vendor client return demonstration strings; these exercises do not require an API key or a running model. In IntelliJ IDEA, run `Main` with `src/` marked as the source root.

For the reference samples, start each command sequence below from the repository root. These examples use Bash syntax.

**Plain Java reference modules** can package to an executable jar:

```bash
cd practices_with_springboot/adapter-pattern-class-samples/adapter-pattern-plain-java && mvn clean package && java -jar target/adapter-pattern-plain-java-1.0.0.jar
```

Most plain-Java modules have a single `*CourseDemo` entry point with all but one demo commented out — **uncomment the one you want before packaging.**

**Builder, factory, template-method and solid** use `exec-maven-plugin` instead, so name the class:

```bash
cd practices_with_springboot/factory-pattern-class-samples/plain-java
mvn exec:java -Dexec.mainClass="com.chheang.mengheak.factory.lesson05_registry.RegistryFactoryDemo"
```

**Spring Boot modules:**

```bash
cd practices_with_springboot/strategy-pattern-class-samples/strategy-pattern-springboot && mvn spring-boot:run
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
