package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo;

import androidx.lifecycle.ViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public class MemoViewModel extends ViewModel {

    MemoNavigator navigator;

    public void sendMemo(MemoListItem item) {
        navigator.sendMemo(item);
    }

    public void onDeleteMemoClick(MemoListItem memo) {
        navigator.deleteMemo(memo);
    }

    public void onEditMemoClick(MemoListItem memo) {
        navigator.editMemo(memo);
    }

    public MemoNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(MemoNavigator navigator) {
        this.navigator = navigator;
    }
}
