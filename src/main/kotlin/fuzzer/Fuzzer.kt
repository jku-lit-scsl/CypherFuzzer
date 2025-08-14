package fuzzer

/**
 * Represents a generic fuzzer of type [T]
 */
interface Fuzzer<T> {

    /**
     * Method that returns a fuzz sequence of [T]
     * @return [Sequence] of [T]
     */
    fun fuzz(): Sequence<T>
}