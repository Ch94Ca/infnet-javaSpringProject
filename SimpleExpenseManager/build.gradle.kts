plugins {
    id("java")
    id 'io.github.cdsap.dotenv' version '3.1.2'
}

group = "org.simpleExpenseManager"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}