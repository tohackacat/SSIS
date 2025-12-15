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


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jline:jline:3.30.0")
    implementation("org.jline:jline-terminal-jansi:3.30.6")
    implementation("org.jline:jline-terminal-jni:3.30.6")
    implementation("net.java.dev.jna:jna:5.18.1")
}
