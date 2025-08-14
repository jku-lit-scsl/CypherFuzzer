package fuzzer.cypher.extension

import fuzzer.cypher.schema.elements.CypherRelationshipDirection
import fuzzer.cypher.statement.CypherRelationshipType

fun CypherRelationshipType.direction(): CypherRelationshipDirection {
    return if (this == CypherRelationshipType.TO) {
        CypherRelationshipDirection.OUT
    } else {
        CypherRelationshipDirection.IN
    }
}