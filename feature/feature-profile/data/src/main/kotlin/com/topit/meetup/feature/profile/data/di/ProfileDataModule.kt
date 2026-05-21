package com.topit.meetup.feature.profile.data.di

import com.topit.meetup.feature.profile.data.repository.ProfileRepositoryImpl
import com.topit.meetup.feature.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileDataModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}
