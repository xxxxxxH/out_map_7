package net.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_nearby.*
import net.entity.NearbyEntity
import net.idik.lib.slimadapter.SlimAdapter
import net.utils.ResUtils

class NearbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby)
        val data = ResUtils.getFileFromAssets(this)
        val adapter =
            SlimAdapter.create().register<NearbyEntity>(R.layout.item_near) { data, injector ->
                val itemIv = injector.findViewById<ImageView>(R.id.itemIv)
                Glide.with(this)
                    .load(data.img)
                    .into(itemIv)
                injector.text(R.id.itemTv, data.name.substring(0, data.name.length - 4))
                    .clicked(R.id.card) {
                        try {
                            val i = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "http://maps.google.com/maps?q=${
                                        data.name.substring(
                                            0,
                                            data.name.length - 4
                                        )
                                    }&hl=en"
                                )
                            )
                            i.setPackage("com.google.android.apps.maps")
                            startActivity(i)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            TastyToast.makeText(
                                this,
                                "Something Wrong",
                                TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR
                            )
                        }
                    }
            }
        adapter.attachTo(recycler)
        adapter.updateData(data)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recycler.layoutManager = staggeredGridLayoutManager
    }

    private fun getScreenHeight(): Int {
        val display = this.windowManager.defaultDisplay
        return display.height
    }

    private fun getScreenWidth(): Int {
        val display = this.windowManager.defaultDisplay
        return display.width
    }
}