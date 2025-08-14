package fuzzer.dictionary.word

import fuzzer.dictionary.FuzzerDictionary
import fuzzer.extension.nextPositiveInt
import java.io.File
import java.util.function.Function
import kotlin.random.Random

/**
 * extendable implementation for a [FuzzerDictionary] of [String] that contains words as a line-separated list
 * default: uses this [list](https://docs.oracle.com/javase/tutorial/collections/interfaces/examples/dictionary.txt)
 */
open class WordDictionary(protected val pathToDic: String = "C:\\dev\\CypherFuzzer\\src\\main\\resources\\word_dictionary.txt") :
    FuzzerDictionary<String> {

    companion object {
        @Volatile
        /**
         * [Map] with the line number as key and the word as value
         */
        private var Instance: Map<Int, String>? = null

        /**
         * gets the instance of the map that holds the data ([Instance])
         */
        fun getInstance(dicPath: String, lineFilter: Function<Pair<Int, String>, Boolean>): Map<Int, String> {
            return Instance ?: synchronized(this) {
                Instance ?: File(dicPath).useLines { lines ->
                    lines.mapIndexed { index, word -> index to word.trim() }
                        .filter { lineFilter.apply(Pair(it.first, it.second)) }
                        .toMap()
                }.also { Instance = it }
            }
        }
    }

    /**
     * @param random: instance of [Random] to get a random line number
     * gets a word at a random line
     * @return a random word
     */
    override fun get(random: Random): String {
        val dictionary = getInstance(pathToDic, lineFilter = { true })
        return dictionary[random.nextPositiveInt(dictionary.size)].orEmpty()
    }
}

/**
 * @param random: instance of [Random] to get a random line number
 * @param wordDictionary: a [WordDictionary] from which to get a word
 * extension function to make [String] return a word
 * @return a random word from [wordDictionary]
 */
fun String.Companion.randomWord(random: Random, wordDictionary: WordDictionary = WordDictionary()): String = wordDictionary.get(random)