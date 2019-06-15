package cobong.jeongwoojin.cobongmemo.cobongmemo.view;

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public interface MemoNavigator {

    default void sendMemo(MemoListItem item){};
    default void deleteMemo(MemoListItem memo){};
    default void editMemo(MemoListItem memo){};
}
