package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.R

object MemoBinding {

    @JvmStatic
    @BindingAdapter("memoType")
    fun setMemoType(view: ImageView, memoType: String) {
        //check memoType
        if (memoType == "text") {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_text_format_black_24dp))
        } else if (memoType == "handwrite") {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_palette_black_24dp))
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_keyboard_voice_black_24dp))
        }

    }

}
