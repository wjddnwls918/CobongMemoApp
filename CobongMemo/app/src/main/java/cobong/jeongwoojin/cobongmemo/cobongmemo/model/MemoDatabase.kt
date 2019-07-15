package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoListDao
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleDao
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem

@Database(entities = arrayOf(MemoItem::class, ScheduleItem::class), version = 1)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun memolistDao(): MemoListDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: MemoDatabase? = null

        fun getDatabase(context: Context): MemoDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(MemoDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoDatabase::class.java,
                    "memo_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }

    }

}