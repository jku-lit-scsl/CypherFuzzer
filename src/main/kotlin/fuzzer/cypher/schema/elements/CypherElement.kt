package fuzzer.cypher.schema.elements

import kotlin.reflect.KClass

/**
 * Base for entities in a graph database: [CypherNode], [CypherRelationship]
 * Each holds a [label] and [attributes]
 */
abstract class CypherElement(open val label: String, open val attributes: List<Pair<String, KClass<*>>>)
