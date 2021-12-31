package com.mercadolivro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class Application

// para rodar em prod tem que usar o comando -Dspring.profiles.active=prod na VM options
// para desabilitar o swagger em prod deve usar a annotation @Profile("!prod")
fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
