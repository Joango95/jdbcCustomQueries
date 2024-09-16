package com.joango.customqueries

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration
class CustomQueriesApplication

fun main(args: Array<String>) {
	runApplication<CustomQueriesApplication>(*args)
}
