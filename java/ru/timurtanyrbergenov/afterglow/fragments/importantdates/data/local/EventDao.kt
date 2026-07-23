package ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity
import java.time.LocalDate

@Dao
interface EventsDao {

    @Query("SELECT * FROM events WHERE date = :date ORDER BY priority DESC")
    fun getEventsForDate(date: LocalDate): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE date >= :today ORDER BY date ASC, priority DESC LIMIT 1")
    fun getNearestEvent(today: LocalDate): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE date = :date LIMIT 1")
    suspend fun getEventByDate(date: LocalDate): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)
}

