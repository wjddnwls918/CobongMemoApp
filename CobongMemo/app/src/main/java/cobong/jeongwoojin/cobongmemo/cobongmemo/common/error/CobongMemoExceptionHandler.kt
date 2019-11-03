package cobong.jeongwoojin.cobongmemo.cobongmemo.common.error

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.ErrorActivity
import java.io.PrintWriter
import java.io.StringWriter

class CobongMemoExceptionHandler(
    application: Application,
    private val defaultExceptionHandler: Thread.UncaughtExceptionHandler,
    private val fabricExceptionHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {

    private var lastActivity: Activity? = null
    private var activityCount = 0

    init {
        application.registerActivityLifecycleCallbacks(
            object : SimpleActivityLifecycleCallbacks() {

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    lastActivity = activity
                }

                override fun onActivityStarted(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount++
                    lastActivity = activity
                }

                override fun onActivityStopped(activity: Activity) {
                    if (isSkipActivity(activity)) {
                        return
                    }
                    activityCount--
                    if (activityCount < 0) {
                        lastActivity = null
                    }

                }
            })
    }

    private fun isSkipActivity(activity: Activity) = activity is ErrorActivity

    override fun uncaughtException(thread: Thread?, throwable: Throwable) {
        fabricExceptionHandler.uncaughtException(thread, throwable)
        lastActivity?.run {
            val stringWriter = StringWriter()
            throwable.printStackTrace(PrintWriter(stringWriter))

            startErrorActivity(this, stringWriter.toString())
        } ?: defaultExceptionHandler.uncaughtException(thread, throwable)

        Process.killProcess(Process.myPid())
        System.exit(-1)
    }

    private fun startErrorActivity(activity: Activity, errorText: String) = activity.run {
        val errorActivityIntent = Intent(this, ErrorActivity::class.java)
            .apply {
                putExtra(ErrorActivity.EXTRA_INTENT, this)
                putExtra(ErrorActivity.EXTRA_ERROR_TEXT, errorText)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        startActivity(errorActivityIntent)
        finish()
    }

}