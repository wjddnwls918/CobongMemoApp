package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleadd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityScheduleAddBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.placeInfo.PlaceInfoActivity
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.SimpleDateFormat
import java.util.*


class ScheduleAddActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var binding: ActivityScheduleAddBinding
    private lateinit var viewmodelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: ScheduleAddViewModel

    private var dialogListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ -> finish() }

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_schedule_add
        )

        viewmodelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel =
            ViewModelProvider(this, viewmodelFactory).get(ScheduleAddViewModel::class.java).apply {
                binding.viewmodel = this
            }


        binding.tietInputPlace.setOnTouchListener(this)

        initToolbar()

        initObservePlace()
        initPlaceEditTextListener()

        setupNavigation()

    }

    private fun setupNavigation() {

        viewModel.setAlarmClickEvent.observe(this, EventObserver {
            onSetAlarmClick()
        })

        viewModel.scheduleWriteFinishClickEvent.observe(this, EventObserver {
            onScheduleWriteFinishClick()
        })

        viewModel.dateClickEvent.observe(this, EventObserver {
            onDateClick()
        })

        viewModel.startTimeSettingClickEvent.observe(this, EventObserver {
            onStartTimeSettingClick()
        })
        viewModel.endTimeSettingClickEvent.observe(this, EventObserver {
            onEndTimeSettingClick()
        })
    }

    fun initToolbar() {
        setSupportActionBar(binding.tbSchedule);

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return false
    }


    fun initObservePlace() {

        viewModel.document.observe(this, androidx.lifecycle.Observer {

            it.run {
                if (!(viewModel.document.value == null)) {
                    extendMapView()

                    val curPoint =
                        MapPoint.mapPointWithGeoCoord(this.y.toDouble(), this.x.toDouble())
                    mapView.setMapCenterPoint(curPoint, true);
                    mapView.setZoomLevel(2, true);
                    mapView.addPOIItem(getMarker(curPoint))

                    binding.flPlaceImage.addView(mapView)

                    viewModel.place.set(this.place_name + "/" + this.address_name)
                    //binding.tietInputPlace.setText(this.place_name + "/" + this.address_name)
                } else {
                    if (binding.flPlaceImage.size > 0)
                        binding.flPlaceImage.removeAllViews()

                    mapView.removeAllPOIItems()

                    reduceMapView()

                }
            }

        })

    }


    fun getMarker(curPoint: MapPoint): MapPOIItem {

        return MapPOIItem().apply {
            itemName = "여기"
            tag = 0
            mapPoint = curPoint
            markerType = MapPOIItem.MarkerType.RedPin
        }
    }

    fun initPlaceEditTextListener() {

        binding.tietInputPlace.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!(binding.tietInputPlace.text?.toString().equals(viewModel.place.get()))) {
                        reduceMapView()
                        if (binding.flPlaceImage.size > 0)
                            binding.flPlaceImage.removeAllViews()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }
            }
        )
    }

    fun extendMapView() {

        binding.glStartOfMap.setGuidelinePercent(0.8.toFloat())

        binding.glEndOfDescription.setGuidelinePercent(0.9.toFloat())

        binding.glEndOfAlarm.setGuidelinePercent(1.toFloat())
    }

    fun reduceMapView() {

        binding.glStartOfMap.setGuidelinePercent(0.5.toFloat())

        binding.glEndOfDescription.setGuidelinePercent(0.6.toFloat())

        binding.glEndOfAlarm.setGuidelinePercent(0.7.toFloat())
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val DRAWABLE_RIGHT = 2

        if (event?.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (binding.tietInputPlace.getRight() - binding.tietInputPlace.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                KeyBoardUtil.hideSoftKeyboard(binding.root, this)

                val intent = Intent(this, PlaceInfoActivity::class.java)
                startActivityForResult(intent, 100)

                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 101) {

            mapView = MapView(this)

            mapView.removeAllPOIItems()

            viewModel.document.value = data?.getParcelableExtra("result")

        }
    }


    override fun onBackPressed() {

        AlertDialog.Builder(this).apply {
            setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?")
            setPositiveButton("확인", dialogListener)
            setNegativeButton("취소", null)
        }.create().apply {
            setCanceledOnTouchOutside(false)
            show()
        }

    }

    fun onScheduleWriteFinishClick() {

        KeyBoardUtil.hideSoftKeyboard(binding.root, this)

        if (inputCheck()) {

            var y: Double = -1.0
            var x: Double = -1.0

            if (binding.tietInputPlace.text?.toString().equals(viewModel.place.get())) {
                y = viewModel.document.value?.y!!.toDouble()
                x = viewModel.document.value?.x!!.toDouble()
            }

            viewModel.insertScheduleByRoom(
                binding.tietInputSchedule.text.toString(),
                binding.tietInputDate.text.toString(),
                binding.tietInputStartTime.text.toString(),
                binding.tietInputEndTime.text.toString(),
                binding.tietInputPlace.text.toString(),
                binding.tietInputDescription.text.toString(),
                viewModel.alarmType.value ?: 0,
                y,
                x
            )

            finish()

        } else
            SnackBarUtil.showSnackBar(binding.root, resources.getString(R.string.input_check))


        //finish()
    }

    fun inputCheck(): Boolean {

        if (binding.tietInputSchedule.text.toString().equals("") ||
            binding.tietInputDate.text.toString().equals("") ||
            binding.tietInputStartTime.text.toString().equals("") ||
            binding.tietInputEndTime.text.toString().equals("") ||
            binding.tietInputPlace.text.toString().equals("") ||
            binding.tietInputDescription.text.toString().equals("") ||
            binding.tietInputAlarm.text.toString().equals("")
        )
            return false

        return true
    }


    fun onSetAlarmClick() {

        val alarmType = arrayOf<CharSequence>(
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_ontime).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_ten_minutes_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_thirty_minutes_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_hours_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_two_hours_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_day_ago).toString()
        )

        android.app.AlertDialog.Builder(this).apply {
            setTitle(resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.set_alarm))
            setItems(alarmType) { _, which ->
                when (which) {

                    which -> {
                        viewModel.alarmType.value = which
                        binding.tietInputAlarm.setText(alarmType.get(which))
                    }
                }
            }

        }.create().apply { show() }


    }


    fun onDateClick() {
        showDatePicker()
    }

    fun onStartTimeSettingClick() {
        showTimePicker("start")

    }

    fun onEndTimeSettingClick() {
        showTimePicker("end")
    }

    fun showDatePicker() {

        val cal: Calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, date ->


                var tempMonth: String

                if (month + 1 < 10) {
                    tempMonth = "0" + (month + 1)
                } else {
                    tempMonth = (month + 1).toString()
                }

                var tempDate: String
                if (date < 10) {
                    tempDate = "0" + date
                } else {
                    tempDate = date.toString()
                }

                val temp = year.toString() + "-" + tempMonth + "-" + tempDate

                viewModel.date.set(temp)

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
        ).apply {
            datePicker.minDate = Date().time
            show()
        }

    }

    fun showTimePicker(format: String) {
        val cal: Calendar = Calendar.getInstance()

        TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, min ->
                when (format) {
                    "start" -> {

                        checkDate(
                            hour,
                            min,
                            viewModel.startTime,
                            viewModel.endTime,
                            "시작 시간을 다시 입력해주세요",
                            { date, hour, min ->

                                when {
                                    (date.hours > hour) -> true

                                    (date.hours == hour) -> when {
                                        date.minutes < min ->
                                            false
                                        else ->
                                            true
                                    }
                                    else -> false
                                }


                            })
                    }

                    "end" -> {

                        checkDate(
                            hour,
                            min,
                            viewModel.endTime,
                            viewModel.startTime,
                            "종료 시간을 다시 입력해주세요",
                            { date, hour, min ->

                                when {
                                    (date.hours < hour) -> true

                                    (date.hours == hour) -> when {
                                        date.minutes > min ->
                                            false
                                        else ->
                                            true
                                    }
                                    else -> false
                                }


                            })

                    }
                }

            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(this)
        ).show()

    }

    fun checkDate(
        hour: Int,
        min: Int,
        checkDate: ObservableField<String>,
        compareDate: ObservableField<String>,
        errorMsg: String,
        ifString: (Date, Int, Int) -> Boolean
    ) {

        var tempHour: String

        if (hour < 10) {
            tempHour = "0" + hour
        } else {
            tempHour = hour.toString()
        }

        var tempMinute: String
        if (min < 10) {
            tempMinute = "0" + min
        } else {
            tempMinute = min.toString()
        }

        val transTime = tempHour + ":" + tempMinute

        if (!(compareDate.get() == null)) {

            var compareTransDate: Date = SimpleDateFormat("HH:mm")
                .parse(compareDate.get())

            if (!(ifString(compareTransDate, hour, min))) {
                SnackBarUtil.showSnackBar(binding.root, errorMsg)

            } else {
                checkDate.set(transTime)

            }

        } else {
            checkDate.set(transTime)

        }

    }

}
