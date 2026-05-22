package com.topit.meetup.feature.profile.data.di

import com.topit.meetup.feature.profile.data.remote.MediaApi
import com.topit.meetup.feature.profile.data.remote.ProfileApi
import com.topit.meetup.feature.profile.data.repository.ProfileRepositoryImpl
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileDataModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    companion object {
        @Provides
        @Singleton
        fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

        @Provides
        @Singleton
        fun provideMediaApi(retrofit: Retrofit): MediaApi = retrofit.create(MediaApi::class.java)
    }
}
