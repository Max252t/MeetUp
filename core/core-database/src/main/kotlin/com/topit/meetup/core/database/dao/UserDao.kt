package com.topit.meetup.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.topit.meetup.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: String): Flow<UserEntity?>

    @Upsert
    suspend fun upsert(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: String)
}
