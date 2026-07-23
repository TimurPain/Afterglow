package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.entity.DailyTaskEntity
import java.time.LocalDate

@Dao
interface DailyTaskDao {

    @Query("SELECT * FROM daily_tasks")
    fun getAllFlow(): Flow<List<DailyTaskEntity>>

    @Query("SELECT * FROM daily_tasks WHERE date = :date")
    suspend fun getByDate(date: LocalDate): List<DailyTaskEntity>

    @Insert
    suspend fun insertAll(list: List<DailyTaskEntity>)

    @Update
    suspend fun update(entity: DailyTaskEntity)
}