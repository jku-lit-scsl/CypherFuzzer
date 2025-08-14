package fuzzer.cypher.fuzzer.random

import kotlin.random.Random

/**
 * [fuzzer.FuzzerSettings] for a [RandomCypherFuzzer]
 */
open class RandomCypherFuzzerSettings(
    random: Random,
    nodes: Int,
    nodesInMatch: Int,
    nodeAttributes: Int,
    nodeConditions: Int,
    returningNodes: Int,
    relationships: Int,
    relationshipsInMatch: Int,
    relationshipAttributes: Int,
    relationshipConditions: Int,
    returningRelationships: Int
) : RandomRangeCypherFuzzerSettings(
    random = random,
    nodes = IntRange(nodes, nodes),
    nodesInMatch = IntRange(nodesInMatch, nodesInMatch),
    nodeAttributes = IntRange(nodeAttributes, nodeAttributes),
    nodeConditions = IntRange(nodeConditions, nodeConditions),
    returningNodes = IntRange(returningNodes, returningNodes),
    relationships = IntRange(relationships, relationships),
    relationshipsInMatch = IntRange(relationshipsInMatch, relationshipsInMatch),
    relationshipAttributes = IntRange(relationshipAttributes, relationshipAttributes),
    relationshipConditions = IntRange(relationshipConditions, relationshipConditions),
    returningRelationships = IntRange(returningRelationships, returningRelationships),
    )