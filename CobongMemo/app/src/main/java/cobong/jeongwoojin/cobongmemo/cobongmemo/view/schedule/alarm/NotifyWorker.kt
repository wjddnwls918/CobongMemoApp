package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.

        //uploadImages()

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}