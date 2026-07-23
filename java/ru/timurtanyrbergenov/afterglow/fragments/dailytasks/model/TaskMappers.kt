package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model

import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.entity.DailyTaskEntity

fun DailyTaskEntity.toModel(): TaskTemplate =
    TaskTemplate(
        id = id,
        title = title,
        points = points,
        loveLanguage = loveLanguage,
        status = status,
        date = date
    )

fun TaskTemplate.toEntity(): DailyTaskEntity =
    DailyTaskEntity(
        id = id,
        title = title,
        points = points,
        loveLanguage = loveLanguage,
        status = status,
        date = date
    )
