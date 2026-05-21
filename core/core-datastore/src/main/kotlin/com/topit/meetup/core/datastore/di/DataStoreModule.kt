package com.topit.meetup.core.datastore.di

import com.topit.meetup.core.common.TokenStorage
import com.topit.meetup.core.datastore.InMemoryTokenStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {
    @Binds
    @Singleton
    abstract fun bindTokenStorage(impl: InMemoryTokenStorage): TokenStorage
}
