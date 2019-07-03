package cobong.jeongwoojin.cobongmemo.cobongmemo.common.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBoardUtil {

    fun hideSoftKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSoftKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}
