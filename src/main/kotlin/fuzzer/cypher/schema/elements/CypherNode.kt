package fuzzer.cypher.schema.elements

import kotlin.reflect.KClass

/**
 * Represents a node in a graph database
 */
data class CypherNode(
    override val label: String,
    override val attributes: List<Pair<String, KClass<*>>>,
    /**
     * Relationships for the node identified by
     * 1) the relationship's label
     * 2) the direction
     * 3) the other node's label
     */
    val relations: MutableList<Triple<String, CypherRelationshipDirection, String>> = mutableListOf()) :
    CypherElement(label, attributes)