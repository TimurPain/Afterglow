package ru.timurtanyrbergenov.afterglow.fragments.importantdates.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local.AppDatabase
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.repository.EventsRepository

class ImportantDatesViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val repository = EventsRepository(db.eventsDao())
        return ImportantDatesViewModel(repository) as T
    }
}