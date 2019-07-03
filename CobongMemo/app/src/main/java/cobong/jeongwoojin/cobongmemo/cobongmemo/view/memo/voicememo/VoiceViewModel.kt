package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    var item: MemoItem? = null
    lateinit var navigator: VoiceNavigator

    //녹음 시작
    fun onRecordClick() {
        navigator.onRecordClick()
    }

    //녹음 끝
    fun onStopClick() {
        navigator.onStopClick()
    }

    fun insertVoiceMemoByRoom(resultDate:String) = viewModelScope.launch(Dispatchers.IO) {
        MemoRepository.getInstance(getApplication()).insertByRoom(
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

    //닫기
    fun onExitClick() {
        navigator.onExitClick()
    }


    //재생 시작
    fun onPlayClick() {
        navigator.onPlayClick()
    }

    //정지
    fun onPauseClick() {
        navigator.onPauseClick()
    }
}
