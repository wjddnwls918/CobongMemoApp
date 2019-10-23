package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.MainActivity



class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "ALARM_ACTION") {

            WakeLocker.acquire(context)

            val alarmTitle = intent.getStringExtra("ALARM_TITLE")
            val alarmSubTitle = intent.getStringExtra("ALARM_NOTI")
            val alarmNum = intent.getStringExtra("ALARM_NUM")

            val alarmChannelId = context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.channel_id).toString()

            //이동 화면
            val moveIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, moveIntent, 0)

            //Notification 설정
            var builder = NotificationCompat.Builder(context, alarmChannelId)
                .setSmallIcon(cobong.jeongwoojin.cobongmemo.cobongmemo.R.drawable.cobonge)
                .setContentTitle(alarmTitle)
                .setContentText(alarmSubTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setVisibility(VISIBILITY_PUBLIC)
                .setAutoCancel(true)

            //버전에 따라 채널등록
            channelControl(context, alarmChannelId)

            //Show the Notification
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(alarmNum.toInt(), builder.build())
            }

            WakeLocker.release()
        }
    }

    fun channelControl(context: Context, alarmChannelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText =
                context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.channel_description).toString()
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(alarmChannelId, alarmChannelId, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}