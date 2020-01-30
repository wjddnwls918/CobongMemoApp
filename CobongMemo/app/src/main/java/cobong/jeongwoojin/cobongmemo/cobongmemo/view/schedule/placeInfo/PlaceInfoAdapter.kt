package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.placeInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BaseMutableRecyclerviewAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ItemPlaceInfoBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document


class PlaceInfoAdapter(dataSet: MutableList<Document>, private val viewModel: PlaceInfoViewModel) : BaseMutableRecyclerviewAdapter<Document,PlaceInfoAdapter.ViewHodler>(dataSet) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
        val binding = ItemPlaceInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        binding.viewmodel = viewModel

        return ViewHodler(binding)
    }

    override fun onBindView(holder: ViewHodler, position: Int) {
        holder.binding.document = getItem(position)
    }

    class ViewHodler(var binding: ItemPlaceInfoBinding) : RecyclerView.ViewHolder(binding.root)

}