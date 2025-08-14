package fuzzer.extension

import kotlin.random.Random

fun Random.nextPositiveInt() = nextPositiveInt(Int.MAX_VALUE)

fun Random.nextPositiveInt(until: Int) = nextInt(0, until)

fun Random.randomSubRange(range: IntRange): IntRange {
    return when {
        range.isEmpty() -> IntRange.EMPTY
        range.count() == 1 && range.last == 0 -> IntRange.EMPTY
        range.count() == 1 && range.last == 1 -> (1..1)
        range.count() == 1 && range.first == range.last -> 1..range.first
        else -> 1.. nextInt(range.first, range.last + 1)
    }
}

inline fun <reified T : Enum<T>> Random.enumValueOf(): T =  enumValues<T>().random(this)