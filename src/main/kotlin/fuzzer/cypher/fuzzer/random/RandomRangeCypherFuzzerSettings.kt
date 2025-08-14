package fuzzer.cypher.fuzzer.random

import fuzzer.cypher.fuzzer.CypherFuzzerSettings
import kotlin.random.Random

/**
 * [fuzzer.FuzzerSettings] for a [RandomCypherFuzzer]
 */
open class RandomRangeCypherFuzzerSettings(
    random: Random,
    val nodes: IntRange,
    val nodesInMatch: IntRange,
    val nodeAttributes: IntRange,
    val nodeConditions: IntRange,
    val returningNodes: IntRange,
    val relationships: IntRange,
    val relationshipsInMatch: IntRange,
    val relationshipAttributes: IntRange,
    val relationshipConditions: IntRange,
    val returningRelationships: IntRange,
) : CypherFuzzerSettings(random) {
    init {
        require(!(nodesInMatch.isEmpty() && relationshipsInMatch.isEmpty())) {
            "Either numOfNodes or numOfRelationships must be not empty!"
        }

        require(returningRelationships.last <= relationshipsInMatch.last) {
            "ReturningRelationships must be greater than relationships!"
        }

        require(!(nodesInMatch.isEmpty()&& !nodeAttributes.isEmpty())) {
            "Node attributes require nodes!"
        }
        require(!(relationshipsInMatch.isEmpty() && !relationshipAttributes.isEmpty())) {
            "Relationship attributes require relationships!"
        }

        require(!(!nodeConditions.isEmpty() && (nodeAttributes.isEmpty() || nodesInMatch.isEmpty()))) {
            "Node conditions require nodes with relationships!"
        }
        require(!(!relationshipConditions.isEmpty() && (relationshipAttributes.isEmpty() || relationshipsInMatch.isEmpty()))) {
            "Relationship conditions require relationships with attributes!"
        }
    }
}