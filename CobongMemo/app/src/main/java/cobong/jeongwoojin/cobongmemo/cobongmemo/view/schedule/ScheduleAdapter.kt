package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ItemScheduleBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ItemScheduleHeaderBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoViewModel

/**
 * Adapter for the task list. Has a reference to the [MemoViewModel] to send actions back to it.
 */
class ScheduleAdapter(private val viewModel: ScheduleViewModel) :
    ListAdapter<ScheduleItem, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when{
            holder is HeaderViewHolder -> {
                holder.bind(viewModel, getItem(0))
            }
            holder is ViewHolder -> {
                val item = getItem(position)
                holder.bind(viewModel, item)
            }
        }
    }

    /*override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }*/

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> return TYPE_HEADER
            else ->
                return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            TYPE_HEADER -> {
                return HeaderViewHolder.from(parent)
            }
            else -> {
                return ViewHolder.from(parent)
            }
        }

    }

    class ViewHolder private constructor(val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ScheduleViewModel, item: ScheduleItem) {

            binding.viewmodel = viewModel
            binding.schedule = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemScheduleBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder(val binding: ItemScheduleHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ScheduleViewModel, item: ScheduleItem) {
            binding.viewmodel = viewModel
            binding.schedule = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemScheduleHeaderBinding.inflate(layoutInflater, parent, false)

                return HeaderViewHolder(binding)
            }
        }

    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<ScheduleItem>() {
    override fun areItemsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean {
        return oldItem == newItem
    }
}