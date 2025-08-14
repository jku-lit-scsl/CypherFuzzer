package fuzzer.cypher.fuzzer

import fuzzer.FuzzerSettings
import kotlin.random.Random

/**
 * base for all settings for [CypherFuzzerBase]
 */
abstract class CypherFuzzerSettings(override val random: Random) : FuzzerSettings