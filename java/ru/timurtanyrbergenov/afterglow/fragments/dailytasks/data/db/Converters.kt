package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db

import androidx.room.TypeConverter
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.LoveLanguage
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskStatus
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromLoveLanguage(value: LoveLanguage): String = value.name

    @TypeConverter
    fun toLoveLanguage(value: String): LoveLanguage = LoveLanguage.valueOf(value)

    @TypeConverter
    fun fromStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): TaskStatus = try {
        TaskStatus.valueOf(value)
    } catch (e: IllegalArgumentException) {
        TaskStatus.ACTIVE  // Дефолт на ACTIVE; можно добавить Log.e("Converters", "Invalid status: $value", e)
    }
}
