package fuzzer.cypher.schema

import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship

/**
 * Represents the database schema, consisting of [CypherNode]s and [CypherRelationship]s
 */
data class CypherSchema(
    val nodes: List<CypherNode> = emptyList(),
    val relationships: List<CypherRelationship> = emptyList()
)