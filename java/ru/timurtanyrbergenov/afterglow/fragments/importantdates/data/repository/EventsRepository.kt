package ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.repository

import kotlinx.coroutines.flow.Flow
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local.EventsDao
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity
import java.time.LocalDate

class EventsRepository(
    private val dao: EventsDao
) {
    fun getEventsForDate(date: LocalDate): Flow<List<EventEntity>> {
        return dao.getEventsForDate(date)
    }

    suspend fun addEvent(event: EventEntity) {
        dao.insert(event)
    }

    suspend fun updateEvent(event: EventEntity) =
        dao.update(event)

    suspend fun deleteEvent(event: EventEntity) {
        dao.delete(event)
    }

    fun getNearestEvent(): Flow<EventEntity?> = dao.getNearestEvent(LocalDate.now())

    suspend fun getEventByDate(date: LocalDate): EventEntity? {
        return dao.getEventByDate(date)
    }

}


