package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BaseMutableRecyclerviewAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ItemScheduleBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem

class ScheduleAdapter(
    dataSet: MutableList<ScheduleItem>,
    private val viewModel: ScheduleViewModel
) : BaseMutableRecyclerviewAdapter<ScheduleItem, ScheduleAdapter.ViewHodler>(dataSet) {

    private var listener: ScheduleClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ViewHodler {

        val binding =
            ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        listener = object :
            ScheduleClickListener {
            override fun onScheduleClick(item: ScheduleItem) {
               viewModel.onScheduleClick(item)
            }
        }

        binding.listener = listener

        return ViewHodler(binding)
    }

    override fun onBindView(holder: ScheduleAdapter.ViewHodler, position: Int) {
        holder.binding.schedule = getItem(position)
    }

    class ViewHodler(var binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)


}