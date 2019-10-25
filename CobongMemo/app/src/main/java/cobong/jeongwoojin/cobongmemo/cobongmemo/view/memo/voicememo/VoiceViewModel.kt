package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

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

class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    var item: MemoItem? = null

    //녹음 시작
    private val _recordClickEvent = MutableLiveData<Event<Unit>>()
    val recordClickEvent: LiveData<Event<Unit>> = _recordClickEvent

    //녹음 끝
    private val _recordStopClickEvent = MutableLiveData<Event<Unit>>()
    val recordStopClickEvent: LiveData<Event<Unit>> = _recordStopClickEvent

    //닫기
    private val _exitClickEvent = MutableLiveData<Event<Unit>>()
    val exitClickEvent: LiveData<Event<Unit>> = _exitClickEvent

    //작성 완료
    private val _playClickEvent = MutableLiveData<Event<Unit>>()
    val playClickEvent: LiveData<Event<Unit>> = _playClickEvent

    //작성 완료
    private val _pauseClickEvent = MutableLiveData<Event<Unit>>()
    val pauseClickEvent: LiveData<Event<Unit>> = _pauseClickEvent

    //녹음 시작
    fun onRecordClick() {
        _recordClickEvent.value = Event(Unit)
    }

    //녹음 끝
    fun onStopClick() {
        _recordStopClickEvent.value = Event(Unit)
    }

    //닫기
    fun onExitClick() {
        _exitClickEvent.value = Event(Unit)
    }

    //재생 시작
    fun onPlayClick() {
        _playClickEvent.value = Event(Unit)
    }

    //정지
    fun onPauseClick() {
        _pauseClickEvent.value = Event(Unit)
    }

    fun insertVoiceMemoByRoom(resultDate:String) = viewModelScope.launch(Dispatchers.IO) {
        MemoApplication.memoRepository.insertByRoom(
            MemoItem(
                index = null,
                title = resultDate,
                subTitle = null,
                memoType = "voice",
                content = null,
                voiceId = resultDate,
                handwriteId = null
            )
        )
    }


}
