package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo;

import androidx.lifecycle.ViewModel;

public class VoiceViewModel extends ViewModel {

    VoiceNavigator navigator;

    //녹음 시작
    public void onRecordClick() {
        navigator.onRecordClick();
    }

    //녹음 끝
    public void onStopClick() {
        navigator.onStopClick();
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
