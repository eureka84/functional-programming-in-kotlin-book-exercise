package org.eureka.kotlin.fp.ch11

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch11.MonadInstances.optionMonad
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.Option
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class MonadTest {

    @ParameterizedTest(name = "{displayName} - input: {0}")
    @ArgumentsSource(SequenceListOfOptionArguments::class)
    fun `sequence a list of option`(input: List<Option<Int>>, result: Option<List<Int>>) {
        optionMonad().sequence(input) shouldBe result
    }

    class SequenceListOfOptionArguments: ArgumentsProvider {
        override fun provideArguments(p0: ExtensionContext?): Stream<out Arguments> = Stream.of(
            Arguments.of(
                List.of(Option.of(3), Option.of(4), Option.of(5)),
                Option.of(List.of(3, 4, 5))
            ),
            Arguments.of(
                List.of(Option.of(3), Option.of(4), Option.empty()),
                Option.empty<List<Int>>()
            )
        )
    }
}