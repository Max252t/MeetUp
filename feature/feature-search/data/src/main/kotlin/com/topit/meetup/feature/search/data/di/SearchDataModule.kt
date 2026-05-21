package com.topit.meetup.feature.search.data.di

import com.topit.meetup.feature.search.data.repository.SearchRepositoryImpl
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchDataModule {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}
