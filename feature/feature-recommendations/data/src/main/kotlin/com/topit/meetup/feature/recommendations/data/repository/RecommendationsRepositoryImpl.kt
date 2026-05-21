package com.topit.meetup.feature.recommendations.data.repository

import com.topit.meetup.core.common.DomainResult
import com.topit.meetup.feature.recommendations.domain.model.ReactionType
import com.topit.meetup.feature.recommendations.domain.model.RecommendedUser
import com.topit.meetup.feature.recommendations.domain.repository.RecommendationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationsRepositoryImpl @Inject constructor() : RecommendationsRepository {

    private val _recommendations = MutableStateFlow(FAKE_RECOMMENDATIONS)

    override fun getRecommendations(): Flow<List<RecommendedUser>> = _recommendations

    override suspend fun reactToUser(userId: String, reaction: ReactionType): DomainResult<Unit> {
        _recommendations.update { list -> list.filter { it.id != userId } }
        return DomainResult.Success(Unit)
    }

    companion object {
        val FAKE_RECOMMENDATIONS = listOf(
            RecommendedUser("user-1", "Анна", 25, "Люблю кофе и йогу", null, listOf("yoga", "coffee"), "Москва", 0.92f),
            RecommendedUser("user-2", "Дмитрий", 28, "Фотограф и путешественник", null, listOf("photography", "travel"), "Санкт-Петербург", 0.87f),
            RecommendedUser("user-3", "Мария", 23, "Hiking enthusiast", null, listOf("hiking", "nature"), "Москва", 0.81f),
            RecommendedUser("user-4", "Алексей", 30, "Спорт и кулинария", null, listOf("sports", "cooking"), "Казань", 0.76f),
            RecommendedUser("user-5", "Елена", 27, "Музыка и искусство", null, listOf("music", "art"), "Москва", 0.74f),
        )
    }
}
