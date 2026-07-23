package ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val date: LocalDate,
    val type: EventType,
    val priority: Int
)


