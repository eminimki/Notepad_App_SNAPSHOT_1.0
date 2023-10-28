// TODO this module has bug now, but bugfix update coming soon , wait pls
package com.eminimki.notepadapp.AlarmHelper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.eminimki.notepadapp.NotificationHelper.NotificationReceiver
import com.eminimki.notepadapp.R
/* KENDIME NOT
* Singleton yapısı ile her seferinde yeni bir init ile mp baslatıp onları çalıştırabiliriz
* ancak  bunda  da aynı saate 2 alarm kurma problemi ortaya çıkmakta
*
* diğer bir seçeenek olarak pause kullanbilir ama bu da çok optimize olmayabilir
* 
*
* */
class AlarmReceiver : BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var alarmManager: AlarmManager
    private lateinit var context: Context
    private var requestCode = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context!!
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationManager = NotificationManagerCompat.from(context)

        //requestCode Alır, bu kod alarmID ile eşleşmelidir.
        if (intent != null){
            requestCode = intent.getIntExtra("requestCode" , -1)
            if (requestCode == -1) {
                Toast.makeText(context,R.string.error_message,Toast.LENGTH_LONG).show()
            }
        }

        // Actionları denetler özel bir action yoksa alarm başlatır. defaultu alarm başlatmadır
        if (intent?.action == "CANCEL_ALARM") {
            if (MediaPlayerSingleton.getPlayer().isPlaying){cancelAlarm(requestCode)}
            cancelNotification(requestCode)

        } else if(intent?.action == "AUTO_CANCEL_ALARM"){
            cancelAlarm(requestCode)

        }else {
            showNotification(requestCode)
            startAlarm()

        }


    }
    // RequestCode ile bir bildirim oluşturur ve bunu gösterir.
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel_$requestCode",
                "Alarm Channel $requestCode",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            this.requestCode,
            notificationIntent,
            0)

        val notification = NotificationCompat.Builder(context, "alarm_channel_$requestCode")
            .setContentTitle("Alarm Çalıyor")
            .setContentText("Alarm Zamanı Geldi!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // veya NotificationCompat.PRIORITY_MAX
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Kapat",
                getCancelAlarmPendingIntent(context, requestCode)
            )
            .build()

        notificationManager.notify(requestCode, notification)
    }

    //Alarm bildirimden alarm iptali gerektiğinde get ile pending intenti notfy actiona iletir
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getCancelAlarmPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "CANCEL_ALARM"
        intent.putExtra("requestCode", requestCode)
        return PendingIntent.getBroadcast(context, requestCode, intent, 0)
    }

    //Alarm başlatılır. Singleton yapısı kullanılarak bir mediaplayer alınır.
    fun startAlarm(){
        //MediaPlayerSingleton.init(context)
        val mediaPlayer = MediaPlayerSingleton.getPlayer()
        Log.d("startAlarmFunctioDeneme", "start alarm mp: $mediaPlayer" )
        println("start Alarm mp: $mediaPlayer")
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        setAutoCancelAlarm(context, this.requestCode)

    }

    // Alarmı susturur
    private fun cancelAlarm(requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        alarmManager.cancel(pendingIntent)
        val mediaPlayer = MediaPlayerSingleton.getPlayer()
        if (mediaPlayer.isPlaying){
            mediaPlayer.let {
                println("isplayig girdi : $mediaPlayer")
                it.isLooping = false
                // Pause kullanmamızın sebebi, stop çektiğimizde aynı objeye start çekemiyoruz, bu da birden çok alarm için
                // birden çok mediaplayer objesi oluşturmak anlamına gelir ancak bu da alarmlarda karışıklığa sebep olabilir,
                it.pause()
            }
        }

    }
    // requestCode ile ilgili alarm notfy kapatır.
    private fun cancelNotification(requestCode: Int) {
        notificationManager.cancel(requestCode)
    }


    private fun getAutoCancelPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "AUTO_CANCEL_ALARM"
        intent.putExtra("requestCode", requestCode)
        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    // Alarm'dan 30 sn sonra çağırılır ve eğer alarm kapatılmadıysa alarmı kapatır. set/get ortak çalışır.
    private fun setAutoCancelAlarm(context: Context, requestCode: Int) {
        val autoCancelIntent = getAutoCancelPendingIntent(context, requestCode)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cancelTimeMillis = System.currentTimeMillis() + 30 * 1000 // 30 saniye sonra alarmı iptal et
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cancelTimeMillis, autoCancelIntent)
    }

}