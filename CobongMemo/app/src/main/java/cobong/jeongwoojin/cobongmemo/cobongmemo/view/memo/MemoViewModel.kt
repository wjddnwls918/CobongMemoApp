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

    val adapter: MemoAdapter

    //메모 보기
    private val _openMemoEvent = MutableLiveData<Event<MemoItem>>()
    val openMemoEvent: LiveData<Event<MemoItem>> = _openMemoEvent

    //삭제
    private val _deleteMemoEvent = MutableLiveData<Event<MemoItem>>()
    val deleteMemoEvent: LiveData<Event<MemoItem>> = _deleteMemoEvent

    //수정
    private val _editMemoEvent = MutableLiveData<Event<MemoItem>>()
    val editMemoEvent: LiveData<Event<MemoItem>> = _editMemoEvent

    val _alertFlag = MutableLiveData<Boolean>()

    //스와이프 완료시
    val _swipedEvent = MutableLiveData<Event<Int>>()
    val swipedEvent: LiveData<Event<Int>> = _swipedEvent

    private val _items = MutableLiveData<List<MemoItem>>(listOf())
    var items: LiveData<List<MemoItem>> = _items

    init {
        adapter = MemoAdapter(this)
        items = MemoApplication.memoRepository.memos
    }

    fun deleteByRoom(memo: MemoItem) = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.deleteByRoom(memo)
    }

    fun deleteByListPosition(listPosition: Int) = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.deleteByRoom(items.value!![listPosition])
    }

    fun onMemoClick(memo: MemoItem) {
        _openMemoEvent.value = Event(memo)
    }

    fun onDeleteMemoClick(memo: MemoItem) {
        _deleteMemoEvent.value = Event(memo)
    }

    fun onEditMemoClick(memo: MemoItem) {
        _editMemoEvent.value = Event(memo)
    }

    fun setFilteredList(filter: String) {

        val filteredList = mutableListOf<MemoItem>()

        val regex = Regex(pattern = filter)
        var matched: Boolean

        for (i in items.value!!.indices) {
            val cur = items.value!![i].title as CharSequence
            matched = regex.containsMatchIn(input = cur)

            if (matched)
                filteredList.add(items.value!![i])
        }

        adapter.submitList(filteredList.toList())
    }

}
