plugins {
    id("java")
    id("info.solidsoft.pitest") version "1.15.0"
}

pitest {
    junit5PluginVersion = "1.2.1"
    targetClasses = setOf("domain.*")
    outputFormats = setOf("HTML")
    mutationThreshold = 100
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
}