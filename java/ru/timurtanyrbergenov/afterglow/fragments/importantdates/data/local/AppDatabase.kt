package ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.timurtanyrbergenov.afterglow.fragments.importantdates.data.model.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventsDao(): EventsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "afterglow_db"
                )
                    .fallbackToDestructiveMigration() // ← ВАЖНО
                    .build()
                    .also { INSTANCE = it }
            }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "event_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
