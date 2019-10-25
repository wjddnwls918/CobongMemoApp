package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

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

class HandwriteViewModel(application: Application) : AndroidViewModel(application) {

    var item: MemoItem? = null

    //메모 삭제
    private val _deleteMemoEvent = MutableLiveData<Event<Unit>>()
    val deleteMemoEvent: LiveData<Event<Unit>> = _deleteMemoEvent

    //뒤로가기
    private val _exitClickEvent = MutableLiveData<Event<Unit>>()
    val exitClickEvent: LiveData<Event<Unit>> = _exitClickEvent

    //작성 완료
    private val _writeClickEvent = MutableLiveData<Event<Unit>>()
    val writeClickEvent: LiveData<Event<Unit>> = _writeClickEvent


    //뒤로가기
    fun onExitClick() {
        _exitClickEvent.value = Event(Unit)
    }

    //삭제
    fun onDeleteClick() {
        _deleteMemoEvent.value = Event(Unit)
    }

    //작성 완료
    fun onWriteClick() {
        _writeClickEvent.value = Event(Unit)
    }

    fun insertHandWriteMemoByRoom(title: String, subTitle: String, handwriteId: String) {
        viewModelScope.launch (Dispatchers.IO){
            MemoApplication.memoRepository.insertByRoom(
                MemoItem(
                    index = null,
                    title = title,
                    subTitle = subTitle,
                    memoType = "handwrite",
                    content = null,
                    voiceId = null,
                    handwriteId = handwriteId
                )
            )
        }
    }

    fun deleteHandwriteMemoByRoom() = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.deleteMemoNullableByRoom(item)
    }

}
