package fuzzer

import fuzzer.cypher.fuzzer.explorer.ExplorerCypherFuzzer
import fuzzer.cypher.fuzzer.explorer.ExplorerCypherFuzzerSettings
import fuzzer.cypher.fuzzer.random.RandomCypherFuzzer
import fuzzer.cypher.fuzzer.random.RandomCypherFuzzerSettings
import fuzzer.cypher.fuzzer.random.RandomRangeCypherFuzzerSettings
import fuzzer.cypher.fuzzer.target.TargetCypherFuzzer
import fuzzer.cypher.fuzzer.target.TargetCypherFuzzerSettings
import fuzzer.cypher.schema.CypherSchema
import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship
import fuzzer.cypher.schema.elements.CypherRelationshipDirection
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import kotlin.random.Random


fun main() {
    /**
     * uncomment the Cypher you want to use
     */
    // executeRandomFuzzer()
    // executeRandomRangeCypherFuzzer()
    // executeTargetCypherFuzzer()
    // executeExplorerCypherFuzzer()
}

fun executeExplorerCypherFuzzer() {
    /**
     * Note: This is for demonstration purposes only
     */

    // adjust
    val uri = "bolt://localhost:7687" // add uri
    val user = "neo4j" // add user
    val password = "12345678" // add password

    val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))

    val explorerCypherFuzzerSettings = ExplorerCypherFuzzerSettings(
        random = Random(123),
        nodesInMatch = 1..1,
        nodeConditions = 1..1,
        returningNodes = 1..1,
        relationshipsInMatch = 1..1,
        relationshipConditions = 0..0,
        returningRelationships = 1..1,
        session = driver.session(),
    )
    val explorerCypherFuzzer = ExplorerCypherFuzzer(
        explorerCypherFuzzerSettings
    )

    explorerCypherFuzzer
        .fuzz()
        .take(10)
        .forEach {
            println(it)
        }
    driver.close()
}

fun executeRandomFuzzer() {
    val randomCypherSettings = RandomCypherFuzzerSettings(
        random = Random(123),
        nodes = 5,
        nodesInMatch = 2,
        nodeAttributes = 7,
        nodeConditions = 3,
        returningNodes = 2,
        relationships = 4,
        relationshipsInMatch = 2,
        relationshipAttributes = 4,
        relationshipConditions = 3,
        returningRelationships = 2
    )
    RandomCypherFuzzer(randomCypherSettings)
        .fuzz()
        .take(10)
        .forEach { println(it) }
}

fun executeRandomRangeCypherFuzzer() {
    val randomCypherSettings = RandomRangeCypherFuzzerSettings(
        random = Random(123),
        nodes = 2..2,
        nodesInMatch = 0..0,
        nodeAttributes = 2..2,
        nodeConditions = 0..0,
        returningNodes = 0..0,
        relationships = 2..2,
        relationshipsInMatch = 1..1,
        relationshipAttributes = 2..2,
        relationshipConditions = 1..1,
        returningRelationships = 1..1
    )
    RandomCypherFuzzer(randomCypherSettings)
        .fuzz()
        .take(10)
        .forEach { println(it) }
}

fun executeTargetCypherFuzzer() {
    val nodes = listOf<CypherNode>(
        CypherNode(
            "Person",
            attributes = listOf(Pair("Age", Int::class), Pair("Name", String::class)),
            relations = mutableListOf(Triple("lives",  CypherRelationshipDirection.OUT, "Address")),
        ),
        CypherNode(
            "Address",
            attributes = listOf(Pair("City", String::class), Pair("ZipCode", String::class), Pair("State", String::class)),
            relations = mutableListOf(Triple("lives", CypherRelationshipDirection.IN, "Person")),
        )
    )
    val relationships = listOf<CypherRelationship>(
        CypherRelationship(
            "lives",
            attributes = listOf(Pair("since", Int::class)),
        )
    )

    val schema = CypherSchema(
        nodes = nodes,
        relationships = relationships,
    )

    val randomCypherSettings = TargetCypherFuzzerSettings(
        random = Random(123),
        nodesInMatch = 1,
        nodeAttributes = 2,
        nodeConditions = 0,
        returningNodes = 1,
        relationshipsInMatch = 1,
        relationshipAttributes = 0,
        relationshipConditions = 0,
        returningRelationships = 0,
        schema
    )
    TargetCypherFuzzer(randomCypherSettings)
        .fuzz()
        .take(10)
        .forEach { println(it) }
}