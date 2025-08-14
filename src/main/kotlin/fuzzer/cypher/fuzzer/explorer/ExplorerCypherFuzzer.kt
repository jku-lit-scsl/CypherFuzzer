package fuzzer.cypher.fuzzer.explorer

import fuzzer.cypher.fuzzer.random.RandomCypherFuzzer
import fuzzer.cypher.schema.CypherSchema
import fuzzer.cypher.schema.elements.CypherNode
import fuzzer.cypher.schema.elements.CypherRelationship
import fuzzer.cypher.schema.elements.CypherRelationshipDirection
import org.neo4j.cypherdsl.core.Node
import kotlin.reflect.KClass

/**
 * [fuzzer.Fuzzer] that extracts metadata from a database and generates fuzz based on it
 */
class ExplorerCypherFuzzer(override val settings: ExplorerCypherFuzzerSettings) : RandomCypherFuzzer(settings) {

    private var cypherSchema: CypherSchema = CypherSchema()

    private fun getClass(className: String): KClass<*> {
        return when (className) {
            "STRING" -> String::class
            "INTEGER" -> Int::class
            "BOOLEAN" -> Boolean::class
            "FLOAT" -> Float::class
            "DOUBLE" -> Double::class
            "CHARACTER" -> Character::class
            else -> Nothing::class
        }
    }

    override fun getNodes(): List<CypherNode> {
        if (cypherSchema.nodes.isEmpty()) {
            val nodeLabels = mutableListOf<String>()
            val nodeQuery = settings.session.run("MATCH (n) RETURN distinct labels(n)")
            nodeQuery.list().forEach { record ->
                nodeLabels.add(record[0].toString().substring(2 until record[0].toString().length - 2))
            }

            val nodeAttributes = mutableListOf<Pair<String, MutableList<Pair<String, String>>>>()
            nodeLabels.forEach { label ->
                val query = settings.session.run("MATCH(n:${label}) UNWIND keys(n) AS node_attrs RETURN DISTINCT node_attrs, apoc.meta.cypher.type(n[node_attrs])")
                var pair = nodeAttributes.firstOrNull { it.first == label }
                if (pair == null) {
                    pair = Pair(label, mutableListOf())
                    nodeAttributes += pair
                }
                query.list().forEach { record -> pair.second.add(
                    Pair(
                        record[0].toString().slice(1 until record[0].toString().length - 1), record[1].toString().substring(1 until record[1].toString().length - 1)
                    )
                ) }
            }

            val nodes = nodeAttributes.map { node ->
                val label = node.first
                val attributes = node.second.map {
                    Pair(it.first, getClass(it.second))
                }

                CypherNode(label, attributes, mutableListOf())
            }

            val outgoingRels = mutableListOf<Pair<String, MutableList<Pair<String, String>>>>()
            nodeLabels.forEach { label ->
                val query = settings.session.run("MATCH (start:${label})-[rel]->(end)\n" +
                        "WITH DISTINCT head(labels(end)) AS startLabel, type(rel) AS relationshipType\n" +
                        "RETURN startLabel, relationshipType")
                var pair = outgoingRels.firstOrNull { it.first == label }
                if (pair == null) {
                    pair = Pair(label, mutableListOf())
                    outgoingRels += pair
                }
                query.list().forEach { record -> pair.second.add(
                    Pair(record[0].toString().substring(1 until record[0].toString().length - 1), record[1].toString()))
                }
            }

            outgoingRels.forEach { rel ->
                val node = nodes.find { it.label == rel.first }
                node?.relations?.addAll(rel.second.map {
                    Triple( it.second.substring(1..it.second.length-2), CypherRelationshipDirection.OUT, it.first.substring(1 until it.first.length - 1) )
                })
            }

            val ingoingRels = mutableListOf<Pair<String, MutableList<Pair<String, String>>>>()
            nodeLabels.forEach { label ->
                val query = settings.session.run("MATCH (start)-[rel]->(end:${label})\n" +
                        "WITH DISTINCT head(labels(start)) AS startLabel, type(rel) AS relationshipType\n" +
                        "RETURN startLabel, relationshipType")
                var pair = ingoingRels.firstOrNull { it.first == label }
                if (pair == null) {
                    pair = Pair(label, mutableListOf())
                    ingoingRels += pair
                }
                query.list().forEach { record -> pair.second.add(
                    Pair(record[0].toString().substring(1..<record[0].toString().length - 1), record[1].toString()))
                }
            }
            ingoingRels.forEach { rel ->
                val node = nodes.find { it.label == rel.first }
                node?.relations?.addAll(rel.second.map { Triple( it.second.substring(1..it.second.length-2), CypherRelationshipDirection.IN, it.first) })
            }
            cypherSchema = CypherSchema(nodes, cypherSchema.relationships)
        }
        return cypherSchema.nodes
    }

    override fun getRelationships(vararg nodePairs: Pair<CypherNode, Node>): List<CypherRelationship> {
        if (cypherSchema.relationships.isEmpty()) {
            val relationshipData = mutableListOf<Pair<String, MutableList<Pair<String, String>>>>()
            val relationshipQuery = settings.session.run("MATCH ()-[r]-() UNWIND keys(r) AS rel_keys RETURN DISTINCT type(r), rel_keys, apoc.meta.cypher.type(r[rel_keys])")
            relationshipQuery
                .list()
                .forEach { record ->
                    val label = record[0].toString().substring(1..<record[0].toString().length - 1)
                    val attributeLabel = record[1].toString().substring(1..<record[1].toString().length - 1)
                    val className = record[2].toString().substring(1 until record[2].toString().length - 1)
                    var pair = relationshipData.firstOrNull { it.first == label}
                    if (pair == null) {
                        pair = Pair(label, mutableListOf())
                        relationshipData += pair
                    }
                    pair.second.add(Pair(attributeLabel, className))
                }
            val relationships = relationshipData
                .map { relationship ->
                    CypherRelationship(
                        relationship.first,
                        relationship.second
                            .map { Pair(it.first, getClass(it.second)) }
                            .filter { it.second != Nothing::class }
                    )
                }
            cypherSchema = CypherSchema(cypherSchema.nodes, relationships)
        }
        return cypherSchema.relationships
    }
}