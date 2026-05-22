package com.topit.meetup.feature.chats.data.di

import com.topit.meetup.feature.chats.data.remote.ChatsApi
import com.topit.meetup.feature.chats.data.remote.ParticipantProfileApi
import com.topit.meetup.feature.chats.data.repository.ChatsRepositoryImpl
import com.topit.meetup.feature.chats.domain.repository.ChatsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatsDataModule {
    @Binds
    @Singleton
    abstract fun bindChatsRepository(impl: ChatsRepositoryImpl): ChatsRepository

    companion object {
        @Provides
        @Singleton
        fun provideChatsApi(retrofit: Retrofit): ChatsApi = retrofit.create(ChatsApi::class.java)

        @Provides
        @Singleton
        fun provideParticipantProfileApi(retrofit: Retrofit): ParticipantProfileApi =
            retrofit.create(ParticipantProfileApi::class.java)
    }
}
