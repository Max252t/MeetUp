package com.topit.meetup.feature.chats.data.di

import com.topit.meetup.feature.chats.data.repository.ChatsRepositoryImpl
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatsDataModule {
    @Binds
    @Singleton
    abstract fun bindChatsRepository(impl: ChatsRepositoryImpl): ChatsRepository
}
