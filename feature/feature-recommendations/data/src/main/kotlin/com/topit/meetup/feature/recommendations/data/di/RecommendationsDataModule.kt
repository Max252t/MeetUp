package com.topit.meetup.feature.recommendations.data.di

import com.topit.meetup.feature.recommendations.data.repository.RecommendationsRepositoryImpl
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecommendationsDataModule {
    @Binds
    @Singleton
    abstract fun bindRecommendationsRepository(impl: RecommendationsRepositoryImpl): RecommendationsRepository
}
