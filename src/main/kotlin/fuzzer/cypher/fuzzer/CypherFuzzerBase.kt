package fuzzer.cypher.fuzzer

import fuzzer.FuzzerBase
import fuzzer.cypher.extension.addLimit
import fuzzer.cypher.extension.direction
import fuzzer.cypher.extension.inverse
import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship
import fuzzer.cypher.statement.CypherRelationshipType
import fuzzer.cypher.statement.CypherStatementGenerator
import fuzzer.dictionary.word.randomWord
import fuzzer.extension.enumValueOf
import fuzzer.extension.randomOrElse
import org.neo4j.cypherdsl.core.*

/**
 * [fuzzer.Fuzzer] base for Cypher
 */
abstract class CypherFuzzerBase(open val settings: CypherFuzzerSettings) : FuzzerBase<String>(settings) {

    /**
     * Instance of [CypherStatementGenerator]
     */
    protected val cypherStatementGenerator by lazy { CypherStatementGenerator(random) }

    /**
     * Returns a [List] of [CypherNode]s
     */
    abstract fun getNodes(): List<CypherNode>

    private fun getNodePairs(nodes: List<CypherNode>): List<Pair<CypherNode, Node>> {
        return (0..nodes.count()).map {
            val node = nodes.random(random)
            Pair(node, cypherStatementGenerator.createNode(node))
        }
    }

    /**
     * Returns a [List] of [CypherRelationship]s
     */
    abstract fun getRelationships(vararg nodePairs: Pair<CypherNode, Node>): List<CypherRelationship>

    private fun getRelationshipPairs(
        relationships: List<CypherRelationship>,
        nodePairs: List<Pair<CypherNode, Node>>
    ): List<Pair<CypherRelationship, Relationship>> {

        fun getEmptyNode(): Node = Cypher.anyNode(String.randomWord(random))

        return relationships.map { relationship ->
            val relationshipType = random.enumValueOf<CypherRelationshipType>()

            val ingoingNode = nodePairs
                .filter { node -> node.first.relations.any { r -> r.first == relationship.label && (r.second == relationshipType.direction() || relationshipType == CypherRelationshipType.BIDIRECTIONAL) } }
                .randomOrElse(random, Pair(CypherNode("", emptyList()), getEmptyNode()))

            val outgoingNode = nodePairs
                .filter { node ->
                    node.first.relations.any { r ->
                        r.first == relationship.label && (r.second == relationshipType.direction()
                            .inverse() || relationshipType == CypherRelationshipType.BIDIRECTIONAL) && r.third == ingoingNode.first.label
                    }
                }
                .randomOrElse(random, Pair(CypherNode("", emptyList()), getEmptyNode()))

            Pair(
                relationship, cypherStatementGenerator.createRelationship(
                    relationshipType,
                    relationship,
                    ingoingNode.second,
                    outgoingNode.second,
                )
            )
        }
    }

    private fun getMatchStatement(nodes: List<Node>, relationships: List<Relationship>): List<PatternElement> =
        cypherStatementGenerator
            .generateMatchExpression(
                *nodes
                    .toTypedArray(),
                *relationships
                    .toTypedArray(),
            )

    /**
     * Returns the nodes used in MATCH clause
     */
    abstract fun getNodesInMatch(nodes: List<Pair<CypherNode, Node>>): List<Pair<CypherNode, Node>>

    /**
     * Returns the relationships used in MATCH clause
     */
    abstract fun getRelationshipsInMatch(relationships: List<Pair<CypherRelationship, Relationship>>): List<Pair<CypherRelationship, Relationship>>

    /**
     * Returns the condition of the WHERE clause
     */
    open fun getCondition(
        nodes: List<Pair<CypherNode, Node>>,
        relationships: List<Pair<CypherRelationship, Relationship>>
    ): Condition = Cypher.noCondition()

    /**
     * Returns the nodes and relationships used the RETURN clause
     */
    abstract fun getReturn(
        nodes: List<Pair<CypherNode, Node>>,
        relationships: List<Pair<CypherRelationship, Relationship>>
    ): List<PropertyContainer>

    /**
     * Generates the Cypher query
     */
    open fun generateCypherStatement(): ResultStatement {
        val nodes = getNodes()
        val nodePairs = getNodePairs(nodes)

        val relationships = getRelationships(*nodePairs.toTypedArray())
        val relationshipPairs = getRelationshipPairs(relationships, nodePairs)

        val nodesInMatch = getNodesInMatch(nodePairs)
        val relationshipsInMatch = getRelationshipsInMatch(relationshipPairs)

        val nodesReferencedInMatch = (nodesInMatch +
                relationshipsInMatch.map { nodePairs.firstOrNull { node -> node.second.symbolicName == it.second.left.symbolicName } } +
                relationshipsInMatch.map { nodePairs.firstOrNull { node -> node.second.symbolicName == it.second.right.symbolicName } })
                    .filterNotNull()
                    .distinctBy { it.first.label }

        return Cypher
            .match(getMatchStatement(nodesInMatch.map { it.second }, relationshipsInMatch.map { it.second }))
            .where(getCondition(nodesReferencedInMatch, relationshipsInMatch))
            .returning(*getReturn(nodesReferencedInMatch, relationshipsInMatch).toTypedArray())
            .addLimit(random)
            .build()
    }

    override fun fuzz(): Sequence<String> = generateSequence {
        generateCypherStatement().cypher
    }
}