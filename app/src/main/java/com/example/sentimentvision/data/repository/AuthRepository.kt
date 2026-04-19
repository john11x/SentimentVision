package com.example.sentimentvision.data.repository

import android.util.Base64
import java.security.MessageDigest
import com.example.sentimentvision.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val userDao: UserDao) {

    private fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }

    suspend fun register(username: String, pin: String, displayName: String? = null): Result<UserEntity> {
        if (username.isBlank() || pin.length < 4) {
            return Result.failure(IllegalArgumentException("Invalid username or PIN"))
        }

        val existing = userDao.findByUsername(username)
        if (existing != null) {
            return Result.failure(IllegalStateException("User already exists"))
        }

        val hash = sha256(pin)
        val user = UserEntity(username = username, pinHash = hash, displayName = displayName)
        val id = userDao.insert(user)
        return if (id > 0) Result.success(user.copy(id = id.toInt())) else Result.failure(Exception("Insert failed"))
    }

    suspend fun login(username: String, pin: String): Result<UserEntity> {
        val user = userDao.findByUsername(username) ?: return Result.failure(Exception("User not found"))
        val hash = sha256(pin)
        return if (user.pinHash == hash) Result.success(user) else Result.failure(Exception("Invalid credentials"))
    }

    fun listUsers(): Flow<List<UserEntity>> = userDao.getAll()
}