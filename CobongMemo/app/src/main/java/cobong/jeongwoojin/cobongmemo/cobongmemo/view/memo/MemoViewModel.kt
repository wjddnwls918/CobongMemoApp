package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.memolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    var navigator: MemoNavigator? = null

    val allMemosByRoom: LiveData<List<MemoItem>>

    init {
        allMemosByRoom = MemoApplication.memoRepository.memos
    }

    fun deleteByRoom(memo: MemoItem) = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.deleteByRoom(memo)
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
