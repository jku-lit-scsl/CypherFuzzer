package fuzzer.cypher.fuzzer.random

import fuzzer.cypher.extension.inverse
import fuzzer.cypher.fuzzer.CypherFuzzerBase
import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship
import fuzzer.cypher.schema.elements.CypherRelationshipDirection
import fuzzer.cypher.statement.CypherConditionType
import fuzzer.dictionary.word.randomWord
import fuzzer.extension.enumValueOf
import fuzzer.extension.randomOrElse
import fuzzer.extension.randomSubRange
import org.neo4j.cypherdsl.core.*

/**
 * [fuzzer.Fuzzer] that fuzzes on a random schema
 */
open class RandomCypherFuzzer(override val settings: RandomRangeCypherFuzzerSettings) : CypherFuzzerBase(settings) {

    private lateinit var labelQueue: ArrayDeque<String>

    private fun setupLabelQueue() {

        val nodeCount = settings.nodes.last
        val nodeAttrCount = settings.nodeAttributes.last

        val relationshipCount = settings.relationships.last
        val relationshipAttrCount = settings.relationshipAttributes.last

        val maximumNumOfLabelsRequired =
            nodeCount + // number of nodes that require a label
            (nodeCount * nodeAttrCount) + // number of nodes, where each node requires attribute labels
            relationshipCount + // number of relationships that require a label
            (relationshipCount * relationshipAttrCount) // number of relationships, where each relationship requires attribute labels

        labelQueue = ArrayDeque(
            mutableSetOf<String>().apply {
                while (size < maximumNumOfLabelsRequired) {
                    add(String.randomWord(random))
                }
            }
        )
    }

    override fun getNodes(): List<CypherNode> {
        return random.randomSubRange(settings.nodes).map {
            CypherNode(
                labelQueue.removeFirst(),
                random.randomSubRange(settings.nodeAttributes).map { Pair(labelQueue.removeFirst(), types.random(random)) }
            )
        }
    }

    override fun getRelationships(vararg nodePairs: Pair<CypherNode, Node>): List<CypherRelationship> {
        val relationships = random.randomSubRange(settings.relationships).map {
            CypherRelationship(
                labelQueue.removeFirst(),
                random.randomSubRange(settings.relationshipAttributes).map { Pair(labelQueue.removeFirst(), types.random(random)) }
            )
        }

        relationships.forEach {
            val direction = random.enumValueOf<CypherRelationshipDirection>()
            val nodes = random.randomSubRange(0..nodePairs.size).map { nodePairs.random(random) }
            val receivingNodes = random.randomSubRange(0..nodePairs.size).map { nodePairs.random(random) }
            for (nodeA in nodes) {
                nodeA.first.relations.add(Triple(it.label, direction, ""))
                for (nodeB in receivingNodes) {
                    nodeA.first.relations.add(Triple(it.label, direction, nodeB.first.label))
                    nodeB.first.relations.add(Triple(it.label, direction.inverse(), nodeA.first.label))
                }
            }
        }

        return relationships
    }

    override fun getNodesInMatch(nodes: List<Pair<CypherNode, Node>>): List<Pair<CypherNode, Node>> {
        return nodes.shuffled(random).take(random.randomSubRange(settings.nodesInMatch).last)
    }

    override fun getRelationshipsInMatch(relationships: List<Pair<CypherRelationship, Relationship>>): List<Pair<CypherRelationship, Relationship>> {
        return relationships.shuffled(random).take(random.randomSubRange(settings.relationshipsInMatch).last)
    }

    override fun getCondition(
        nodes: List<Pair<CypherNode, Node>>,
        relationships: List<Pair<CypherRelationship, Relationship>>
    ): Condition {
        val nodeConditions = random.randomSubRange(settings.nodeConditions)
            .map {
                val nodesShuffled = nodes.shuffled(random)
                cypherStatementGenerator.generateCondition(
                    nodesShuffled[0].second,
                    nodesShuffled[0].first.attributes.randomOrElse(random, Pair("", Nothing::class))
                )}
        val relationshipConditions = random.randomSubRange(settings.relationshipConditions)
            .map {
                val relationshipsShuffled = relationships.shuffled(random)
                cypherStatementGenerator.generateCondition(
                    relationshipsShuffled[0].second,
                    relationshipsShuffled[0].first.attributes.randomOrElse(random, Pair("", Nothing::class))
                ) }

        return (nodeConditions + relationshipConditions + Cypher.noCondition())
            .reduce { acc, condition ->
                cypherStatementGenerator.chainCondition(random.enumValueOf<CypherConditionType>(), acc, condition)
            }
    }

    override fun getReturn(
        nodes: List<Pair<CypherNode, Node>>,
        relationships: List<Pair<CypherRelationship, Relationship>>
    ): List<PropertyContainer> {
        var nodesToTake = random.randomSubRange(settings.returningNodes).last
        var relationshipsToTake = random.randomSubRange(settings.returningRelationships).last

        if (nodes.isEmpty() && relationshipsToTake == 0)
            relationshipsToTake++
        if (relationships.isEmpty() && nodesToTake == 0)
            nodesToTake++

        return (
                nodes.shuffled(random).take(nodesToTake).map { it.second } +
                        relationships.shuffled(random).take(relationshipsToTake).map { it.second }
                )
    }

    override fun generateCypherStatement(): ResultStatement {
        setupLabelQueue()
        return super.generateCypherStatement()
    }
}