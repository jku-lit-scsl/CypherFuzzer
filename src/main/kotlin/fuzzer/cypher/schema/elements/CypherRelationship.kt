package fuzzer.cypher.schema.elements

import kotlin.reflect.KClass

/**
 * Represents a relationship in a graph database
 */
data class CypherRelationship(override val label: String, override val attributes: List<Pair<String, KClass<*>>>) :
    CypherElement(label, attributes)