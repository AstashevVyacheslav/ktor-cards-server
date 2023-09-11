package ru.astashev.domain.repository

import ru.astashev.data.model.UserModel

interface UserRepository {

    suspend fun getUserByEmail(email: String): UserModel?

    suspend fun insertUser(userModel: UserModel)


}