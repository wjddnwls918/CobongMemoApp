package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo;

public interface VoiceNavigator {
    default public void onRecordClick() {};
    default public void onStopClick() {};
    default public void onExitClick() {};
    default public void onPlayClick() {};
    default public void onPauseClick() {};
}
