package net.adaoter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import net.activity.R
import net.entity.DataEntity
import net.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import uk.co.ribot.easyadapter.ItemViewHolder
import uk.co.ribot.easyadapter.PositionInfo
import uk.co.ribot.easyadapter.annotations.LayoutId
import uk.co.ribot.easyadapter.annotations.ViewId

@SuppressLint("NonConstantResourceId")
@LayoutId(R.layout.item_near)
class DetailsItem(view: View) : ItemViewHolder<DataEntity>(view) {
    @ViewId(R.id.itemIv)
    var itemIv: ImageView? = null

    @ViewId(R.id.itemTv)
    var itemTv: TextView? = null

    @ViewId(R.id.card)
    var card: CardView? = null
    override fun onSetValues(item: DataEntity?, positionInfo: PositionInfo?) {
        Glide.with(context).load(item!!.imageUrl).into(itemIv!!)
        itemTv!!.text = item.title
        card?.let {
            it.setOnClickListener {
                EventBus.getDefault().post(MessageEvent("click", item))
            }
        }
    }
}