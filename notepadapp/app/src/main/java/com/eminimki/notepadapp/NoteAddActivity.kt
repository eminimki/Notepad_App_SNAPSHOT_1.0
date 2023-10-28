package com.eminimki.notepadapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eminimki.notepadapp.AlarmHelper.Alarm
import com.eminimki.notepadapp.DBHelper.DataBaseHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class NoteAddActivity : AppCompatActivity() {
    var noteImportanceLevel = ""
    lateinit var db : DataBaseHelper
    lateinit var autoCompleteTextView : AutoCompleteTextView
    var hourResult = "0"
    var minuteResult = "0"
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)


    }

    

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val lockSwitch: SwitchMaterial = findViewById(R.id.lockSwitchButton)
        val alarmSwitch: SwitchMaterial = findViewById(R.id.alarmSwitchButton)
        val dateButton = findViewById<Button>(R.id.dateInputArea)
        val timerButton = findViewById<Button>(R.id.timeInputArea)
        val noteLockArea: TextInputLayout = findViewById(R.id.editTextLockArea)
        val context = this
        db = DataBaseHelper(context)
        // Spinner Creator
        val importanceLevels = resources.getStringArray(R.array.importanceLevel)
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val adapter = ArrayAdapter(this, R.layout.list_item, importanceLevels)
        autoCompleteTextView.setAdapter(adapter)

        //alarm switch Listener
        alarmSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                dateButton.setEnabled(true)
                timerButton.setEnabled(true)
            } else {
                dateButton.setEnabled(false)
                timerButton.setEnabled(false)
            }
        }
        //lock switch Listener
        lockSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                noteLockArea.setEnabled(true)
                Toast.makeText( this, "This feature not work yet", Toast.LENGTH_SHORT).show()
            } else {
                noteLockArea.setEnabled(false)
                noteLockArea.editText?.setText("")
                Toast.makeText( this, "This feature not work yet", Toast.LENGTH_SHORT).show()
            }
        }
        //datePicker
        dateButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("EEEE, MMM d, yyyy")
                val date = dateFormatter.format(Date(it))
                dateButton.setText(date)
            }
            // Setting up the event for when cancelled is clicked

        }

        //Time Picker
        timerButton.setOnClickListener {
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting our hour, minute.
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // on below line we are initializing
            // our Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    // on below line we are setting selected
                    // time in our text view.

                    if (hourOfDay < 10){hourResult = "0$hourOfDay"}else{ hourResult = hourOfDay.toString() }
                    if (minute < 10){minuteResult = "0$minute"}else{minuteResult = minute.toString()}
                    timerButton.setText("$hourResult:$minuteResult")
                },
                hour,
                minute,
                true
            )
            // at last we are calling show to
            // display our time picker dialog.
            timePickerDialog.show()


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun noteAddButtonAddPage(view: View) {
        val lockSwitch: SwitchMaterial = findViewById(R.id.lockSwitchButton)
        val alarmSwitch: SwitchMaterial = findViewById(R.id.alarmSwitchButton)
        val dateButton = findViewById<Button>(R.id.dateInputArea)
        val timerButton = findViewById<Button>(R.id.timeInputArea)
        var lastNoteDataID = 0
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")
        val currentTime = LocalDateTime.now().format(formatter)
        val noteNameArea : TextInputLayout = findViewById(R.id.editTextNoteName)
        val noteName = noteNameArea.editText?.text.toString()
        if (noteName.isNotEmpty()){
            if (noteName.length <= 24){
                if (autoCompleteTextView.text.toString().isNotEmpty()){
                    var note = Notes(noteNameArea.editText?.text.toString(),"", autoCompleteTextView.text.toString(),
                        alarmSwitch.isChecked.toString(),lockSwitch.isChecked.toString(),currentTime.toString(), currentTime.toString())

                    // alarm insert
                    if (alarmSwitch.isChecked){
                        if (!dateButton.text.equals(resources.getString(R.string.select_date))){
                            if (!timerButton.text.equals(resources.getString(R.string.select_time))){
                                db.noteInsertData(note)
                                lastNoteDataID = db.readNoteData().last().noteID
                                db.alarmInsertData(dateButton.text.toString(),timerButton.text.toString(),
                                    "none", currentTime,lastNoteDataID)

                                val lastAlarmDataID = db.readAlarmData().last().alarmID



                                val calendar: Calendar = Calendar.getInstance()
                                if (Build.VERSION.SDK_INT >= 23) {
                                    calendar.set(
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                        hourResult.toInt(),
                                        minuteResult.toInt(),
                                        0
                                    )
                                } else {
                                    calendar.set(
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                        hourResult.toInt(),
                                        minuteResult.toInt(), 0
                                    )
                                }
                                // TODO this module has bug now, but bugfix update coming soon , wait pls
                                /*println("noteadd reqID: $lastAlarmDataID ")
                                Alarm().setAlarm(this, calendar.timeInMillis , lastAlarmDataID)
                                */
                                startViewActivity(lastNoteDataID)

                            }else{Toast.makeText(this,R.string.choose_a_time,Toast.LENGTH_SHORT).show()}
                        }else{ Toast.makeText(this,R.string.choose_a_date,Toast.LENGTH_SHORT).show()}
                    }else{
                        db.noteInsertData(note)
                        lastNoteDataID = db.readNoteData().last().noteID
                        startViewActivity(lastNoteDataID)
                    }

                }else{
                    Toast.makeText(this,R.string.note_importance_is_not_empty,Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,R.string.note_name_too_long_message,Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,R.string.note_name_is_not_empty,Toast.LENGTH_SHORT).show()
        }
        

    }

    private fun startViewActivity(lastDataID : Int){
        val intent = Intent(this, NoteViewActivity::class.java)
        intent.putExtra("choosedID", lastDataID )
        startActivity(intent)
        finish()
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navHostNoteAddActivity,fragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
}


