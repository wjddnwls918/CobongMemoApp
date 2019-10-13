package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentScheduleBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener


class ScheduleFragment : Fragment(), OnDateSelectedListener, ScheduleNavigator {

    lateinit var viewModel: ScheduleViewModel
    lateinit var binding: FragmentScheduleBinding
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
        viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)


        viewModel.navigator = this
        binding.viewmodel = viewModel

        initScheduleRecyclerView()
        initScheduleObserveLivedata()

        binding.mcvScheduleCalendar.setOnDateChangedListener(this)
        //binding.mcvScheduleCalendar.addDecorator(CalendarDecorator(R.color.cobongGray, hashSetOf(CalendarDay.from(2019,7,5) )))


        return binding.root
    }

    fun initScheduleRecyclerView() {
        scheduleAdapter = ScheduleAdapter(arrayListOf(), viewModel)

        binding.rcvScheduleList.layoutManager = LinearLayoutManager(context)
        binding.rcvScheduleList.adapter = scheduleAdapter

    }


    fun initScheduleObserveLivedata() {
        viewModel.allSchedulesByRoom.observe(this, Observer { schedules ->
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

        viewModel.setClickDate(month,day)
        viewModel.getAllScheduleByDate(transDate)

        Log.d(
            "checkdate",
            transDate + " total date size : " + viewModel.allSchedulesByRoom.value?.size
        )
    }

    override fun onAddScheduleStartClick() {
        val intent: Intent = Intent(context, ScheduleAddActivity::class.java)
        startActivity(intent)
    }

    override fun onScheduleClick(schedule: ScheduleItem) {
        
    }
}
