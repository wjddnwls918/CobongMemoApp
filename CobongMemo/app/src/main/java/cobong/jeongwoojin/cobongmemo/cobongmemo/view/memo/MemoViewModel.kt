package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    //메모 보기
    private val _openMemoEvent = MutableLiveData<Event<MemoItem>>()
    val openMemoEvent: LiveData<Event<MemoItem>> = _openMemoEvent

    //삭제
    private val _deleteMemoEvent = MutableLiveData<Event<MemoItem>>()
    val deleteMemoEvent: LiveData<Event<MemoItem>> = _deleteMemoEvent

    //수정
    private val _editMemoEvent = MutableLiveData<Event<MemoItem>>()
    val editMemoEvent: LiveData<Event<MemoItem>> = _editMemoEvent

    val items: LiveData<List<MemoItem>>

    init {
        items = MemoApplication.memoRepository.memos
    }

    fun deleteByRoom(memo: MemoItem) = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.deleteByRoom(memo)
    }


    /*fun sendMemo(item: MemoItem) {
        _openMemoEvent.value = Event(item)
        navigator!!.sendMemo(item)
    }*/

    fun onMemoClick(memo: MemoItem) {
        _openMemoEvent.value = Event(memo)
    }

    fun onDeleteMemoClick(memo: MemoItem) {
        _deleteMemoEvent.value = Event(memo)
    }

    fun onEditMemoClick(memo: MemoItem) {
        _editMemoEvent.value = Event(memo)
    }

}
