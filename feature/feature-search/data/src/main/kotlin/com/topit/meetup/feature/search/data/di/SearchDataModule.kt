package com.topit.meetup.feature.search.data.di

import com.topit.meetup.feature.search.data.remote.SearchApi
import com.topit.meetup.feature.search.data.remote.SearchProfileApi
import com.topit.meetup.feature.search.data.repository.SearchRepositoryImpl
import com.topit.meetup.feature.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchDataModule {
    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {
        @Provides
        @Singleton
        fun provideSearchApi(retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)

        @Provides
        @Singleton
        fun provideSearchProfileApi(retrofit: Retrofit): SearchProfileApi =
            retrofit.create(SearchProfileApi::class.java)
    }
}
