package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentScheduleBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.calendar.CalendarDecorator
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.calendar.CalendarViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener


class ScheduleFragment : Fragment(), OnDateSelectedListener {

    lateinit var viewModel: CalendarViewModel
    lateinit var binding: FragmentScheduleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        viewModel = ViewModelProviders.of(this).get(CalendarViewModel::class.java)

        binding.mcvScheduleCalendar.setOnDateChangedListener(this)

        binding.mcvScheduleCalendar.addDecorator(CalendarDecorator(R.color.cobongGray, hashSetOf(CalendarDay.from(2019,7,5) )))


        return binding.root
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        Log.d("checkdate",date.toString())
    }
}
