package cobong.jeongwoojin.cobongmemo.cobongmemo.common.util

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

import com.google.android.material.snackbar.Snackbar

object SnackBarUtil {

    fun showSnackBar(parentLayout: View, text: String) {
        val snack = Snackbar.make(parentLayout, text, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    fun showSnackBar(parentLayout: View, resId: Int) {
        val snack = Snackbar.make(
            parentLayout,
            parentLayout.resources.getText(resId).toString(),
            Snackbar.LENGTH_SHORT
        )
        snack.show()
    }

    fun showSnackBartoTop(parentLayout: View, resId: Int) {
        val snack = Snackbar.make(
            parentLayout,
            parentLayout.resources.getText(resId).toString(),
            Snackbar.LENGTH_SHORT
        )
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack.show()
    }
}
