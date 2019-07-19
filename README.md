# Sentry Log4J2 + Java Agent Demo

This project contains an example Sentry integration with a Spring Boot application.
It uses the default `log4j` logging framework to send log level `WARN` and above to sentry via the `log4j` Sentry integration/SDK, and the agent will enhance your application stack traces on Sentry by adding the names and values of local variables to each frame.

## Installation and Configuration

1. Add the Sentry Log4j2 package to the [pom.xml](https://github.com/sentry-demos/spring-boot-log4j2/blob/master/pom.xml#L27-L31) file.
2. Specify DSN in [sentry.properties](https://github.com/sentry-demos/java-spring-boot-log4j/blob/master/sentry.properties#L8) 
3. `SENTRY_AUTH_TOKEN` must be set as ENV var (for consumption by `sentry-cli` in Makefile)
4. Add the logger configuration file ([log4j2.xml](https://github.com/sentry-demos/spring-boot-log4j2/blob/master/src/main/resources/log4j2.xml))

The example application can be started as follows:
```
    make
```

Now, visit `http://localhost:8080/handled` in your browser and an error with full context (including stack locals / variasble values) should be sent to Sentry.

# Documentaion:
- Log4j SDK -> https://docs.sentry.io/clients/java/modules/log4j2/
- Java Agent -> https://docs.sentry.io/clients/java/agent/

# GIF
![Alt Text](java-spring-boot-log4j-agent-small.gif)
