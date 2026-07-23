package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.dao.DailyTaskDao
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskTemplate
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.TaskStatus
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.toEntity
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.toModel
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.progress.DailyProgress
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.util.TaskGenerator
import java.time.LocalDate

class DailyTasksRepository(
    private val dao: DailyTaskDao
) {

    fun getAllTasksFlow(): Flow<List<TaskTemplate>> =
        dao.getAllFlow().map { entities -> entities.map { it.toModel() } }

    suspend fun getTodayTasks(): List<TaskTemplate> {
        val today = LocalDate.now()
        val stored = dao.getByDate(today)
        return if (stored.isNotEmpty()) {
            stored.map { it.toModel() }
        } else {
            val generated = TaskGenerator.generateForDate(today)
            dao.insertAll(generated.map { it.toEntity() })
            generated
        }
    }

    suspend fun updateTask(task: TaskTemplate) {
        dao.update(task.toEntity())
    }
}