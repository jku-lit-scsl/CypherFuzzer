package fuzzer.extension

import kotlin.random.Random

fun Boolean.Companion.random(random: Random): Boolean = random.nextBoolean()

fun Byte.Companion.random(random: Random): Byte = random.nextInt(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt() + 1).toByte()

fun Short.Companion.random(random: Random): Short = random.nextInt(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt() + 1).toShort()

fun Int.Companion.random(random: Random): Int = random.nextInt()

fun Long.Companion.random(random: Random): Long = random.nextLong()

fun Float.Companion.random(random: Random): Float = random.nextFloat() * Float.MAX_VALUE * if (random.nextBoolean()) 1 else -1

fun Double.Companion.random(random: Random): Double = random.nextDouble() * Double.MAX_VALUE * if (random.nextBoolean()) 1 else -1

/**
 * Returns one of the following ASCII chars in range
 * - SPACE (32)
 * - ~ (126)
 */
fun Char.Companion.random(random: Random): Char = random.nextInt(' '.code, '~'.code + 1).toChar()

fun String.Companion.random(length: Int = Int.MAX_VALUE, random: Random): String = (0 .. length)
    .map { Char.random(random) }
    .joinToString("")