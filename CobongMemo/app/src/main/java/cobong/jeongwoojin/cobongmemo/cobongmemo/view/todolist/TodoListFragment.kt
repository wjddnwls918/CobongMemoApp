package cobong.jeongwoojin.cobongmemo.cobongmemo.view.todolist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.EventObserver
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.FragmentTodoListBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleshow.ScheduleShowActivity

class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var viewModel: TodoListViewModel

    private lateinit var todayTodoListAdapter: TodoListAdapter
    private lateinit var tomorrowTodoListAdapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false)

        viewModelFactory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(TodoListViewModel::class.java).apply {
                binding.viewmodel = this
            }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        initObserveLivedata()
        setupListAdapter()
        setupNavigation()
    }

    private fun initObserveLivedata() {
        viewModel.todayItems.observe(this, Observer { schedules ->
            schedules.let {
                when (it.size) {
                    0 -> binding.tvTodayEmptyList.visibility = View.VISIBLE
                    else -> binding.tvTodayEmptyList.visibility = View.GONE
                }
            }
        })

        viewModel.tomorrowItems.observe(this, Observer { schedules ->
            schedules.let {
                when (it.size) {
                    0 -> binding.tvTomorrowEmptyList.visibility = View.VISIBLE
                    else -> binding.tvTomorrowEmptyList.visibility = View.GONE
                }
            }
        })
    }

    private fun setupListAdapter() {
        if (binding.viewmodel != null) {
            todayTodoListAdapter = TodoListAdapter(viewModel)
            tomorrowTodoListAdapter = TodoListAdapter(viewModel)
            binding.rcvTodayTodoList.adapter = todayTodoListAdapter
            binding.rcvTomorrowTodoList.adapter = tomorrowTodoListAdapter
        }
    }

    private fun setupNavigation() {
        viewModel.openTodoListEvent.observe(this, EventObserver {
            openTaskDetails(it)
        })

    }

    private fun openTaskDetails(schedule: ScheduleItem) {
        startActivity(
            Intent(
                context,
                ScheduleShowActivity::class.java
            ).apply { this.putExtra("schedule", schedule) })
    }

}
