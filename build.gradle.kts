plugins {
    application
    java
    eclipse
}

application {
    mainClass = "org.example.App"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}
