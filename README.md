# Production-Grade Selenium Automation Framework

## Tech Stack
| Tool | Purpose |
|---|---|
| **Selenium 4.x** | Browser automation |
| **TestNG 7.x** | Test orchestration |
| **Cucumber 7.x** | BDD (feature files, step definitions) |
| **ExtentReports 5.x** | HTML reports with screenshots |
| **Apache POI 5.x** | Excel data provider |
| **Log4j2** | Test execution logging |
| **WebDriverManager** | Automatic browser driver management |
| **Maven** | Build & dependency management |
| **Jenkins** | CI/CD |

---

## Project Structure
```
selenium-automation-framework/
├── config/
│   └── config.properties          # Environment config
├── src/
│   ├── main/java/com/framework/
│   │   ├── annotations/           # Custom annotations
│   │   ├── base/                  # BasePage (all Selenium interactions), BaseTest
│   │   ├── config/                # ConfigReader (singleton)
│   │   ├── constants/             # Constants, timeouts, paths
│   │   ├── dataprovider/          # ExcelDataProvider (TestNG @DataProvider)
│   │   ├── enums/                 # Browser, WaitStrategy
│   │   ├── factory/               # DriverFactory (Chrome/Firefox/Edge/Safari)
│   │   ├── listeners/             # TestListener, ExtentReportListener
│   │   ├── manager/               # DriverManager (ThreadLocal for parallel)
│   │   ├── pages/                 # Page Objects (PageFactory)
│   │   ├── reports/               # ExtentReport, ExtentLogger
│   │   ├── retry/                 # RetryAnalyzer, RetryAnnotationTransformer
│   │   └── utils/                 # JavaScriptUtils, ScreenshotUtils, ExcelUtils, DatabaseUtils
│   └── test/java/com/framework/
│       ├── hooks/                 # Cucumber @Before/@After hooks
│       ├── runners/               # CucumberRunner, ParallelCucumberRunner
│       ├── stepdefinitions/       # Cucumber step definition classes
│       └── tests/                 # TestNG test classes
├── src/test/resources/
│   ├── features/                  # Cucumber .feature files
│   ├── extent.properties          # ExtentReports Cucumber adapter config
│   └── cucumber.properties        # Cucumber publish config
├── testdata/
│   └── testdata.xlsx              # Excel test data (create manually)
├── testng.xml                     # Sequential execution suite
├── testng-parallel.xml            # Parallel execution suite
├── testng-smoke.xml               # Smoke suite
├── Jenkinsfile                    # CI/CD pipeline
└── pom.xml                        # Central execution point
```

---

## Running Tests

### From Maven (Central Execution Point)
```bash
# Full regression suite (default)
mvn clean test

# Smoke suite only
mvn clean test -Psmoke

# Parallel execution
mvn clean test -Pparallel

# Custom parameters
mvn clean test -Dbrowser=firefox -Denv=staging -Dheadless=true

# Run specific suite
mvn clean test -Dsuite=testng-smoke.xml
```

### From Jenkins
1. Create a **Pipeline** job
2. Set `Definition = Pipeline script from SCM`
3. Point to this repository
4. The `Jenkinsfile` exposes **BROWSER**, **ENVIRONMENT**, **SUITE**, **HEADLESS** parameters
5. Build with Parameters

---

## Key Concepts Implemented

| Concept | Location |
|---|---|
| Page Object Model | `src/main/java/.../pages/` |
| PageFactory | `BasePage` constructor |
| Base Class | `BasePage`, `BaseTest` |
| OOP (Inheritance, Encapsulation, Abstraction, Polymorphism) | `BasePage` → Page hierarchy |
| BDD Feature Files | `src/test/resources/features/` |
| Step Definitions | `src/test/java/.../stepdefinitions/` |
| Hooks (Before/After) | `com.framework.hooks.Hooks` |
| Cucumber Runner | `CucumberRunner`, `ParallelCucumberRunner` |
| TestNG Suite XML | `testng.xml`, `testng-parallel.xml`, `testng-smoke.xml` |
| Parallel Execution | `testng-parallel.xml` + `ThreadLocal<WebDriver>` |
| Retry Analyzer | `RetryAnalyzer`, `RetryAnnotationTransformer` |
| Data Provider | `ExcelDataProvider` + Apache POI |
| TestNG Listener | `TestListener`, `ExtentReportListener` |
| Explicit/Fluent Waits | `WaitUtils` |
| Implicit Wait | `BaseTest.setUp()` / `Hooks.beforeScenario()` |
| Dropdown Handling | `BasePage.selectByVisibleText/Value/Index()` |
| Window Handling | `BasePage.switchToWindowByTitle/Index()` |
| Frame Handling | `BasePage.switchToFrame/DefaultContent()` |
| Alert Handling | `BasePage.acceptAlert/dismissAlert()` |
| Drag & Drop | `BasePage.dragAndDrop()` |
| JavaScript Executor | `JavaScriptUtils` + `BasePage.executeScript()` |
| Screenshot on Failure | `ScreenshotUtils` + `TestListener` + `Hooks` |
| ExtentReports | `ExtentReport`, `ExtentLogger` |
| Log4j2 Logging | `log4j2.xml` + `LogManager.getLogger()` |
| Excel Utility | `ExcelUtils` |
| Database Utility | `DatabaseUtils` |
| JSON Utility | `JsonUtils` |
| Config Management | `ConfigReader` (singleton, env-aware) |
| CI/CD | `Jenkinsfile` (parameterized pipeline) |

---

## Reports
- **Extent Report**: `reports/ExtentReport.html`
- **Cucumber HTML**: `reports/cucumber-report.html`
- **TestNG Surefire**: `target/surefire-reports/`
- **Screenshots**: `screenshots/` (attached to Extent Report)
- **Logs**: `logs/framework.log`, `logs/test-execution.log`, `logs/errors.log`
