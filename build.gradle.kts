plugins {
    id("java")
}

group = "me.vkorostelev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.rometools:rome:2.1.0")
    implementation("com.rometools:rome-utils:2.1.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
