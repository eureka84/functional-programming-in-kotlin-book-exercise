package org.eureka.kotlin.fp.ch9

import org.eureka.kotlin.fp.ch4.None
import org.eureka.kotlin.fp.ch4.Option
import org.eureka.kotlin.fp.ch4.Some
import org.eureka.kotlin.fp.ch4.filter

abstract class ParsersImpl<A>: Parsers<ParseError> {

    override fun string(s: String): Parser<String> =
        { state: Location ->
            when (val idx = firstNonMatchingIndex(state.input, s, state.offset)) {
                is None ->
                    Success(s, s.length)
                is Some ->
                    Failure(
                        state.advanceBy(idx.get).toError("'$s'"),
                        idx.get != 0
                    )
            }
        }
    private fun firstNonMatchingIndex(hayStack: String, needle: String, offset: Int): Option<Int> {
        var i = 0
        while (i < hayStack.length && i < needle.length) {
            if (hayStack[i + offset] != needle[i])
                return Some(i)
            else i += 1
        }
        return if (hayStack.length - offset >= needle.length) None else Some(hayStack.length - offset)
    }

    private fun Location.advanceBy(i: Int) =
        this.copy(offset = this.offset + i)

    override fun regex(r: String): Parser<String> =
        { state: Location ->
            when (val prefix = state.input.findPrefixOf(r.toRegex())) {
                is Some ->
                    Success(prefix.get.value, prefix.get.value.length)
                is None ->
                    Failure(state.toError("regex ${r.toRegex()}"))
            }
        }
    private fun String.findPrefixOf(r: Regex): Option<MatchResult> =
        r.find(this).toOption().filter { it.range.first == 0 }

    override fun <A> succeed(a: A): Parser<A> = { Success(a, 0) }
    override fun <A> Parser<A>.slice(): Parser<String> =
        { state: Location ->
            when (val result = this(state)) {
                is Success ->
                    Success(state.slice(result.consumed), result.consumed)
                is Failure -> result
            }
        }
    private fun Location.slice(n: Int) =
        this.input.substring(this.offset..this.offset + n)

    private fun Location.toError(msg: String) =
        ParseError(listOf(Pair(this, msg)))

    private fun <T> T?.toOption(): Option<T> = this?.let { Some(it) } ?: None

}