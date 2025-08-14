package fuzzer.extension

import kotlin.random.Random

fun <T> Collection<T>.randomOrElse(random: Random, defaultValue: T): T {
    return if (this.isEmpty()) {
        defaultValue
    }
    else this.random(random)
}