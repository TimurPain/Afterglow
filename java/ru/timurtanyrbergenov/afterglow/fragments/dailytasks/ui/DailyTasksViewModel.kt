package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.repository.DailyTasksRepository
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.model.*
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.progress.DailyProgress
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyTasksViewModel(
    private val repository: DailyTasksRepository
) : ViewModel() {

    // Автоматически обновляется при изменении любой задачи в БД
    val items: LiveData<List<DailyListItem>> = repository.getAllTasksFlow()
        .map { allTasks ->
            val today = LocalDate.now()
            val todayTasks = repository.getTodayTasks() // suspend вызов — будет выполнен один раз
            val history = allTasks.filter { it.date < today }

            mutableListOf<DailyListItem>().apply {
                add(DailyListItem.TodayHeader)
                todayTasks.forEach { add(DailyListItem.TodayTask(it)) }

                if (history.isNotEmpty()) {
                    add(DailyListItem.HistoryHeader)
                    history
                        .groupBy { it.date }
                        .toSortedMap(compareByDescending { it })
                        .forEach { (date, tasks) ->
                            // Формат 22.12.2025
                            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            add(DailyListItem.DayHeader(formattedDate))
                            tasks.forEach { add(DailyListItem.HistoryTask(it)) }
                        }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        .asLiveData()

    // Прогресс тоже обновляется автоматически
    val totalProgress: LiveData<DailyProgress> = repository.getAllTasksFlow()
        .map { tasks ->
            val completed = tasks.filter { it.status == TaskStatus.DONE }.sumOf { it.points }
            val total = tasks.sumOf { it.points }
            DailyProgress(completed, total)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DailyProgress(0, 0))
        .asLiveData()

    // Методы для изменения статуса — БД обновится → Flow → UI автоматически
    fun complete(task: TaskTemplate) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = TaskStatus.DONE))
        }
    }

    fun skip(task: TaskTemplate) {
        viewModelScope.launch {
            repository.updateTask(task.copy(status = TaskStatus.SKIPPED))
        }
    }

    fun getTodayTasks() {
        viewModelScope.launch {
            repository.getTodayTasks()  // Просто вызовем, чтобы сгенерировать при необходимости
        }
    }
}