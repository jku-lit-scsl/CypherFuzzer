package fuzzer.cypher.extension

import fuzzer.extension.nextPositiveInt
import org.neo4j.cypherdsl.core.StatementBuilder
import kotlin.random.Random

fun StatementBuilder.OngoingReadingAndReturn.addLimit(random: Random): StatementBuilder.OngoingReadingAndReturn
 = apply { when { random.nextBoolean() -> this.limit(random.nextPositiveInt()) }  }