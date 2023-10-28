package com.eminimki.notepadapp.AlarmHelper

import android.content.Context
import android.media.MediaPlayer
import android.provider.Settings
// Alarm için tek bir obje oluşturmak için gerekli

object MediaPlayerSingleton {
    private var appContext: Context? = null
    lateinit var mediaPlayer: MediaPlayer

    fun init(context: Context) {
        // Uygulama başlatıldığında bu metodu çağırarak Context'i ayarlayın.
        appContext = context.applicationContext
        mediaPlayer = MediaPlayer.create(context,Settings.System.DEFAULT_RINGTONE_URI)

    }

    fun getPlayer() : MediaPlayer {
        return this.mediaPlayer
    }
}