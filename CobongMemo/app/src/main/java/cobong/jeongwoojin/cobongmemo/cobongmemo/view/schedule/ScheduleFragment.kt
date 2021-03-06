package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.CalendarUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentScheduleBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm.AlarmAddWorker
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.calendar.CalendarDecorator
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleadd.ScheduleAddActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleshow.ScheduleShowActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import org.threeten.bp.format.DateTimeFormatter


class ScheduleFragment : Fragment(), OnDateSelectedListener {

    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: ScheduleViewModel

    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ScheduleViewModel::class.java).apply {
                binding.viewmodel = this
            }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        initScheduleObserve()
        initScheduleCalendar()
        setupNavigation()

    }

    private fun setupNavigation() {

        viewModel.scheduleClickEvent.observe(this.viewLifecycleOwner, EventObserver {
            onScheduleClick(it)
        })

        viewModel.addScheduleStartClickEvent.observe(this.viewLifecycleOwner, EventObserver {
            onAddScheduleStartClick()
        })
    }

    fun initScheduleCalendar() {

        binding.mcvScheduleCalendar.setOnDateChangedListener(this)
        binding.mcvScheduleCalendar.setTitleFormatter(
            DateFormatTitleFormatter(
                DateTimeFormatter.ofPattern(
                    "yyyy년 MM월"
                )
            )
        )

        onDateSelected(
            binding.mcvScheduleCalendar, CalendarDay.from(
                DateUtil.curYear().toInt(),
                DateUtil.curMonth().toInt(),
                DateUtil.curDay().toInt()
            ), true
        )
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        val year = date.year.toString()

        val month: String
        if (date.month < 10)
            month = ("0" + date.month)
        else
            month = "" + date.month
        val day: String
        if (date.day < 10)
            day = ("0" + date.day)
        else
            day = "" + date.day

        val transDate = year + "-" + month + "-" + day

        viewModel.transDate = transDate
        viewModel.getAllScheduleByDate(transDate)

    }

    override fun onStop() {
        super.onStop()

        binding.mcvScheduleCalendar.clearSelection()
    }

    fun onAddScheduleStartClick() {
        startActivity(Intent(context, ScheduleAddActivity::class.java))
    }

    fun onScheduleClick(schedule: ScheduleItem) {
        startActivity(
            Intent(
                context,
                ScheduleShowActivity::class.java
            ).apply { this.putExtra("schedule", schedule) })
    }

    fun initScheduleObserve() {
        viewModel.allSchedulesByRoom.observe(this.viewLifecycleOwner, Observer { memos ->

            memos?.let {

                viewModel.scheduleAdapter.notifyDataSetChanged()

                if (!viewModel.transDate.equals(""))
                    viewModel.getAllScheduleByDate(viewModel.transDate)

                val work = OneTimeWorkRequest.Builder(AlarmAddWorker::class.java)
                val data = Data.Builder()

                binding.mcvScheduleCalendar.removeDecorators()
                val set = HashSet<CalendarDay>()
                for (i in it.indices) {
                    val dateArray = it[i].date.split("-")
                    val startTimeArray = it[i].startTime.split(":")

                    set.add(
                        CalendarDay.from(
                            dateArray[0].toInt(),
                            dateArray[1].toInt(),
                            dateArray[2].toInt()
                        )
                    )

                    if (CalendarUtil.setTime(
                            dateArray.toTypedArray(),
                            startTimeArray.toTypedArray(),
                            it[i].alarmType
                        ).timeInMillis < System.currentTimeMillis()
                    ) {

                        continue
                    }
                    data.putString("title", it[i].title)
                    data.putStringArray("dateArray", dateArray.toTypedArray())
                    data.putStringArray("startTimeArray", startTimeArray.toTypedArray())
                    data.putInt("requestCode", it[i].index)
                    data.putInt("alarmType", it[i].alarmType)
                    work.setInputData(data.build())
                    WorkManager.getInstance(activity!!.applicationContext).enqueue(work.build())

                }
                //달력 표시
                binding.mcvScheduleCalendar.addDecorator(CalendarDecorator(R.color.cobongRed, set))


            }
        })
    }

}
