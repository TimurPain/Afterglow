package ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

class Converter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString() // ISO-8601: 2025-12-19
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

}

