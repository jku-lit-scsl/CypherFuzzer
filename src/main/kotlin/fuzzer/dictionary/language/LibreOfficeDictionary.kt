package fuzzer.dictionary.language

import fuzzer.dictionary.word.WordDictionary
import fuzzer.extension.nextPositiveInt
import kotlin.random.Random

/**
 * Implementation of a LibreOffice dictionary
 * Default: uses the [Austrian dictionary](https://github.com/LibreOffice/dictionaries/blob/master/de/de_AT_frami.dic)
 *
 * Supported are all files that follow the format of the default dictionary.
 */
class LibreOfficeDictionary : WordDictionary("C:\\dev\\CypherFuzzer\\src\\main\\resources\\de_AT_frami.dic") {

    override fun get(random: Random): String {
        val dictionary = getInstance(pathToDic, lineFilter = {
            when {
                it.second.isBlank() -> false
                it.second[0].isDigit() -> false
                it.second[0] == '#' -> false
                else -> true
            }
        })
        val word = dictionary[random.nextPositiveInt(dictionary.size)].orEmpty()
        return if (word.isNotEmpty()) word.substringBefore("/") else word
    }
}