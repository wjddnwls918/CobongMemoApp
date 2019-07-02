package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

interface VoiceNavigator {
    open fun onRecordClick() {}
    open fun onStopClick() {}
    open fun onExitClick() {}
    open fun onPlayClick() {}
    open fun onPauseClick() {}
}
