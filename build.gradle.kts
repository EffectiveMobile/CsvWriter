plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.writer"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        withSourcesJar()
        withJavadocJar()
    }

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")


    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "csv"
            pom {
                name.set("csv")
                description.set("CSV utilities")
            }
        }
    }
    repositories {
        mavenLocal()
    }
}
