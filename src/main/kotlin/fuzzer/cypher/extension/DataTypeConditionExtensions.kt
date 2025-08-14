package fuzzer.cypher.extension

import org.neo4j.cypherdsl.core.Property
import kotlin.random.Random

fun Boolean.Companion.randomConditionFunction(random: Random) = listOf(
    Property::eq,
    Property::ne
).random(random)

fun Byte.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun Short.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun Int.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun Long.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun Float.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun Double.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun Char.Companion.randomConditionFunction(random: Random) = listOf(
    Property::gt,
    Property::gte,
    Property::lt,
    Property::lte,
    Property::eq,
    Property::ne,
).random(random)

fun String.Companion.randomConditionFunction(random: Random) = listOf(
    Property::startsWith,
    Property::endsWith,
    Property::contains,
    Property::eq,
    Property::ne,
).random(random)