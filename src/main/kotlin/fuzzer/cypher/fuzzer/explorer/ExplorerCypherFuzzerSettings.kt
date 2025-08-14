package fuzzer.cypher.fuzzer.explorer

import fuzzer.cypher.fuzzer.random.RandomRangeCypherFuzzerSettings
import org.neo4j.driver.Session
import kotlin.random.Random

/**
 * [fuzzer.FuzzerSettings] for [ExplorerCypherFuzzer]
 */
class ExplorerCypherFuzzerSettings(
    random: Random,
    nodesInMatch: IntRange,
    nodeConditions: IntRange,
    returningNodes: IntRange,
    relationshipsInMatch: IntRange,
    relationshipConditions: IntRange,
    returningRelationships: IntRange,
    val session: Session
) : RandomRangeCypherFuzzerSettings(
    random,
    0..0,
    nodesInMatch,
    0..0,
    nodeConditions,
    returningNodes,
    0..0,
    relationshipsInMatch,
    0..0,
    relationshipConditions,
    returningRelationships
)