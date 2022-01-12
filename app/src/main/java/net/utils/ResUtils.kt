package net.utils

import android.content.Context
import android.graphics.BitmapFactory
import net.entity.NearbyEntity

object ResUtils {
    fun getFileFromAssets(context: Context): ArrayList<NearbyEntity> {
        val result = ArrayList<NearbyEntity>()
        val assetsManager = context.resources.assets
        val files = assetsManager.list("img")
        if (files!!.isNotEmpty()) {
            files.forEach {
                val inputStream = assetsManager.open("img/$it")
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val e = NearbyEntity(it, bitmap)
                result.add(e)
            }
        }
        return result
    }
}