package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem

interface MemoNavigator {

    open fun sendMemo(item: MemoListItem) {}
    open fun deleteMemo(memo: MemoListItem) {}
    open fun editMemo(memo: MemoListItem) {}
}
