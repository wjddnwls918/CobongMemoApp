package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(MemoListItem::class), version = 1)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun memolistDao(): MemoListDao

    companion object {
        @Volatile
        private var INSTANCE: MemoDatabase? = null

        fun getDatabase(context: Context): MemoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoDatabase::class.java,
                    "Memo_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }

    }

}