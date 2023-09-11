package ru.astashev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.astashev.plugins.DatabaseFactory.initializationDatabase
import ru.astashev.plugins.configureMonitoring
import ru.astashev.plugins.configureSecurity
import ru.astashev.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    initializationDatabase()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
//    configureRouting()
}
