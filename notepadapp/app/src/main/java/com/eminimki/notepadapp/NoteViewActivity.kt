package com.eminimki.notepadapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NoteViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_view)
        // Getting intent data
        val bundle: Bundle? = intent.extras
        bundle?.let {
            val getExtr = intent.getIntExtra("choosedID",0)
        }
    }





}