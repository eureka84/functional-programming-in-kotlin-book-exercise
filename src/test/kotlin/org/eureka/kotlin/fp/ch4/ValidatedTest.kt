package org.eureka.kotlin.fp.ch4

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch4.Validated.Companion.map2
import org.junit.jupiter.api.Test

data class Person(val name: Name, val age: Age)
data class Name(val value: String)
data class Age(val value: Int)

fun mkName(name: String): Validated<String, Name> =
    if (name.isBlank()) Validated.invalid("Name is empty.")
    else Validated.valid(Name(name))

fun mkAge(age: Int): Validated<String, Age> =
    if (age < 0) Validated.invalid("Age is out of range.")
    else Validated.valid(Age(age))

fun mkPerson(name: String, age: Int): Validated<String, Person> =
    map2(mkName(name), mkAge(age)) { n, a -> Person(n, a) }

class ValidatedTest {

    @Test
    fun `accumulating errors`() {
        mkPerson("", -2) shouldBe Invalid("Name is empty.", "Age is out of range.")
    }
}