package ru.astashev.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.astashev.authentification.hash
import ru.astashev.data.model.UserModel
import ru.astashev.data.model.getRoleByString
import ru.astashev.data.model.requests.LoginRequest
import ru.astashev.data.model.requests.RegisterRequest
import ru.astashev.data.model.response.BaseResponse
import ru.astashev.domain.usecase.UserUseCase
import ru.astashev.utils.Constants

fun Route.UserRoute(userUseCase: UserUseCase) {

    val hashFunction = { p: String  -> hash(password = p) }

    post("api/v1/signup") {
        val registerRequest = call.receiveNullable<RegisterRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        try {
            val user = UserModel(
                id = 0,
                email = registerRequest.email.trim().lowercase(),
                login = registerRequest.login.trim().lowercase(),
                password = hashFunction(registerRequest.password.trim()),
                firstName = registerRequest.firstName.trim(),
                lastName = registerRequest.lastName.trim(),
                role = registerRequest.role.trim().getRoleByString()
            )

            userUseCase.createUser(user)
            call.respond(HttpStatusCode.OK, BaseResponse(true, userUseCase.generateToken(userModel = user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
        }
    }
//работает

    post("api/v1/login") {
        val loginRequest = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        try {
            val user = userUseCase.findUserByEmail(loginRequest.email.trim().lowercase())

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.WRONG_EMAIL))
            } else {
                if (user.password == hashFunction(loginRequest.password)) {
                    call.respond(HttpStatusCode.OK, BaseResponse(true, userUseCase.generateToken(userModel = user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.INCORRECT_PASSWORD))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
        }
    }
}