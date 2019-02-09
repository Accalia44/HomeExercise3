package at.fh.swengb.gradwohl.homework3

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Note::class, Users::class], version = 8)
abstract class RoomDB : RoomDatabase() {

    abstract val notesDao: NotesDao
    abstract val userDao: UserDao

    companion object {
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(context, RoomDB::class.java, "notesandusers-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
