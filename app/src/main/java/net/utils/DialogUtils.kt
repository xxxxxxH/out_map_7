package utils

import android.content.Context
import android.view.LayoutInflater
import com.yarolegovich.lovelydialog.LovelyCustomDialog
import net.event.MessageEvent
import net.activity.R
import org.greenrobot.eventbus.EventBus


object DialogUtils {
    fun createDlg(context: Context) {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_option, null)
        LovelyCustomDialog(context)
            .setTitle("Select Your Option")
            .setView(v)
            .setListener(R.id.nearby,true) { sendMsg("nearby") }
            .setListener(R.id.streetview,true) { sendMsg("streetview") }
            .setListener(R.id.inter,true) { sendMsg("inter") }
            .setListener(R.id.normal,true) { sendMsg("normal") }
            .setListener(R.id.hybird,true) { sendMsg("hybird") }
            .setListener(R.id.sat,true) { sendMsg("sat") }
            .setListener(R.id.terrain,true) { sendMsg("terrain") }
            .setListener(R.id.search,true) { sendMsg("search") }
            .show()
    }

    private fun sendMsg(msg: String) {
        EventBus.getDefault().post(MessageEvent(msg))
    }
}