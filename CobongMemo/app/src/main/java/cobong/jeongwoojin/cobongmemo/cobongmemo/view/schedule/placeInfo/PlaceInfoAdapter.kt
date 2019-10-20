package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.placeInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BaseMutableRecyclerviewAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.databinding.ItemPlaceInfoBinding
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document


class PlaceInfoAdapter(dataSet: MutableList<Document>, private val viewModel: PlaceInfoViewModel) : BaseMutableRecyclerviewAdapter<Document,PlaceInfoAdapter.ViewHodler>(dataSet) {

    private var listener: PlaceInfoNavigator? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHodler {
        val binding = ItemPlaceInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        //선언하고
        listener = object :
            PlaceInfoNavigator {
            override fun onDocumentClick(document: Document) {
                viewModel.onDocumentClick(document)
            }
        }

        //아이템 레이아웃에 뷰모델,리스너 등록
        binding.listener = listener

        return ViewHodler(binding)
    }

    override fun onBindView(holder: ViewHodler, position: Int) {
        holder.binding.document = getItem(position)
    }


    class ViewHodler(var binding: ItemPlaceInfoBinding) : RecyclerView.ViewHolder(binding.root)

}