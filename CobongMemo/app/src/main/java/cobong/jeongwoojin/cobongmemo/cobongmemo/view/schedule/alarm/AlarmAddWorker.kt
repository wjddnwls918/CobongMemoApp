package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication.Companion.intent
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.CalendarUtil.setTime

class AlarmAddWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.

        val title = inputData.getString("title")
        val dateArray = inputData.getStringArray("dateArray")
        val startTimeArray = inputData.getStringArray("startTimeArray")
        val requestCode = inputData.getInt("requestCode", 0)
        val alarmType = inputData.getInt("alarmType", 0)

        // Get AlarmManager instance
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        intent.putExtra(
            "ALARM_TITLE",
            title + "/" + dateArray!![0] + "-" + dateArray[1] + "-" + dateArray[2] + " " + startTimeArray!![0] + ":" + startTimeArray[1]
        )
        intent.putExtra(
            "ALARM_NOTI",
            returnAlarmString(alarmType)
        )

        intent.putExtra("ALARM_NUM", requestCode.toString())


        val pendingIntent =
            PendingIntent.getBroadcast(
                applicationContext,
                requestCode,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )


        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            setTime(dateArray, startTimeArray, alarmType).timeInMillis,
            //alarmTimeAtUTC,
            pendingIntent
        )

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }


    fun returnAlarmString(alarmType: Int): String {
        return when (alarmType) {
            0 -> applicationContext.resources?.getString(R.string.alarm_ontime).toString()
            1 -> applicationContext.resources?.getString(R.string.alarm_ten_minutes_ago).toString()
            2 -> applicationContext.resources?.getString(R.string.alarm_thirty_minutes_ago).toString()
            3 -> applicationContext.resources?.getString(R.string.alarm_one_hours_ago).toString()
            4 -> applicationContext.resources?.getString(R.string.alarm_two_hours_ago).toString()
            else -> applicationContext.resources?.getString(R.string.alarm_one_day_ago).toString()
        }
    }

}