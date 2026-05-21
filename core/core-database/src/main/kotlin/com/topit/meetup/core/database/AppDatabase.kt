package com.topit.meetup.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.topit.meetup.core.database.dao.ConversationDao
import com.topit.meetup.core.database.dao.MessageDao
import com.topit.meetup.core.database.dao.UserDao
import com.topit.meetup.core.database.entity.ConversationEntity
import com.topit.meetup.core.database.entity.MessageEntity
import com.topit.meetup.core.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, ConversationEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}
