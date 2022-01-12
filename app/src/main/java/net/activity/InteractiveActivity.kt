package net.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_interactive.*
import net.adaoter.InteractiveItem
import net.entity.DataEntity
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import uk.co.ribot.easyadapter.EasyAdapter
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class InteractiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interactive)
        val params = RequestParams("https://www.google.com/streetview/feed/gallery/data.json")
        x.http().get(params,object :Callback.CommonCallback<String>{
            override fun onSuccess(result: String?) {
                val data = ArrayList<DataEntity>()
                val map: Map<String, DataEntity> =
                    JSON.parseObject(
                        result,
                        object : TypeReference<Map<String, DataEntity>>() {})
                val m: Set<Map.Entry<String, DataEntity>> = map.entries
                val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
                do {
                    val en: Map.Entry<String, DataEntity> = it.next()
                    val json = JSON.toJSON(en.value)
                    val entity: DataEntity =
                        JSON.parseObject(json.toString(), DataEntity::class.java)
                    entity.key = en.key
                    if (TextUtils.isEmpty(entity.panoid)) {
                        continue
                    } else {
                        if (entity.panoid == "LiAWseC5n46JieDt9Dkevw") {
                            continue
                        }
                    }
                    if (entity.fife) {
                        entity.imageUrl =
                            "https://lh4.googleusercontent.com/" + entity.panoid + "/w400-h300-fo90-ya0-pi0/"
                        continue
                    } else {
                        entity.imageUrl =
                            "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity.panoid
                    }
                    data.add(entity)
                } while (it.hasNext())

                val adapter = EasyRecyclerAdapter<DataEntity>(this@InteractiveActivity,InteractiveItem::class.java,data)
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(
                    2,
                    StaggeredGridLayoutManager.VERTICAL
                )
                recycler.layoutManager = staggeredGridLayoutManager
                recycler.adapter = adapter
                
            }

            override fun onError(ex: Throwable?, isOnCallback: Boolean) {
                TastyToast.makeText(
                    this@InteractiveActivity,
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
}