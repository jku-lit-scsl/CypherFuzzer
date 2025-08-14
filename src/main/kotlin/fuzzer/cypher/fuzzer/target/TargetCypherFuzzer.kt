package fuzzer.cypher.fuzzer.target

import fuzzer.cypher.fuzzer.random.RandomCypherFuzzer
import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship
import fuzzer.extension.randomSubRange
import org.neo4j.cypherdsl.core.*

/**
 * [fuzzer.Fuzzer] that targets a certain schema
 */
class TargetCypherFuzzer(override val settings: TargetCypherFuzzerSettings) : RandomCypherFuzzer(settings) {

    override fun getNodes(): List<CypherNode> {
        return settings.cypherSchema.nodes.map { node ->
            CypherNode(
                node.label,
                node.attributes.shuffled(random).take(random.randomSubRange(settings.nodeAttributes).last),
                node.relations
            )
        }
    }

    override fun getRelationships(vararg nodePairs: Pair<CypherNode, Node>): List<CypherRelationship> {
        return settings.cypherSchema.relationships.map { rel ->
            CypherRelationship(
                rel.label,
                rel.attributes.shuffled(random).take(random.randomSubRange(settings.relationshipAttributes).last)
            )
        }
    }
}
