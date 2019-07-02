package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository

class MemoViewModel(application: Application): AndroidViewModel(application) {

    var navigator: MemoNavigator? = null
    private var repository: MemoRepository = MemoRepository.getInstance(application)

    lateinit var allMemos:MutableList<MemoListItem>
/*

    private lateinit var memoAdapter: MemoAdapter

    fun initAdapter() {
        memoAdapter = MemoAdapter(ArrayList(), this)
    }

    fun getAdapter(): MemoAdapter {
        return memoAdapter
    }
*/

    //get allmemos
    fun getAllMemos() {
        allMemos = repository.getAllMemo()
    }


    //delete memo
    fun deleteMemo (memo: MemoListItem, memoAdapter: MemoAdapter){
        repository.deleteMemo(memo, memoAdapter)
    }

/*    private var allMemos: LiveData<List<MemoItem>> = repository.getAll()

    fun getAll(): LiveData<List<MemoItem>> {
        return allMemos
    }

    fun insert(memo: MemoItem) {
        repository.insert(memo)
    }

    fun deleteAll() {
        repository.deleteAll()
    }*/

    override fun onCleared() {
        super.onCleared()
    }




    fun sendMemo(item: MemoListItem) {
        navigator!!.sendMemo(item)
    }

    fun onDeleteMemoClick(memo: MemoListItem) {
        navigator!!.deleteMemo(memo)
    }

    fun onEditMemoClick(memo: MemoListItem) {
        navigator!!.editMemo(memo)
    }
}
