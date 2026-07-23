package ru.timurtanyrbergenov.afterglow.fragments.importantdates.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.repository.EventsRepository
import java.time.LocalDate

class ImportantDatesViewModel(
    private val repository: EventsRepository
) : ViewModel() {

    private val selectedDate =
        MutableStateFlow(LocalDate.now())

    val events: StateFlow<List<EventEntity>> =
        selectedDate
            .flatMapLatest { date ->
                repository.getEventsForDate(date)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.addEvent(event)
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.updateEvent(event)
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

}