package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo

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

class TextMemoViewModel(application: Application) : AndroidViewModel(application) {

    var item = MutableLiveData<MemoItem>()

    //메모 수정
    private val _editMemoEvent = MutableLiveData<Event<Unit>>()
    val editMemoEvent: LiveData<Event<Unit>> = _editMemoEvent

    //메모 삭제
    private val _deleteMemoEvent = MutableLiveData<Event<Unit>>()
    val deleteMemoEvent: LiveData<Event<Unit>> = _deleteMemoEvent

    //뒤로가기
    private val _exitClickEvent = MutableLiveData<Event<Unit>>()
    val exitClickEvent: LiveData<Event<Unit>> = _exitClickEvent

    //작성 완료
    private val _writeClickEvent = MutableLiveData<Event<Unit>>()
    val writeClickEvent: LiveData<Event<Unit>> = _writeClickEvent

    //메모 수정
    fun onEditClick() {
        _editMemoEvent.value = Event(Unit)
    }

    //메모 삭제
    fun onDeleteClick() {
        _deleteMemoEvent.value = Event(Unit)
    }

    //뒤로가기
    fun onExitClick() {
        _exitClickEvent.value = Event(Unit)
    }

    //작성완료
    fun onWriteClick() {
        _writeClickEvent.value = Event(Unit)
    }

    fun insertTextMemoByRoom(title: String, subTitle: String, content: String) =
        viewModelScope.launch (Dispatchers.IO){
            MemoApplication.memoRepository.insertByRoom(
                MemoItem(
                    index = null,
                    title = title,
                    subTitle = subTitle,
                    memoType = "text",
                    content = content,
                    voiceId = null,
                    handwriteId = null
                )
            )
        }

    fun updateTextMemoByRoom(title: String, subTitle: String, content: String) =
        viewModelScope.launch (Dispatchers.IO){
            MemoApplication.memoRepository.updateByRoom(
                MemoItem(
                    index = item.value?.index,
                    title = title,
                    subTitle = subTitle,
                    memoType = item.value?.memoType,
                    content = content,
                    voiceId = item.value?.voiceId,
                    handwriteId = item.value?.handwriteId
                )
            )
        }

    fun deleteTextMemoByRoom() =
        viewModelScope.launch (Dispatchers.IO){
            MemoApplication.memoRepository.deleteMemoNullableByRoom(
               item.value
            )
        }

}
