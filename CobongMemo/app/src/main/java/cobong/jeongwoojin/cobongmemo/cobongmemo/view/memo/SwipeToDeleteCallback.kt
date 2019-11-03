package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event

class SwipeToDeleteCallback(val applicationContext: Context, val memoViewModel: MemoViewModel) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    val icon: Drawable?
    val background: ColorDrawable

    init {

        icon = ContextCompat.getDrawable(
            applicationContext,
            cobong.jeongwoojin.cobongmemo.cobongmemo.R.drawable.ic_delete_sweep_white_24dp
        )
        background = ColorDrawable(
            ContextCompat.getColor(
                applicationContext,
                cobong.jeongwoojin.cobongmemo.cobongmemo.R.color.cobongRed
            )
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconMargin = (itemView.height - icon!!.getIntrinsicHeight()) / 2
        val iconTop = itemView.top + (itemView.height - icon.getIntrinsicHeight()) / 2
        val iconBottom = iconTop + icon.getIntrinsicHeight()

        when {

            //왼쪽으로
            dX<0 -> {
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            }

            else -> {
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
        icon.draw(c)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        memoViewModel._swipedEvent.value = Event(viewHolder.adapterPosition)
    }
}