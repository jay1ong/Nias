package cn.jaylong.nias.core.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/18
 */
class TimeZoneBroadcastReceiver(
    val onTimeZoneChanged: () -> Unit,
) : BroadcastReceiver() {
    private var register = false

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
            onTimeZoneChanged()
        }
    }

    fun register(context: Context) {
        if (!register) {
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
            context.registerReceiver(this, filter)
            register = true
        }
    }

    fun unregister(context: Context) {
        if (register) {
            context.unregisterReceiver(this)
            register = false
        }
    }

}