package fuzzer.cypher.fuzzer.target

import fuzzer.cypher.fuzzer.random.RandomCypherFuzzerSettings
import fuzzer.cypher.schema.CypherSchema
import kotlin.random.Random

/**
 * [fuzzer.cypher.fuzzer.CypherFuzzerSettings] for [TargetCypherFuzzer]
 */
class TargetCypherFuzzerSettings(
    random: Random,
    nodesInMatch: Int,
    nodeAttributes: Int,
    nodeConditions: Int,
    returningNodes: Int,
    relationshipsInMatch: Int,
    relationshipAttributes: Int,
    relationshipConditions: Int,
    returningRelationships: Int,
    val cypherSchema: CypherSchema
) : RandomCypherFuzzerSettings(random,
    cypherSchema.nodes.count(),
    nodesInMatch,
    nodeAttributes,
    nodeConditions,
    returningNodes,
    cypherSchema.relationships.count(),
    relationshipsInMatch,
    relationshipAttributes,
    relationshipConditions,
    returningRelationships)