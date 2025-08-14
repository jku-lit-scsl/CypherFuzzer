package fuzzer.dictionary.password

import fuzzer.dictionary.word.WordDictionary
import kotlin.random.Random

/**
 * Implementation of a dictionary for passwords based on the list from [10-million-password-list-top-10000](https://github.com/danielmiessler/SecLists/blob/master/Passwords/Common-Credentials/10-million-password-list-top-10000.txt)
 */
class PasswordDictionary : WordDictionary("C:\\dev\\CypherFuzzer\\src\\main\\resources\\10-million-password-list-top-10000.txt")

/**
 * @param random: Instance of [Random] to get a random line number
 * @param passwordDictionary: A [PasswordDictionary]
 * Extension function that returns a random password from [passwordDictionary]
 * @return a random password from [passwordDictionary]
 */
fun String.Companion.randomPassword(random: Random, passwordDictionary: PasswordDictionary = PasswordDictionary()): String = passwordDictionary.get(random)

