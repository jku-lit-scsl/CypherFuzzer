package fuzzer.cypher.statement

/**
 * Represents condition types used in the Cypher query language, such as in the WHERE clause
 */
enum class CypherConditionType {
    /**
     * logical AND
     */
    AND,

    /**
     * logical OR
     */
    OR,

    /**
     * logical XOR
     */
    XOR
}