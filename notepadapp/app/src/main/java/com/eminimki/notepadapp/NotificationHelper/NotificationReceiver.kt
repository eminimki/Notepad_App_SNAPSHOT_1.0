// TODO this module has bug now, but bugfix update coming soon , wait pls
package com.eminimki.notepadapp.NotificationHelper

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            if (intent.action == "CLOSE_NOTIFICATION") {
                val notificationId = intent.getIntExtra("notificationId", 0)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationId)
            }
        }
    }
}
