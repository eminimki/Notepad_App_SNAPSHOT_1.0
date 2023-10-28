package com.eminimki.notepadapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.eminimki.notepadapp.AlarmHelper.MediaPlayerSingleton
import com.eminimki.notepadapp.Fragments.CalendarFragment
import com.eminimki.notepadapp.Fragments.FilterFragment
import com.eminimki.notepadapp.Fragments.MyNotesFragment
import com.eminimki.notepadapp.Fragments.SettingsFragment
import com.eminimki.notepadapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MediaPlayerSingleton.init(this)
        loadFragment(MyNotesFragment())
        val bottomNav = findViewById(R.id.bottom_navigation) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.myNotesNavButton -> {
                    loadFragment(MyNotesFragment())
                    true
                }
                R.id.calendarNavButton -> {
                    loadFragment(CalendarFragment())
                    true
                }
                R.id.filterNavButton -> {
                    loadFragment(FilterFragment())
                    true
                }
                R.id.settingsNavButton -> {
                    loadFragment(SettingsFragment())
                    true
                }

                else -> {true}
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentNavHost,fragment)
        transaction.commit()
    }


    fun addNewNote(view : View) {
        val intent =Intent(this,NoteAddActivity::class.java)
        startActivity(intent)
    }


    fun finisThisActivity(id : Int){
        val intent = Intent(this, NoteViewActivity::class.java)
        intent.putExtra("choosedID", id)
        startActivity(intent)
    }



}