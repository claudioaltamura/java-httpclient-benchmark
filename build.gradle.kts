plugins {
    java
    eclipse
    idea
    application
    id("com.diffplug.gradle.spotless") version "3.24.1"
    id("com.github.ben-manes.versions") version "0.22.0"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("org.apache.httpcomponents:httpasyncclient:4.1.4")
    implementation("org.apache.commons:commons-lang3:3.9")

    implementation("io.vertx:vertx-core:3.8.1")
    implementation("io.vertx:vertx-web-client:3.8.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

spotless {
    java {
        googleJavaFormat()
    }
}

application {
    mainClassName = "de.claudioaltamura.java.httpclient.benchmark.Benchmark"
}

