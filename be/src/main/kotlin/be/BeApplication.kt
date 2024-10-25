package be

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import shared.SharedTest

@SpringBootApplication
class BeApplication

fun main(args: Array<String>) {
    runApplication<BeApplication>(*args)

    println(SharedTest.test())
}
