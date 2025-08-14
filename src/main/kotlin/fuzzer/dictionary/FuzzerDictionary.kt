package fuzzer.dictionary

import kotlin.random.Random

/**
 * Represents a dictionary of values used for fuzzing [T]
 */
interface FuzzerDictionary<T> {

    /**
     * @param random used to randomly get a [T] from [FuzzerDictionary]
     * gets a value [T] of the [FuzzerDictionary]
     * @return value [T] from [FuzzerDictionary]
     */
    fun get(random: Random): T
}