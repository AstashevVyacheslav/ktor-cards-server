package ru.astashev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.astashev.authentification.JwtService
import ru.astashev.data.repository.CardRepositoryImpl
import ru.astashev.data.repository.UserRepositoryImpl
import ru.astashev.domain.usecase.CardUseCase
import ru.astashev.domain.usecase.UserUseCase
import ru.astashev.plugins.DatabaseFactory.initializationDatabase
import ru.astashev.plugins.configureMonitoring
import ru.astashev.plugins.configureSecurity
import ru.astashev.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val jwtService = JwtService()
    val userRepository = UserRepositoryImpl()
    val cardRepository = CardRepositoryImpl()
    val userUseCase = UserUseCase(userRepository, jwtService)
    val cardUseCase = CardUseCase(cardRepository)

    initializationDatabase()
    configureMonitoring()
    configureSerialization()
    configureSecurity(userUseCase)
//    configureRouting()
}
