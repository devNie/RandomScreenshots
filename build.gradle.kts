

plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.14.3")
}

application {
    mainClass.set("com.github.devnie.randomscreenshots.Main")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

