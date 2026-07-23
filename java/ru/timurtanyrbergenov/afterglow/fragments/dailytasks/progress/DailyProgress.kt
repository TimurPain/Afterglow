package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.progress

data class DailyProgress(
    val completed: Int,
    val total: Int
) {
    val percent: Int
        get() = if (total == 0) 0 else (completed * 100 / total)
}