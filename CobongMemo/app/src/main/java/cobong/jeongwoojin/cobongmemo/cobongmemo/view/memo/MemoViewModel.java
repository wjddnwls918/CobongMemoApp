package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public class MemoViewModel extends ViewModel {

    public void sendMemo(MemoListItem item) {
        Log.d("arrive check", "hi~");
    }

}
