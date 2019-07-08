package cobong.jeongwoojin.cobongmemo.cobongmemo.common

import androidx.recyclerview.widget.RecyclerView
abstract class BaseRecyclerVIewAdapter<T, VH : RecyclerView.ViewHolder?>(var dataSet: List<T>) :
    RecyclerView.Adapter<VH>() {
    fun getItem(position: Int): T? {
        return if (dataSet.size==0) null else dataSet[position]
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindView(holder, position)
    }

    abstract fun onBindView(holder: VH, position: Int)

    override fun getItemCount(): Int {
        return dataSet.size
    }

    /**
     * Set items.
     */
    fun setItem(items: List<T>) {
        this.dataSet = items
        notifyDataSetChanged()
    }


    /**
     * Get items.
     */

    fun getItemPosition(item: T): Int {
        return dataSet.indexOf(item)
    }


}