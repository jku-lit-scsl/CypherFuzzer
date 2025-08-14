package fuzzer

import kotlin.reflect.KClass

/**
 * Base class for all fuzzers
 */
abstract class FuzzerBase<T>(val fuzzerSettings: FuzzerSettings) : Fuzzer<T> {

    companion object {
        @JvmStatic
        /**
         * [List] of [KClass] that represents all simple/primitive types
         */
        protected val types: List<KClass<*>> by lazy {
            listOf<KClass<*>>(
                Boolean::class,
                Byte::class,
                Short::class,
                Int::class,
                Long::class,
                Float::class,
                Double::class,
                Char::class,
                String::class,
            )
        }
    }

    /**
     * instance of [kotlin.random.Random] provided by [fuzzerSettings]
     */
    protected val random by lazy { fuzzerSettings.random }
}