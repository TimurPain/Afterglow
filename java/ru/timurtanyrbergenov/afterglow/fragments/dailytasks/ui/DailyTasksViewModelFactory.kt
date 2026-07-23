package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.repository.DailyTasksRepository

class DailyTasksViewModelFactory(
    private val repository: DailyTasksRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyTasksViewModel(repository) as T
    }
}


