package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.LoveLanguage
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskStatus
import java.time.LocalDate

@Entity(tableName = "daily_tasks")
data class DailyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val points: Int,
    val loveLanguage: LoveLanguage,
    var status: TaskStatus,
    val date: LocalDate
)
