package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    var navigator: MemoNavigator? = null
    private var repository: MemoRepository

    lateinit var allMemos: MutableList<MemoListItem>

    val allMemosByRoom: LiveData<List<MemoItem>>

    init {
        repository = MemoRepository.getInstance(application)
        allMemosByRoom = repository.memos
    }

    fun insertByRoom(memo: MemoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertByRoom(memo)
    }

    fun deleteByRoom(memo: MemoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByRoom(memo)
    }



/*


    private lateinit var memoAdapter: MemoAdapter

    fun initAdapter() {
        memoAdapter = MemoAdapter(ArrayList(), this)
    }

    fun getAdapter(): MemoAdapter {
        return memoAdapter
    }
*/

/*
    //get allmemos
    fun getAllMemos() {
        allMemos = repository.getAllMemo()
    }

    //delete memo
    fun deleteMemo (memo: MemoListItem, memoAdapter: MemoAdapter){
        repository.deleteMemo(memo, memoAdapter)
    }*/

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


fun sendMemo(item: MemoItem) {
    navigator!!.sendMemo(item)
}

fun onDeleteMemoClick(memo: MemoItem) {
    navigator!!.deleteMemo(memo)
}

fun onEditMemoClick(memo: MemoItem) {
    navigator!!.editMemo(memo)
}
}
