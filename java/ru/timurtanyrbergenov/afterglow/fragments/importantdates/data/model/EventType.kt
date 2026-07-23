package ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model

enum class EventType(val title: String, val priority: Int, val color: Int) {
    ANNIVERSARY("Годовщина", 4, 0xFFFF9800.toInt()), // оранжевый
    FIRST_MEET("День знакомства", 3, 0xFF8BC34A.toInt()), // зеленый
    BIRTHDAY("День рождения", 2, 0xFFC62828.toInt()), // красный
    GIFT("Подарок", 1, 0xFFFFD700.toInt()), // желтый
    DEFAULT("Другое", 0, 0xFFB0B0B0.toInt()) // серый
}
