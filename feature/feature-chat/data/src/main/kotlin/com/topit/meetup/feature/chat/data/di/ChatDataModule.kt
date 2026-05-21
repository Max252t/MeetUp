package com.topit.meetup.feature.chat.data.di

import com.topit.meetup.feature.chat.data.repository.ChatRepositoryImpl
import com.topit.meetup.feature.chat.data.repository.MessageRepositoryImpl
import com.topit.meetup.feature.chat.domain.repository.ChatRepository
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatDataModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}
