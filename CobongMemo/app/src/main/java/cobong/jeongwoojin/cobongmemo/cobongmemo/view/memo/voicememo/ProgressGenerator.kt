package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.voicememo

import android.os.Handler

import com.dd.processbutton.ProcessButton

import java.util.Random

class ProgressGenerator(private val mListener: OnCompleteListener) {
    private var mProgress: Int = 0

    private val random = Random()


    interface OnCompleteListener {

        fun onComplete()
    }

    fun start(button: ProcessButton) {
        val handler = Handler()
        handler.postDelayed({
            mProgress += 10
            if (mProgress == 100)
                mProgress = 0
            button.progress = mProgress

            /*if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }*/
        }, generateDelay().toLong())
    }

    private fun generateDelay(): Int {
        return random.nextInt(1000)
    }

}
