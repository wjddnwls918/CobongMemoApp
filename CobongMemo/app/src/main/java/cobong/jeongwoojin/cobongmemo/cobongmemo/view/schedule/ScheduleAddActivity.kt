package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.KeyBoardUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.SnackBarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ActivityScheduleAddBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.placeInfo.PlaceInfoActivity
import java.text.SimpleDateFormat
import java.util.*


class ScheduleAddActivity : AppCompatActivity(), ScheduleNavigator, View.OnTouchListener {

    lateinit var binding: ActivityScheduleAddBinding
    lateinit var viewModel: ScheduleViewModel

    var dialogListener: DialogInterface.OnClickListener =
        DialogInterface.OnClickListener { _, _ -> finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            cobong.jeongwoojin.cobongmemo.cobongmemo.R.layout.activity_schedule_add
        )

        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)

        binding.viewmodel = viewModel

        viewModel.navigator = this


        binding.tietInputPlace.setOnTouchListener(this)

        initToolbar()

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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val DRAWABLE_LEFT = 0;
        val DRAWABLE_TOP = 1;
        val DRAWABLE_RIGHT = 2;
        val DRAWABLE_BOTTOM = 3;

        if (event?.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (binding.tietInputPlace.getRight() - binding.tietInputPlace.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                KeyBoardUtil.hideSoftKeyboard(binding.root, this)
                //SnackBarUtil.showSnackBar(binding.root, "검색 버튼 클릭")

                val intent = Intent(this, PlaceInfoActivity::class.java)
                startActivityForResult(intent,100)

                return true;
            }
        }
        return false;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 100 && resultCode == 101) {
            val placeName = data?.getStringExtra("resultPlaceName")
            val addressName = data?.getStringExtra("resultAddressName")

            viewModel.place.set(placeName+"/"+addressName)
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

    override fun onScheduleWriteFinishClick() {
        finish()
    }

    override fun onSetAlarmClick() {

        val alarmType = arrayOf<CharSequence>(
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_ontime).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_five_minutes_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_ten_minutes_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_fifteen_minutes_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_thirty_minutes_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_hours_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_two_hours_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_three_hours_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_twelve_hours_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_day_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_two_days_ago).toString(),
            resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_week_ago).toString()
        )

        android.app.AlertDialog.Builder(this).apply {
            setTitle(resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.set_alarm))
            setItems(alarmType) { dialog, which ->
                when (which) {

                    which -> {
                        binding.tietInputAlarm.setText(alarmType.get(which))
                    }
                }
            }

        }.create().apply { show() }


    }


    override fun onDateClick() {
        showDatePicker()
    }

    override fun onStartTimeSettingClick() {
        showTimePicker("start")

    }

    override fun onEndTimeSettingClick() {
        showTimePicker("end")
    }

    fun showDatePicker() {

        val cal: Calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, date ->
                viewModel.date.set(String.format("%d년 %d월 %d일", year, month + 1, date))
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
            TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                when (format) {
                    "start" -> {

                        checkDate(
                            hour,
                            min,
                            viewModel.startTime,
                            viewModel.endTime,
                            "시작 시간을 다시 입력해주세요",
                            { date, hour, min ->
                                (date.hours >= hour && date.minutes >= min)
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
                                (date.hours <= hour && date.minutes <= min)
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

        if (!(compareDate.get() == null)) {

            var compareTransDate: Date = SimpleDateFormat("HH시 mm분")
                .parse(compareDate.get().toString())

            if (!(ifString(compareTransDate, hour, min))) {
                SnackBarUtil.showSnackBar(binding.root, errorMsg)

            } else {
                checkDate.set(String.format("%d시 %d분", hour, min))

            }

        } else {
            checkDate.set(String.format("%d시 %d분", hour, min))
        }

    }

}
