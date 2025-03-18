package com.hyun.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class WordOfTheDayApplication

fun main(args: Array<String>) {
	runApplication<WordOfTheDayApplication>(*args)
}
