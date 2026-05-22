package com.topit.meetup.feature.recommendations.data.di

import com.topit.meetup.feature.recommendations.data.remote.RecommendedProfileApi
import com.topit.meetup.feature.recommendations.data.remote.RecommendationsApi
import com.topit.meetup.feature.recommendations.data.repository.RecommendationsRepositoryImpl
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecommendationsDataModule {
    @Binds
    @Singleton
    abstract fun bindRecommendationsRepository(impl: RecommendationsRepositoryImpl): RecommendationsRepository

    companion object {
        @Provides
        @Singleton
        fun provideRecommendationsApi(retrofit: Retrofit): RecommendationsApi =
            retrofit.create(RecommendationsApi::class.java)

        @Provides
        @Singleton
        fun provideRecommendedProfileApi(retrofit: Retrofit): RecommendedProfileApi =
            retrofit.create(RecommendedProfileApi::class.java)
    }
}
