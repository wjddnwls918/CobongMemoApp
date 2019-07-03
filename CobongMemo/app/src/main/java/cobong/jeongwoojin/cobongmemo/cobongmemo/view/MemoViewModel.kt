package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    var navigator: MemoNavigator? = null
    private var repository: MemoRepository

    val allMemosByRoom: LiveData<List<MemoItem>>

    init {
        repository = MemoRepository.getInstance(application)
        allMemosByRoom = repository.memos
    }

    fun deleteByRoom(memo: MemoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByRoom(memo)
    }

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
