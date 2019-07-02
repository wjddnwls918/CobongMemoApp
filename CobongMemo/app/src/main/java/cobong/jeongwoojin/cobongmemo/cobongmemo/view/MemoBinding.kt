package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import androidx.databinding.BindingAdapter
import android.widget.ImageView

import cobong.jeongwoojin.cobongmemo.cobongmemo.R

object MemoBinding {

    @BindingAdapter("memoType")
    fun setMemoType(view: ImageView, memoType: String) {
        //check memoType
        if (memoType == "text") {
            view.setImageDrawable(view.resources.getDrawable(R.drawable.text))
        } else if (memoType == "handwrite") {
            view.setImageDrawable(view.resources.getDrawable(R.drawable.handwriting))
        } else {
            view.setImageDrawable(view.resources.getDrawable(R.drawable.voice))
        }

    }

}
