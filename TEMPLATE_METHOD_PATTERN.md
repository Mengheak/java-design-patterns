# Template Method Pattern

**Local practice:** [AnswerWorkflow](src/template_method/AnswerWorkflow.java) fixes the sequence in `final answer()`, requires `preparePrompt()`, and offers a `formatAnswer()` hook. Package: `template_method`.

Run the local exercises through [src/Main.java](src/Main.java); see [README run instructions](README.md#running-the-code). The detailed lessons below describe the reference samples under `practices_with_springboot/`, using the `com.chheang.mengheak` package prefix. Run each sample command sequence from the repository root in Bash.

> **Tier 2** · **Module:** `practices_with_springboot/template-method-pattern-class-samples` · **Packages:** `com.chheang.mengheak.template.*`

**Low value for writing, high value for reading.** You will not create many template hierarchies. You will read Spring's every day — `JdbcTemplate`, `RestTemplate`, `TransactionTemplate`, `AbstractAuthenticationProcessingFilter`. The name "Template" in those classes is literal, not decorative.

---

## Intent

> Define the skeleton of an algorithm in a method, deferring some steps to subclasses. Template Method lets subclasses redefine certain steps without changing the algorithm's structure.

One fixed sequence. A few pluggable steps. **The parent owns the order; the child owns the details.**

---

## The problem

`plain-java/.../lesson01_problem/` — three exporters, each with the same six steps:

```java
public class CsvReportExporter {
    public void export() {
        System.out.println("Validate request");
        System.out.println("Load report data");
        System.out.println("Format data as CSV");     // ← differs
        System.out.println("Generate CSV file");      // ← differs
        System.out.println("Save file");
        System.out.println("Return result");
    }
}
```

`PdfReportExporter` and `ExcelReportExporter` are the same file with two lines changed.

**Four of six steps are duplicated three times.** In real code those steps are not print statements — validation is thirty lines, loading hits a database, saving writes to S3 with retry. Now:

- a bug in `saveFile` must be fixed in three places, and you will miss one
- a new step (audit logging) must be added to three places, in the right position
- a fourth format means copy-pasting the whole class again
- nothing enforces that the six steps stay in the same order across exporters

That last point is the subtle one. If `PdfReportExporter` saves before formatting, nothing catches it.

---

## The solution

`plain-java/.../lesson02_template/ReportExporter.java`:

```java
public abstract class ReportExporter {

    public final void export() {          // ← final: the algorithm
        validateRequest();
        loadData();
        formatData();
        generateFile();
        saveFile();
        returnResult();
    }

    private void validateRequest() { System.out.println("Validate request"); }

    private void loadData()        { System.out.println("Load report data"); }

    protected abstract void formatData();      // ← child must supply

    protected abstract void generateFile();    // ← child must supply

    private void saveFile()        { System.out.println("Save file"); }

    private void returnResult()    { System.out.println("Return result"); }
}
```

```java
public class CsvReportExporter extends ReportExporter {

    @Override
    protected void formatData()   { System.out.println("Format data as Csv"); }

    @Override
    protected void generateFile() { System.out.println("Generate Csv file"); }
}
```

Adding JSON export is now one class with two short methods.

### The three access modifiers are the entire design

This is what to take away. **Nothing else in the pattern matters as much:**

| Modifier | Meaning | Used for |
|---|---|---|
| `public final` | The algorithm. Subclasses **cannot** reorder or skip steps. | `export()` |
| `private` | Fixed steps. Subclasses cannot even see them, let alone override. | `validateRequest`, `loadData`, `saveFile` |
| `protected abstract` | Required variation. Subclasses **must** supply. | `formatData`, `generateFile` |
| `protected` (with empty body) | Optional variation — a **hook**. | see below |

**`final` on the template method is not optional.** Drop it and a subclass can override `export()` entirely, discard the sequence, and the pattern provides nothing. Every step you make `protected` is a promise you can never take back.

---

## Hooks — optional steps

`plain-java/.../lesson03_hook/ReportExporter.java`:

```java
public abstract class ReportExporter {

    public final void export() {
        validate();
        loadData();
        format();
        compress();      // ← hook
        save();
    }

    protected abstract void format();

    protected void compress() {
        // empty by default — subclasses may override
    }
}
```

```java
public class CompressedPdfExporter extends ReportExporter {

    @Override
    protected void format()   { System.out.println("Format PDF"); }

    @Override
    protected void compress() { System.out.println("Compress PDF"); }
}
```

A **hook** is a step with a default no-op (or a sensible default) that subclasses may override but need not. `PdfExporter` ignores it; `CompressedPdfExporter` uses it.

You have overridden hooks before without knowing the term: `WebSecurityConfigurerAdapter.configure(...)`, `OncePerRequestFilter.shouldNotFilter(...)`, and every `@Override` on a Spring `Abstract*` class.

**Design rule:** `abstract` for steps every subclass must think about; hook for steps most subclasses ignore. Making everything a hook means a subclass can silently do nothing; making everything abstract means boilerplate empty overrides.

---

## The Spring version

`spring-boot/.../template/PaymentProcessingTemplate.java`:

```java
public abstract class PaymentProcessingTemplate {

    public final PaymentResponse process(PaymentRequest request) {
        validate(request);
        fraudCheck(request);
        PaymentTransaction transaction = chargePayment(request);
        sendReceipt(transaction);
        sendSms(transaction);
        saveTransaction(transaction);
        return toResponse(transaction);
    }

    private void validate(PaymentRequest request) { ... }          // fixed

    protected abstract void fraudCheck(PaymentRequest request);    // required

    protected PaymentTransaction createSuccessTransaction(PaymentRequest request) {
        return PaymentTransaction.builder()
                .id(UUID.randomUUID())
                .method(request.getMethod())
                .accountNo(request.getAccountNo())
                .amount(request.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();                                          // ← helper, not a step
    }

    protected abstract PaymentTransaction chargePayment(PaymentRequest request);   // required

    private void sendReceipt(PaymentTransaction transaction) { ... }               // fixed

    protected void sendSms(PaymentTransaction transaction) { }                     // hook

    private void saveTransaction(PaymentTransaction transaction) { ... }           // fixed

    private PaymentResponse toResponse(PaymentTransaction transaction) { ... }     // fixed
}
```

Note `createSuccessTransaction` — `protected` but **not a step in the template**. It's a helper the subclasses call from inside `chargePayment`. That's a legitimate fourth category: shared utility offered to children.

A subclass is tiny:

```java
@Component("ABA")
public class AbaPaymentProcessor extends PaymentProcessingTemplate {

    protected void fraudCheck(PaymentRequest request) {
        System.out.println("ABA fraud check");
    }

    protected PaymentTransaction chargePayment(PaymentRequest request) {
        System.out.println("Charge by ABA: " + request.getAmount());
        return createSuccessTransaction(request);
    }
}
```

### Template Method meets Strategy

`spring-boot/.../service/PaymentProcessorFactory.java`:

```java
@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {

    private final Map<String, PaymentProcessingTemplate> processors;

    public PaymentProcessingTemplate getProcessor(String method) {
        PaymentProcessingTemplate processor = processors.get(method);
        if (processor == null)
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        return processor;
    }
}
```

**This is `Map<String, T>` injection keyed by bean name** — the variant mentioned in [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md). It works here precisely because the components are named explicitly: `@Component("ABA")`, `@Component("KHQR")`, `@Component("ACLEDA")`.

Two things to notice:

1. **The patterns compose.** Template Method structures each processor internally; Strategy/Factory selects between them at runtime. This is normal — patterns are not mutually exclusive.
2. **The lookup is case-sensitive.** `processors.get(method)` with no `toUpperCase()`, so `"aba"` returns `null` and throws. The [factory module](FACTORY_PATTERN.md) does `method.toUpperCase()` for the same lookup. Small inconsistency, real bug class — normalize your keys.

---

## Template Method vs. Strategy

`plain-java/.../lesson04_strategy_vs_template/` exists for this comparison, and it is the most useful lesson in the module. **Knowing which to pick is a real decision you will face.**

| | Template Method | [Strategy](STRATEGY_PATTERN.md) |
|---|---|---|
| **Mechanism** | Inheritance (`extends`) | Composition (holds a field) |
| **Bound** | Compile time | Runtime |
| **Varies** | A few steps *inside* a fixed sequence | The *whole* algorithm |
| **Reuse** | Shared code lives in the parent | Each strategy is independent |
| **Swappable at runtime** | No | Yes |
| **Testable in isolation** | Awkward — needs a subclass | Easy — it's just an object |
| **Multiple variations** | No — Java has single inheritance | Yes, compose freely |

**The decision rule:**

> If the implementations share **most** of their logic and differ in a **few** steps → **Template Method**.
> If the implementations are **independent** and share **nothing** → **Strategy**.

If email and SMS notification are 90% identical with one differing step, Strategy forces you to duplicate the 90%. If CSV and PDF export share nothing but a name, Template Method gives you an empty parent.

**When in doubt, prefer Strategy.** Composition is more flexible, easier to test, and doesn't consume your one inheritance slot. Template Method's cost is permanent: a subclass can never extend anything else.

---

## Where Template Method already exists

This is why you read the chapter:

| Spring / JDK class | Fixed steps | Your step |
|---|---|---|
| `JdbcTemplate.query(...)` | get connection, create statement, execute, **iterate**, close, translate exceptions | `RowMapper.mapRow` |
| `TransactionTemplate.execute(...)` | begin, **run**, commit or roll back | `TransactionCallback.doInTransaction` |
| `RestTemplate.execute(...)` | open, write, **read**, close | `ResponseExtractor` |
| `OncePerRequestFilter` | dedupe-per-request guard, **filter**, continue | `doFilterInternal` |
| `AbstractAuthenticationProcessingFilter` | match URL, **authenticate**, success/failure handling | `attemptAuthentication` |
| `HttpServlet` | dispatch by method | `doGet`, `doPost` |
| `AbstractList` (JDK) | everything | `get`, `size` |

Every one of those `*Template` classes is this pattern, named after it. `JdbcTemplate` handles connection management, resource cleanup and exception translation — all the steps you'd get wrong — and asks you for one thing: how to turn a row into an object.

---

## When NOT to use it

- **The steps aren't genuinely shared.** An abstract parent with one concrete step and five abstract ones is not a template, it's an interface wearing a costume.
- **You need runtime swapping.** Inheritance binds at compile time. Use Strategy.
- **The subclass needs to extend something else.** Java gives you one superclass. Spending it on a template is a real cost.
- **More than two levels deep.** `A extends B extends C extends D` — nobody can tell what actually runs. Cap it at one level.
- **Callbacks are cleaner.** Modern Java often expresses this better with a functional parameter than a subclass:
  ```java
  public Report export(Formatter formatter) { ... }   // instead of an abstract subclass
  ```
  `JdbcTemplate` does exactly this with `RowMapper` — a template method whose variable step is supplied as a lambda, not a subclass. **This is the modern form of the pattern, and usually the better one.**

---

## File map

```
practices_with_springboot/template-method-pattern-class-samples/
├── README.md
├── plain-java/
│   └── .../template/
│       ├── lesson01_problem/          ← 3 duplicated exporters
│       ├── lesson02_template/         ← ⭐ the pattern
│       ├── lesson03_hook/             ← ⭐ hooks
│       ├── lesson04_strategy_vs_template/  ← ⭐⭐ the most useful lesson
│       ├── lesson05_java_examples/    ← ⭐ where it lives in the JDK/Spring
│       ├── homework/                  ← payment template — skip, the Spring module is better
│       └── interview/                 ← talking points
└── spring-boot/
    └── .../template/
        ├── template/   PaymentProcessingTemplate + Aba/Acleda/Khqr processors
        ├── service/    PaymentProcessorFactory, PaymentService
        ├── controller/ PaymentController, ApiExceptionHandler
        ├── domain/     PaymentTransaction
        └── dto/        PaymentRequest, PaymentResponse
```

**Read:** `lesson04_strategy_vs_template` and `lesson05_java_examples` first — they carry the value. Then `lesson02` and `lesson03` for the mechanics, then the Spring module.

**Skip:** `lesson01` (you already know duplication is bad), `homework`, `interview`.

---

## How to run

The plain-java module uses `exec-maven-plugin`, so pick a demo class:

```bash
cd practices_with_springboot/template-method-pattern-class-samples/plain-java && mvn clean package
```

```bash
mvn exec:java -Dexec.mainClass="com.chheang.mengheak.template.lesson04_strategy_vs_template.StrategyVsTemplateDemo"
```

Other main classes: `lesson01_problem.DuplicateWorkflowDemo`, `lesson02_template.TemplateMethodDemo`, `lesson03_hook.HookMethodDemo`, `lesson05_java_examples.JavaTemplateExamplesDemo`.

Spring Boot:

```bash
cd practices_with_springboot/template-method-pattern-class-samples/spring-boot && mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/payments -H "Content-Type: application/json" -d "{\"method\":\"ABA\",\"accountNo\":\"001\",\"amount\":50.00}"
```

Postman files: `practices_with_springboot/template-method-pattern-class-samples/postman/`.

---

## Exercises

1. **Add a format.** Add `XmlReportExporter` to `lesson02_template`. Two methods, no duplication.
2. **Break the contract.** Remove `final` from `export()`, then write a subclass that overrides it and skips `saveFile()`. Understand what `final` was protecting.
3. **Add a hook.** Add a `watermark()` hook to the exporter template, used only by PDF.
4. **Fix the case bug.** Make `PaymentProcessorFactory` case-insensitive. Decide whether to normalize at lookup or at registration, and why.
5. **Convert to Strategy.** Rewrite `PaymentProcessingTemplate` as a Strategy with a shared helper class. Compare the two. Notice what you gained (testability, no inheritance) and lost (the enforced step order).

---

## Self-check

- Why must the template method be `final`?
- What is the difference between an `abstract` step and a hook, and when do you choose each?
- Give the one-sentence rule for Template Method vs. Strategy.
- What does `JdbcTemplate` fix, and which step does it delegate to you?
- Why is a `RowMapper` lambda usually better than an abstract subclass?

---

## Next

→ **[BUILDER_PATTERN.md](BUILDER_PATTERN.md)**

## Related

- [STRATEGY_PATTERN.md](STRATEGY_PATTERN.md) — the alternative; read the comparison table above
- [FACTORY_PATTERN.md](FACTORY_PATTERN.md) — `PaymentProcessorFactory` is a factory selecting templates
- [README.md](README.md) — full reading order
