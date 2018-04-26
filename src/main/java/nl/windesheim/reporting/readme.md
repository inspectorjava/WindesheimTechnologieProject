# Supported patterns
- Singleton


## Adding a new pattern

1. Add a new ENUM to the DesignPatternType
2. Create a new builder in the builders package
3. Extend the AbstractFoundPatternBuilder and its methods

## Create a report

```java
// Create Report builder
CodeReportBuilder codeReportBuilder = Report.create();

// Add singleton
codeReportBuilder.addFoundPatternBuilder(new SingletonFoundPatternBuilder("file"));

// create the report class
CodeReport codeReport = codeReportBuilder.buildReport();

// get the textual report
String report = codeReport.getReport();
```