package ru.astashev.domain.usecase

import ru.astashev.authentification.JwtService
import ru.astashev.data.model.UserModel
import ru.astashev.domain.repository.UserRepository

class UserUseCase(
    private val repositoryImpl: UserRepository,
    private val jwtService: JwtService
) {

    suspend fun createUser(userModel: UserModel) = repositoryImpl.insertUser(userModel =  userModel)

    suspend fun findUserByEmail(email: String) = repositoryImpl.getUserByEmail(email = email)

    fun generateToken(userModel: UserModel): String = jwtService.generateToken(user = userModel)

}