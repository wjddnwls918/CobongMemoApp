package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository

class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    var navigator: VoiceNavigator

    //녹음 시작
    fun onRecordClick() {
        navigator.onRecordClick()
    }

    //녹음 끝
    fun onStopClick() {
        navigator.onStopClick()
    }

    fun insertVoice(resultDate: String) {
        MemoRepository.getInstance(getApplication()).insertVoiceRecord(resultDate)
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
