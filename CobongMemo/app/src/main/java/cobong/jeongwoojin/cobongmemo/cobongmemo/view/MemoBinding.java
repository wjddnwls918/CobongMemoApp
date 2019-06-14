package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo;

import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import cobong.jeongwoojin.cobongmemo.cobongmemo.R;

public class MemoBinding {

    @BindingAdapter("memoType")
    public static void setMemoType(ImageView view, String memoType) {
        //check memoType
        if (memoType.equals("text")) {
            view.setImageDrawable(view.getResources().getDrawable(R.drawable.text));
        } else if (memoType.equals("handwrite")) {
            view.setImageDrawable(view.getResources().getDrawable(R.drawable.handwriting));
        } else {
            view.setImageDrawable(view.getResources().getDrawable(R.drawable.voice));
        }

    }

}
