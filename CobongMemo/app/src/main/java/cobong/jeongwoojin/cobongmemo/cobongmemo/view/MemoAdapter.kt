package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BaseRecyclerVIewAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.MemoItemBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem

class MemoAdapter(list: MutableList<MemoListItem>, private val viewModel: MemoViewModel) :
    BaseRecyclerVIewAdapter<MemoListItem, MemoAdapter.MemoViewHolder>(list) {
    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val binding = MemoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        //선언하고
        listener = object : ItemClickListener {
            override fun onMemoClick(item: MemoListItem) {
                viewModel.sendMemo(item)
            }
        }

        //아이템 레이아웃에 뷰모델,리스너 등록
        binding.listener = listener

        binding.viewmodel = viewModel
        return MemoAdapter.MemoViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.hashCode().toLong()
    }

    override fun onBindView(holder: MemoAdapter.MemoViewHolder, position: Int) {
        holder.binding.memo = getItem(position)
    }

    internal inner class MemoViewHolder(var binding: MemoItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
