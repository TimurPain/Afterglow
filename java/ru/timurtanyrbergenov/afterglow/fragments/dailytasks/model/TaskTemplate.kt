package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model

import java.time.LocalDate

enum class TaskStatus { ACTIVE, DONE, SKIPPED }

enum class LoveLanguage(val displayName: String) {
    WORDS("Слова поддержки"),
    ACTS("Помощь делами"),
    TIME("Совместное время"),
    GIFTS("Подарки"),
    TOUCH("Тактичность")
}

data class TaskTemplate(
    val id: Long = 0,
    val title: String,
    val points: Int,
    val loveLanguage: LoveLanguage,
    var status: TaskStatus,
    val date: LocalDate
)

data class ProgressTuple(
    val completed: Int,
    val total: Int
)

sealed class DailyListItem {
    object TodayHeader : DailyListItem()
    data class TodayTask(val task: TaskTemplate) : DailyListItem()
    object HistoryHeader : DailyListItem()
    data class DayHeader(val date: String) : DailyListItem()
    data class HistoryTask(val task: TaskTemplate) : DailyListItem()
}


