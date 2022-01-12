package net.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.bumptech.glide.Glide
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.recycler
import kotlinx.android.synthetic.main.activity_interactive.*
import net.adaoter.DetailsItem
import net.adaoter.InteractiveItem
import net.entity.DataEntity
import net.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class DetailsActivity : AppCompatActivity() {
    private var bigEntity: DataEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        EventBus.getDefault().register(this)
        val i = intent
        bigEntity = i.getSerializableExtra("data") as DataEntity
        Glide.with(this).load(bigEntity!!.imageUrl).into(img)
        val key = bigEntity!!.key
        val url = "https://www.google.com/streetview/feed/gallery/collection/$key.json"
        val params = RequestParams(url)
        x.http().get(params, object : Callback.CommonCallback<String> {
            override fun onSuccess(result: String?) {
                val data = ArrayList<DataEntity>()
                val map: Map<String, DataEntity> =
                    JSON.parseObject(
                        result.toString(),
                        object : TypeReference<Map<String, DataEntity>>() {})
                val m: Set<Map.Entry<String, DataEntity>> = map.entries
                val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
                do {
                    val en: Map.Entry<String, DataEntity> = it.next()
                    val json = JSON.toJSON(en.value)
                    val entity1: DataEntity =
                        JSON.parseObject(json.toString(), DataEntity::class.java)
                    entity1.pannoId = entity1.panoid
                    if (bigEntity!!.fife) {
                        entity1.imageUrl =
                            "https://lh4.googleusercontent.com/" + entity1.pannoId + "/w400-h300-fo90-ya0-pi0/"
                    } else {
                        entity1.imageUrl =
                            "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity1.panoid
                    }
                    data.add(entity1)
                } while (it.hasNext())
                val adapter = EasyRecyclerAdapter<DataEntity>(this@DetailsActivity,
                    DetailsItem::class.java,data)
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
                recycler.layoutManager = staggeredGridLayoutManager
                recycler.adapter = adapter
//                adapter.setOnItemClickListener { _, _, item ->
//                    Glide.with(this@DetailsActivity).load(item.imageUrl).into(img)
//                }
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                TastyToast.makeText(
                    this@DetailsActivity,
                    "error",
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                )
            }

            override fun onCancelled(cex: Callback.CancelledException?) {
            }

            override fun onFinished() {
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e:MessageEvent){
        val msg = e.getMessage()
        if (msg[0].toString() == "click"){
            val e = msg[1] as DataEntity
            Glide.with(this@DetailsActivity).load(e.imageUrl).into(img)
        }
    }
}