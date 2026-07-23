package ru.timurtanyrbergenov.afterglow.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.repository.EventsRepository

class HomeViewModel(private val repository: EventsRepository) : ViewModel() {
    val nearestEvent: Flow<EventEntity?> = repository.getNearestEvent()
}

class HomeViewModelFactory(private val repository: EventsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
