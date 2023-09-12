package ru.astashev.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.astashev.domain.usecase.CardUseCase
import ru.astashev.domain.usecase.UserUseCase
import ru.astashev.routes.UserRoute

fun Application.configureRouting(userUseCase: UserUseCase, cardUseCase: CardUseCase) {

    routing {
        UserRoute(userUseCase = userUseCase)
    }
}
