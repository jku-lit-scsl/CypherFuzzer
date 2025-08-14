package fuzzer.cypher.extension

import fuzzer.cypher.schema.elements.CypherRelationshipDirection

fun CypherRelationshipDirection.inverse(): CypherRelationshipDirection {
    return if (this == CypherRelationshipDirection.IN) {
        CypherRelationshipDirection.OUT
    } else {
        CypherRelationshipDirection.IN
    }
}