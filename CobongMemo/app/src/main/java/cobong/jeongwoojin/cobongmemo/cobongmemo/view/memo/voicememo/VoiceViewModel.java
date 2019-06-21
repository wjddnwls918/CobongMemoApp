package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository;

public class VoiceViewModel extends AndroidViewModel {

    VoiceNavigator navigator;

    public VoiceViewModel(Application application) {
            super(application);
    }

    //녹음 시작
    public void onRecordClick() {
        navigator.onRecordClick();
    }

    //녹음 끝
    public void onStopClick() {
        navigator.onStopClick();
    }

    public void insertVoice(String resultDate) {
        MemoRepository.Companion.getInstance(getApplication()).insertVoiceRecord(resultDate);
    }

    //닫기
    public void onExitClick() {
        navigator.onExitClick();
    }


    //재생 시작
    public void onPlayClick() {
        navigator.onPlayClick();
    }

    //정지
    public void onPauseClick() {
        navigator.onPauseClick();
    }



    public VoiceNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(VoiceNavigator navigator) {
        this.navigator = navigator;
    }
}
