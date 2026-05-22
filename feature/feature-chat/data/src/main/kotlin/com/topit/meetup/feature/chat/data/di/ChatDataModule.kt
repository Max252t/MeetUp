package com.topit.meetup.feature.chat.data.di

import com.topit.meetup.feature.chat.data.remote.ChatApi
import com.topit.meetup.feature.chat.data.remote.ConversationsApi
import com.topit.meetup.feature.chat.data.remote.ParticipantProfileApi
import com.topit.meetup.feature.chat.data.repository.ChatRepositoryImpl
import com.topit.meetup.feature.chat.data.repository.MessageRepositoryImpl
import com.topit.meetup.feature.chat.domain.repository.ChatRepository
import com.topit.meetup.feature.chat.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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

    companion object {
        @Provides
        @Singleton
        fun provideChatApi(retrofit: Retrofit): ChatApi = retrofit.create(ChatApi::class.java)

        @Provides
        @Singleton
        fun provideConversationsApi(retrofit: Retrofit): ConversationsApi =
            retrofit.create(ConversationsApi::class.java)

        @Provides
        @Singleton
        fun provideParticipantProfileApi(retrofit: Retrofit): ParticipantProfileApi =
            retrofit.create(ParticipantProfileApi::class.java)
    }
}
