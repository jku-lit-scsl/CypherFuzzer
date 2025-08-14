plugins {
    kotlin("jvm") version "2.1.0"
}

group = "philipp.com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.neo4j:neo4j-cypher-dsl:2024.4.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.neo4j:neo4j-jdbc:6.1.5")
    implementation("org.neo4j.driver:neo4j-java-driver:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}
