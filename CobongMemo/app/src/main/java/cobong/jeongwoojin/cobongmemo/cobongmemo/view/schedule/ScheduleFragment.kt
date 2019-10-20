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
import androidx.recyclerview.widget.LinearLayoutManager
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentScheduleBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.calendar.CalendarDecorator
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleadd.ScheduleAddActivity
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleshow.ScheduleShowActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener


class ScheduleFragment : Fragment(), OnDateSelectedListener, ScheduleNavigator {

    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: ScheduleViewModel

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScheduleViewModel::class.java).apply {
            navigator = this@ScheduleFragment
            binding.viewmodel = this
        }

        initScheduleRecyclerView()
        initScheduleObserveByDate()
        initScheduleObserve()
        initScheduleCalendar()

        //binding.mcvScheduleCalendar.addDecorator(CalendarDecorator(R.color.cobongGray, hashSetOf(CalendarDay.from(2019,7,5) )))

        return binding.root
    }

    fun initScheduleCalendar() {

        binding.mcvScheduleCalendar.setOnDateChangedListener(this)
    }

    fun initScheduleRecyclerView() {
        scheduleAdapter = ScheduleAdapter(arrayListOf(), viewModel)

        binding.rcvScheduleList.layoutManager = LinearLayoutManager(context)
        binding.rcvScheduleList.adapter = scheduleAdapter

    }


    fun initScheduleObserveByDate() {
        viewModel.allSchedulesByRoomByDate.observe(this, Observer { schedules ->
            schedules.let {
                scheduleAdapter.setItem(it.toMutableList())
            }
        })
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

    override fun onAddScheduleStartClick() {
        startActivity(Intent(context, ScheduleAddActivity::class.java))
    }

    override fun onScheduleClick(schedule: ScheduleItem) {
        startActivity(
            Intent(
                context,
                ScheduleShowActivity::class.java
            ).apply { this.putExtra("schedule", schedule) })
    }

    fun initScheduleObserve() {
        viewModel.allSchedulesByRoom.observe(this, Observer { memos ->
            memos.let {

                if (!viewModel.transDate.equals(""))
                    viewModel.getAllScheduleByDate(viewModel.transDate)

                binding.mcvScheduleCalendar.removeDecorators()
                val set = HashSet<CalendarDay>()
                for (i in it.indices) {
                    val trans = it[i].date.split("-")
                    set.add(CalendarDay.from(trans[0].toInt(), trans[1].toInt(), trans[2].toInt()))
                }
                //달력 표시
                binding.mcvScheduleCalendar.addDecorator(CalendarDecorator(R.color.cobongRed, set))

            }
        })
    }

}
