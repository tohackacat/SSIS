plugins {
    application
    java
    eclipse
    id("com.gradleup.shadow") version "8.3.0"
}

application {
    mainClass = "org.example.App"
    applicationDefaultJvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
distributions.shadow {}
tasks.installShadowDist {}
tasks.startShadowScripts {}
tasks.shadowDistZip {}
tasks.shadowDistTar {}

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
    implementation("org.xerial:sqlite-jdbc:3.51.1.0")
}
