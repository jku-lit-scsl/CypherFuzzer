package fuzzer.cypher.statement

/**
 * Represents the types of Cypher relationships
 */
enum class CypherRelationshipType {

    /**
     * an ingoing relationship from the perspective of node 'a':
     * (a: node) <- \[rel] - (b: node)
     */
    FROM,
    /**
     * an outgoing relationship from the perspective of node 'a':
     * (a: node) - \[rel] -> (b: node)
     */
    TO,

    /**
     * a relationship with no specified direction (thus reachable from both nodes):
     * (a: node) - \[rel] - (b: node)
     */
    BIDIRECTIONAL,
}