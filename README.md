# Windesheim codeparser
Library for the design pattern detection and analysis in java code.

[![Build Status](https://travis-ci.org/Taronyuu/WindesheimTechnologieProject.svg?branch=master)](https://travis-ci.org/Taronyuu/WindesheimTechnologieProject)

## Installation
Since this library is not available on any code repositories we recommend to use Jitpack to include this library as a dependency in your projects.
### Maven
For Maven you have to add Jitpack to your list of repositories if you haven't done so already.
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Once you have the Jitpack repository you can add this Github project as a dependency like so.
```xml
<dependency>
    <groupId>com.github.Taronyuu</groupId>
    <artifactId>WindesheimTechnologieProject</artifactId>
    <version>master</version>
</dependency>
```
As version use 'master' for the latest stable release. Use 'develop' for testing unreleased features or fixes.
Or use a version tag of a release for a fixed version of the library, this is recommended for production software.

### Gradle
For Gradle you have to add Jitpack as a maven repository this can be done by adding the following in your root build.gradle.
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Once you have the Jitpack repository you can add this Github project as a dependency like so.
```
dependencies {
    implementation 'com.github.Taronyuu:WindesheimTechnologieProject:master'
}
```
As version use 'master' for the latest stable release. Use 'develop' for testing unreleased features or fixes.
Or use a version tag of a release for a fixed version of the library, this is recommended for production software.

## Usage
This library provides a FileAnalysisProvider class which can be used to quickly analyze one or multiple files.
The FileAnalysisProvider has a static function which creates a pre-configured instance which will analyze files for all known design patterns this library can detect.
It is also possible to use your own configuration.

This is a example for using the pre-configured FileAnalysisProvider
```java
class Main{
    public static main(String[] argv) {
        String path = "/path/to/some/file.java";
        File file = new File(path);
        
        FileAnalysisProvider analysis = FileAnalysisProvider
                .getConfiguredFileAnalysisProvider();
        
        ArrayList<IDesignPattern> patterns = new ArrayList<>();
        
        try{
            patterns = analysis.analyzeFile(file.toURL());
        } catch (IOException ex) {
            System.out.println("Something went wrong: "
                    + ex.getMessage());
        }
        
        //... do something with the found patterns
    }
}
```