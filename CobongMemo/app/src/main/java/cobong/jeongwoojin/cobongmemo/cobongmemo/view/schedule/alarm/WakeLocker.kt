package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.os.PowerManager


object WakeLocker {

    private var wakeLock: PowerManager.WakeLock? = null

    fun acquire(context: Context) {
        if (wakeLock != null) wakeLock!!.release()

        val pm = context.getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "CobongMemo:tag"
        )
        wakeLock!!.acquire()
    }

    fun release() {
        if (wakeLock != null)
            wakeLock!!.release()
        wakeLock = null
    }

}