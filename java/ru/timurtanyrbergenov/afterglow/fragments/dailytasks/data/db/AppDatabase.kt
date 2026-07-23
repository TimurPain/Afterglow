package ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration  // Импорт для Migration
import androidx.sqlite.db.SupportSQLiteDatabase  // Импорт для SupportSQLiteDatabase
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.dao.DailyTaskDao
import ru.timurtanyrbergenov.afterglow.fragments.dailytasks.data.db.entity.DailyTaskEntity

@Database(entities = [DailyTaskEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dailyTaskDao(): DailyTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "daily_tasks_db"
                )
                    .addMigrations(MIGRATION_1_2)  // Добавьте миграцию здесь
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Миграция от версии 1 к 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Обновляем старые статусы, если они есть
        db.execSQL("UPDATE daily_tasks SET status = 'DONE' WHERE status = 'COMPLETED'")
    }
}