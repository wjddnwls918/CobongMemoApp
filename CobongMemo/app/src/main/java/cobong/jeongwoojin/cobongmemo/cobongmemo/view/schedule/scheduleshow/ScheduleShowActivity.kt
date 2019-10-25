package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleshow

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityScheduleShowBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class ScheduleShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleShowBinding
    private lateinit var viewmodelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: ScheduleShowViewModel

    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_show)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_schedule_show
        )

        viewmodelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel =
            ViewModelProvider(this, viewmodelFactory).get(ScheduleShowViewModel::class.java).apply {
                binding.viewmodel = this
            }

        viewModel.curSchedule = intent.getParcelableExtra("schedule")

        initMap()
        setupNavigation()
    }

    private fun setupNavigation() {

        viewModel.scheduleDeleteClickEvent.observe(this, EventObserver {
            onScheduleDeleteClick()
        })
    }

    override fun onPause() {

        if (mapView != null) {
            if (binding.flPlaceImage.size > 0)
                binding.flPlaceImage.removeAllViews()

            mapView?.removeAllPOIItems()
        }


        super.onPause()
    }

    fun initMap() {

        when {
            viewModel.curSchedule.x != -1.0 -> {

                if (mapView == null)
                    mapView = MapView(this)

                extendMapView()

                val curPoint =
                    MapPoint.mapPointWithGeoCoord(viewModel.curSchedule.y, viewModel.curSchedule.x)

                mapView?.setMapCenterPoint(curPoint, true);
                mapView?.setZoomLevel(2, true);
                mapView?.addPOIItem(getMarker(curPoint))


                binding.flPlaceImage.addView(mapView)

            }
        }

    }

    fun onScheduleDeleteClick() {
        AlertDialog.Builder(this@ScheduleShowActivity).setTitle("확인")
            .setMessage("일정을 지우시겠습니까?")
            .setNegativeButton("확인") { _, _ ->

                deleteAlarm()
                viewModel.deleteSchedule()

                finish()
            }
            .setPositiveButton("취소", null)
            .create().show()

    }

    fun deleteAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val sender =
            PendingIntent.getBroadcast(
                applicationContext,
                viewModel.curSchedule.index,
                MemoApplication.intent,
                FLAG_ONE_SHOT
            )

        alarmManager.cancel(sender)
        sender.cancel()
    }

    fun extendMapView() {

        binding.glStartOfMap.setGuidelinePercent(0.8.toFloat())

        binding.glEndOfDescription.setGuidelinePercent(0.9.toFloat())

        binding.glEndOfAlarm.setGuidelinePercent(1.toFloat())
    }


    fun getMarker(curPoint: MapPoint): MapPOIItem {

        return MapPOIItem().apply {
            itemName = "여기"
            tag = 0
            mapPoint = curPoint
            markerType = MapPOIItem.MarkerType.RedPin
        }
    }

}
