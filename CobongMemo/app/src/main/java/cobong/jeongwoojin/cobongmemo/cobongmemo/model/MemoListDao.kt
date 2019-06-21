package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemoListDao {

    @Query("SELECT * from memo order by `index` desc")
    fun getAllMemos(): LiveData<List<MemoItem>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: MemoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemos(vararg memos: MemoItem)

    @Query("delete from memo")
    fun deleteAll()

}