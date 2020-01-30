package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class HandwriteViewModel(application: Application) : AndroidViewModel(application) {

    var item: MemoItem

    //메모 삭제
    private val _deleteMemoEvent = MutableLiveData<Event<Unit>>()
    val deleteMemoEvent: LiveData<Event<Unit>> = _deleteMemoEvent

    //뒤로가기
    private val _exitClickEvent = MutableLiveData<Event<Unit>>()
    val exitClickEvent: LiveData<Event<Unit>> = _exitClickEvent

    //작성 완료
    private val _writeClickEvent = MutableLiveData<Event<Unit>>()
    val writeClickEvent: LiveData<Event<Unit>> = _writeClickEvent

    val pencilSize = ObservableField(3.toString())

    init {
        item = MemoItem(0,"","","","","","","")
    }

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
        viewModelScope.launch(Dispatchers.IO) {
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

    fun updateHandWriteMemoByRoom(title: String, subTitle: String) {
        item.title = title
        item.subTitle = subTitle

        viewModelScope.launch(Dispatchers.IO) {
            MemoApplication.memoRepository.updateByRoom(
                item
            )
        }
    }

    fun deleteHandwriteMemoByRoom() = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.deleteMemoNullableByRoom(item)
    }

    fun deleteHandwriteInStorage() {
        val path = MemoApplication.root + "/saved_images/" + item.handwriteId + ".jpg"
        val file = File(path)
        Log.d("filedelete", path)
        if (file.exists()) {
            if (file.delete()) {
                Log.d("filedelete", "삭제완료")
            } else {
                Log.d("filedelete", "실패")
            }
        }
    }

}
