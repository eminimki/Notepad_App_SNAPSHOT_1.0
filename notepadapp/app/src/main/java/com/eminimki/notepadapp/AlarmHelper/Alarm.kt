// TODO this module has bug now, but bugfix update coming soon , wait pls
package com.eminimki.notepadapp.AlarmHelper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlin.properties.Delegates

class Alarm () {


    var alarmID by Delegates.notNull<Int>()
    lateinit var alarmDate : String
    lateinit var alarmClock: String
    lateinit var alarmRecurring: String
    lateinit var alarmModifiedTime : String
    lateinit var alarmCreatedTime: String
    var noteID by Delegates.notNull<Int>()


    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarm(context: Context, triggerTime : Long, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startIntent = Intent(context, AlarmReceiver::class.java)
        startIntent.putExtra("requestCode" , requestCode)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, startIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        MediaPlayerSingleton.init(context)
        //set Player
       //val intent = Intent(context, AlarmReceiver::class.java)

        println("setAlarm reqID: $requestCode ")
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }





}