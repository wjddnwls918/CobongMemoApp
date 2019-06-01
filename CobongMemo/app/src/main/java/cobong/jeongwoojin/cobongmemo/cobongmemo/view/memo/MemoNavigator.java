package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo;

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public interface MemoNavigator {

    default void sendMemo(MemoListItem item){};

}
