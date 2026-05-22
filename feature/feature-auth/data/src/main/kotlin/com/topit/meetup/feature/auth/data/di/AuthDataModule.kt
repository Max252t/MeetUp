package com.topit.meetup.feature.auth.data.di

import com.topit.meetup.feature.auth.data.remote.AuthApi
import com.topit.meetup.feature.auth.data.remote.MeApi
import com.topit.meetup.feature.auth.data.repository.AuthRepositoryImpl
import com.topit.meetup.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

        @Provides
        @Singleton
        fun provideMeApi(retrofit: Retrofit): MeApi = retrofit.create(MeApi::class.java)
    }
}
