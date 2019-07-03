package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem

interface MemoNavigator {
    fun sendMemo(item: MemoItem) {}
    fun deleteMemo(memo: MemoItem) {}
    fun editMemo(memo: MemoItem) {}
}
