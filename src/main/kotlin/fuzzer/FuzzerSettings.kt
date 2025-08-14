package fuzzer

import kotlin.random.Random

/**
 * Represents settings used by [fuzzer.FuzzerBase]
 */
interface FuzzerSettings {

    /**
     * instance of [Random]
     */
    val random: Random
}