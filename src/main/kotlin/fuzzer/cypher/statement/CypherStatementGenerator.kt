package fuzzer.cypher.statement

import fuzzer.extension.random
import fuzzer.cypher.extension.randomConditionFunction
import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship
import fuzzer.dictionary.word.WordDictionary
import fuzzer.dictionary.word.randomWord
import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Cypher
import org.neo4j.cypherdsl.core.Node
import org.neo4j.cypherdsl.core.PatternElement
import org.neo4j.cypherdsl.core.PropertyContainer
import org.neo4j.cypherdsl.core.Relationship
import kotlin.random.Random
import kotlin.reflect.KClass

class CypherStatementGenerator(val random: Random) {

    private val usedNames: HashSet<String> by lazy { HashSet() }

    private val wordDictionary: WordDictionary by lazy { WordDictionary() }

    private fun uniqueName(): String {
        var word: String
        do {
            word = wordDictionary.get(random)
        } while (!usedNames.add(word))
        return word
    }

    fun createNode(node: CypherNode): Node = Cypher
            .node(node.label)
            .named(uniqueName())

    fun createRelationship(relationshipType: CypherRelationshipType, relationship: CypherRelationship, a: Node, b: Node): Relationship =
        when (relationshipType) {
            CypherRelationshipType.TO -> ::createRelationshipTo
            CypherRelationshipType.FROM -> ::createRelationshipFrom
            CypherRelationshipType.BIDIRECTIONAL -> ::createBidirectionalRelationship
        }.invoke(relationship, a, b)

    private fun createRelationshipTo(relationship: CypherRelationship, a: Node, b: Node): Relationship = a
        .relationshipTo(b, relationship.label)
        .named(uniqueName())

    private fun createRelationshipFrom(relationship: CypherRelationship, a: Node, b: Node): Relationship = a
        .relationshipFrom(b, relationship.label)
        .named(uniqueName())

    private fun createBidirectionalRelationship(relationship: CypherRelationship, a: Node, b: Node): Relationship = a
        .relationshipBetween(b, relationship.label)
        .named(uniqueName())

    fun generateMatchExpression(vararg elements: PatternElement): List<PatternElement> {
        return listOf(*elements)
    }

    fun chainCondition(condition: CypherConditionType, condA: Condition, condB: Condition): Condition = when (condition)
    {
        CypherConditionType.AND -> condA.and(condB)
        CypherConditionType.OR -> condA.or(condB)
        CypherConditionType.XOR -> condA.xor(condB)
    }

    fun generateCondition(element: PropertyContainer, attribute: Pair<String, KClass<*>>): Condition {
        when (attribute.second) {
            Boolean::class -> return Boolean
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Boolean>(Boolean.random(random)))
            Byte::class -> return Byte
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Byte>(Byte.random(random)))
            Short::class -> return Short
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Short>(Short.random(random)))
            Int::class -> return Int
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Int>(Int.random(random)))
            Long::class -> return Long
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Long>(Long.random(random)))
            Float::class -> return Float
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Float>(Float.random(random)))
            Double::class -> return Double
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Double>(Double.random(random)))
            Char::class -> return Char
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<Char>(Char.random(random)))
            String::class -> return String
                .randomConditionFunction(random)
                .invoke(element.property(attribute.first), Cypher.literalOf<String>(String.randomWord(random)))
            else -> return Cypher.noCondition()
        }
    }
}