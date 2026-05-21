package com.topit.meetup.feature.profile.data.mapper

import com.topit.meetup.feature.profile.data.remote.dto.ProfileDto
import com.topit.meetup.feature.profile.data.remote.dto.UpsertProfileRequestDto
import com.topit.meetup.feature.profile.domain.model.Interest
import com.topit.meetup.feature.profile.domain.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun ProfileDto.toDomain(): User {
    val birthYear = birthDate?.let { parseYear(it) }
    val age = birthYear?.let { Calendar.getInstance().get(Calendar.YEAR) - it } ?: 0
    return User(
        id = id,
        name = displayName,
        age = age,
        bio = bio ?: "",
        avatarUrl = photoUrls.firstOrNull(),
        interests = interests.mapIndexed { i, name -> Interest(i.toString(), name) },
        location = listOfNotNull(city, country).joinToString(", ").ifEmpty { null },
    )
}

fun User.toUpsertDto(): UpsertProfileRequestDto = UpsertProfileRequestDto(
    displayName = name,
    bio = bio.ifEmpty { null },
    interests = interests.map { it.name },
)

private fun parseYear(dateStr: String): Int? = runCatching {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val date: Date? = sdf.parse(dateStr)
    val cal = Calendar.getInstance().apply { time = date!! }
    cal.get(Calendar.YEAR)
}.getOrNull()
