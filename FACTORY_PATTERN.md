# Factory Pattern

**Local practice:** [AiModelFactory](src/factory/AiModelFactory.java) creates a fake model or vendor adapter from the keys `fake` and `vendor`. Package: `factory`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 3 — lowest priority in this repository.**
> **Module:** `practices_with_springboot/factory-pattern-class-samples` · **Packages:** `com.chheang.mengheak.factory.*`, `com.chheang.mengheak.factoryspring.*`

**The Spring container is already your factory.** That single sentence is most of what you need. This module has nine lesson packages hand-writing object creation; read three of them, actively avoid one, and spend the time you save on [Adapter](ADAPTER_PATTERN.md).

---

## Intent

"Factory" is three different patterns that share a name. Keeping them apart is half the lesson:

| Name | What it is | GoF? |
|---|---|---|
| **Simple Factory** | A class with a method that returns an object based on input | No — an idiom |
| **Factory Method** | A *subclass* decides which concrete type to create | Yes |
| **Abstract Factory** | One factory creates a *family* of related objects | Yes |

Most of the time "factory" means the first one, which is not a GoF pattern at all.

---

## The problem

`plain-java/.../lesson01_problem/BadReportService.java`:

```java
public class BadReportService {

    public void generate(String type) {
        Report report;
        if ("SALES".equalsIgnoreCase(type)) {
            report = new SalesReport();
        } else if ("STOCK".equalsIgnoreCase(type)) {
            report = new StockReport();
        } else if ("CUSTOMER".equalsIgnoreCase(type)) {
            report = new CustomerReport();
        } else {
            throw new IllegalArgumentException("Unsupported report type: " + type);
        }
        report.generate();
    }
}
```

If this looks exactly like the [Strategy](STRATEGY_PATTERN.md) "before" example, that's because it is the same shape. **The difference is what varies:** Strategy varies *behaviour*, Factory varies *which object gets constructed*. In practice they collapse into each other constantly — a factory usually returns a strategy.

Problems: [OCP](SOLID_PRINCIPLES.md) violation, `new` hardcoded into business logic (a [DIP](SOLID_PRINCIPLES.md) violation), untestable, and the service now knows every concrete report type.

---

## Evolution through the lessons

### Lesson 02 — Simple Factory

Move the `if`-chain into a dedicated `ReportFactory`. The service no longer knows concrete types. **This is progress but not a solution** — the chain still exists, it just moved.

### Lesson 04 — Map-based ⭐

```java
public class MapBasedReportFactory {

    private final Map<String, Report> reports = Map.of(
            "SALES",    new SalesReport(),
            "STOCK",    new StockReport(),
            "CUSTOMER", new CustomerReport()
    );

    public Report getReport(String type) {
        Report report = reports.get(type.toUpperCase());
        if (report == null) throw new IllegalArgumentException("Unsupported report type: " + type);
        return report;
    }
}
```

The `if`-chain is gone. Lookup is O(1).

**⚠️ But note what this actually does:** it stores *instances* and returns the **same object every time**. That is fine for stateless reports, and it is what Spring singletons do. It is a bug if `Report` carries per-request state — two concurrent requests would share one object.

### Lesson 05 — Registry of suppliers ⭐

```java
public class RegistryReportFactory {

    private final Map<String, Supplier<Report>> registry = Map.of(
            "SALES",    SalesReport::new,
            "STOCK",    StockReport::new,
            "CUSTOMER", CustomerReport::new
    );

    public Report create(String type) {
        Supplier<Report> supplier = registry.get(type.toUpperCase());
        if (supplier == null) throw new IllegalArgumentException("Unsupported report type: " + type);
        return supplier.get();
    }
}
```

**This is the best plain-Java factory in the module.** Storing `Supplier<Report>` instead of `Report` means a **fresh instance per call**, while keeping O(1) lookup and no `if`-chain. `SalesReport::new` is a constructor reference — a zero-argument factory in one token.

Use this shape whenever you need new instances and are not in a Spring context.

### ❌ Lesson 06 — Reflection: do not use this

`ReflectionReportFactory` looks up a class by name and calls `newInstance()`. It appears flexible. **Avoid it.**

| Problem | Consequence |
|---|---|
| No compile-time safety | A typo'd class name fails at runtime |
| Breaks refactoring | Rename the class; the string is not updated; the IDE cannot help |
| Breaks GraalVM native images | Reflective construction needs explicit registration metadata |
| Breaks with a module path / sealed types | Access checks fail at runtime |
| Security risk | If the type name comes from user input, you have arbitrary instantiation |
| Slow and unreadable | Reflective calls resist JIT optimisation |

The registry in lesson 05 gives you the same flexibility with none of these costs. **Read this lesson only so you can argue against it in a code review.**

### Lessons 07 & 08 — the real GoF patterns

**Factory Method** (`lesson07_factorymethod`): an abstract `DocumentApplication` declares `createDocument()`; `PdfApplication` and `WordApplication` each return their own type. The *subclass* chooses. This is [Template Method](TEMPLATE_METHOD_PATTERN.md) applied to object creation.

**Abstract Factory** (`lesson08_abstractfactory`): `UIFactory` creates a matching `Button` **and** `Input`; `WebUIFactory` returns web versions, `MobileUIFactory` mobile ones. The value is **consistency** — you cannot accidentally pair a `WebButton` with a `MobileInput`.

Read both once for vocabulary. In Spring you will almost always reach for configuration plus `@Bean` methods instead.

---

## The Spring reality

`spring-boot/.../factoryspring/payment/`:

```java
@Component("ABA")
public class AbaPaymentStrategy implements PaymentStrategy { ... }

@Component("KHQR")
public class KhqrPaymentStrategy implements PaymentStrategy { ... }
```

```java
@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public PaymentStrategy getStrategy(String method) {
        PaymentStrategy strategy = strategies.get(method.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
        return strategy;
    }
}
```

**Spring injected the map.** No registration code, no `if`-chain, no reflection.

The mechanism: injecting `Map<String, SomeInterface>` gives you every bean of that type, **keyed by bean name**. `@Component("ABA")` sets the bean name to `"ABA"`, so `strategies.get("ABA")` resolves. Without that explicit name the key would be `"abaPaymentStrategy"` and the lookup would fail.

`ExportFileFactory` in the same module uses the identical shape with `@Component("PDF")` and `@Component("JSON")`.

### The trade-off — read this before copying it

| | Bean-name map (`@Component("ABA")`) | Interface method (`type()`) |
|---|---|---|
| Lines of code | Fewer | A few more |
| Routing key lives in | An annotation argument | The interface contract |
| Survives class rename | Yes | Yes |
| Visible in the interface | **No** | **Yes** |
| Discoverable by a new developer | Must inspect annotations | Shows up in the interface |
| Works without Spring | No | Yes |

Both are used in this repository. The bean-name form appears here and in the [template method](TEMPLATE_METHOD_PATTERN.md) module; the `type()` form appears in [Strategy](STRATEGY_PATTERN.md) and [Adapter](ADAPTER_PATTERN.md).

**Prefer `type()` on the interface** — usually an enum. The routing key is then part of the contract rather than metadata, and the code is testable without a Spring context.

### Note the inconsistency

`PaymentStrategyFactory` calls `method.toUpperCase()`; `PaymentProcessorFactory` in the template-method module does **not**. Same idiom, one normalizes and one doesn't. **Pick a rule and apply it everywhere** — key normalization bugs are silent and annoying.

---

## Where factories already exist in Spring

The container *is* a factory. Literally — the core interface is called `BeanFactory`.

| Spring feature | Factory role |
|---|---|
| `@Bean` methods | An explicit factory method the container calls |
| `FactoryBean<T>` | A bean whose job is producing another bean |
| `ObjectProvider<T>` | Lazy / optional / plural retrieval — the DI-friendly factory |
| `@Conditional`, `@ConditionalOnProperty` | Chooses *which* implementation to create per environment |
| `@Scope("prototype")` | A fresh instance per lookup |
| `@Profile` | Environment-selected implementations |

**This is why the factory module is Tier 3.** Ninety percent of what these nine lessons teach, Spring gives you for free:

```java
@Bean
@ConditionalOnProperty(name = "report.engine", havingValue = "pdf")
ReportEngine pdfEngine() { return new PdfReportEngine(); }
```

That is a factory, a strategy selection and environment-based configuration in four lines.

### Need a fresh instance inside a singleton?

Don't build a registry — use `ObjectProvider`:

```java
@Service
public class ReportService {

    private final ObjectProvider<SalesReport> reportProvider;

    public void run() {
        SalesReport report = reportProvider.getObject();   // fresh if prototype-scoped
    }
}
```

This is the idiomatic answer to "I need a new one each time," and it's cleaner than `Supplier` registries or `ApplicationContext` lookups.

---

## When NOT to use a factory

- **The object is a simple value.** `new PaymentCommand(...)` needs no factory. Look at the [adapter module](ADAPTER_PATTERN.md) — plain records, constructed directly.
- **There is exactly one implementation.** A factory returning one type is pure indirection.
- **Spring can decide it.** `@Conditional` / `@Profile` / `@ConditionalOnProperty` beats a hand-rolled factory.
- **The creation logic is complex and multi-step** — that's a [Builder](BUILDER_PATTERN.md), not a factory.
- **Ever, with reflection, based on user input.** See lesson 06.

---

## File map

```
practices_with_springboot/factory-pattern-class-samples/
├── plain-java/.../factory/
│   ├── common/                  ← Report, SalesReport, StockReport, CustomerReport
│   ├── lesson01_problem/        ← the if-chain — skim
│   ├── lesson02_simple/         ← simple factory — skim
│   ├── lesson03_strategy/       ← factory returning a strategy — the overlap made explicit
│   ├── lesson04_map/            ← ⭐ READ — map of instances
│   ├── lesson05_registry/       ← ⭐ READ — map of Supplier<T>, the best plain-Java version
│   ├── lesson06_reflection/     ← ❌ READ ONLY TO REJECT
│   ├── lesson07_factorymethod/  ← real GoF Factory Method — skim once
│   ├── lesson08_abstractfactory/← real GoF Abstract Factory — skim once
│   └── lesson09_javafactory/    ← JDK examples — skim
└── spring-boot/.../factoryspring/
    ├── payment/  ← ⭐ READ — PaymentStrategyFactory, @Component("ABA")
    └── export/   ← same idiom for PDF/JSON export
```

**Read four things: `lesson04_map`, `lesson05_registry`, `factoryspring/payment`, and `lesson06_reflection` (only to understand why it's wrong).** Everything else is optional.

---

## How to run

```bash
cd practices_with_springboot/factory-pattern-class-samples/plain-java && mvn clean package
```

```bash
mvn exec:java -Dexec.mainClass="com.chheang.mengheak.factory.lesson05_registry.RegistryFactoryDemo"
```

Other demos: `lesson01_problem.ProblemDemo`, `lesson02_simple.SimpleFactoryDemo`, `lesson03_strategy.StrategyFactoryDemo`, `lesson04_map.MapFactoryDemo`, `lesson07_factorymethod.FactoryMethodDemo`, `lesson08_abstractfactory.AbstractFactoryDemo`.

Spring Boot module (Java 17, Spring Boot 3.5.0 — it's a `spring-boot-starter` app, not a web app, so it runs and exits):

```bash
cd practices_with_springboot/factory-pattern-class-samples/spring-boot && mvn spring-boot:run
```

---

## Exercises

1. **Instance vs. supplier.** Give `SalesReport` a mutable field. Call `MapBasedReportFactory.getReport("SALES")` twice and mutate the first result. Observe that the second call sees the change. Then do the same with `RegistryReportFactory` and observe that it doesn't.
2. **Break reflection.** In `lesson06_reflection`, rename `SalesReport` using your IDE's refactor tool. Watch the string not update and the factory fail at runtime. That is the argument against it, felt rather than read.
3. **Add a provider.** Add `WingPaymentStrategy` with `@Component("WING")` to the Spring module. Confirm `PaymentStrategyFactory` needs no change.
4. **Remove the bean name.** Delete the `("ABA")` from `@Component`. Watch the lookup fail. Now you understand exactly what bean-name-keyed map injection depends on.
5. **Replace the factory with Spring.** Rewrite the export factory using `@ConditionalOnProperty` so the format is chosen by configuration rather than a runtime string. Decide which fits your real use case.

---

## Self-check

- What are the three distinct patterns called "factory," and which one is not GoF?
- Why is `Map<String, Supplier<Report>>` better than `Map<String, Report>`?
- Give three concrete reasons not to use a reflection-based factory.
- What exactly does `@Component("ABA")` change about `Map<String, PaymentStrategy>` injection?
- When should you use `ObjectProvider<T>` instead of writing a factory?

---

## You've finished

All nine documents are done. Go back to **[README.md](README.md)** for the consolidated cheat sheet and the suggested project work.

## Related

- [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md) — factories usually return strategies; `lesson03` shows both at once
- [ADAPTER_PATTERN.md](ADAPTER_PATTERN.md) — `PaymentGatewayRegistry` is the best factory in this repo
- [BUILDER_PATTERN.md](BUILDER_PATTERN.md) — for complex multi-step construction
- [SOLID_PRINCIPLES.md](SOLID_PRINCIPLES.md) — factories exist to serve OCP and DIP
