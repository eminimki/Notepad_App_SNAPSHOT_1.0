package com.eminimki.notepadapp.DBHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eminimki.notepadapp.AlarmHelper.Alarm
import com.eminimki.notepadapp.Notes
//DB NAME
val db_name = "NotepadX"
//NOTES TABLE
val note_table_name = "notes"
val col_note_id = "note_id"
val col_note_name = "note_name"
val col_note_content = "note_content"
val col_note_importance_level = "note_importance_level"
val col_note_has_alarm = "note_has_alarm"
val col_note_has_lock = "note_has_lock"
val col_note_created_time = "note_created_time"
val col_note_modified_time = "note_modified_time"
//ALARM TABLE
val alarm_table_name = "alarms"
val col_alarm_id = "alarm_id"
val col_alarm_date = "alarm_date"
val col_alarm_clock = "alarm_clock"
val col_alarm_recurring = "alarm_recurring"
val col_alarm_created_time = "alarm_created_time"
val col_alarm_modified_time = "alarm_modified_time"
//LOCK TABLE
val lock_table_name = "locks"
val col_lock_id = "lock_id"
val col_lock_pass = "lock_pass"


class DataBaseHelper (var context : Context) : SQLiteOpenHelper(context, db_name,null,1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        var createNoteTable = " CREATE TABLE " + note_table_name + "(" +
                col_note_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                col_note_name + " VARCHAR(24)," +
                col_note_content + " TEXT," +
                col_note_importance_level + " VARCHAR," +
                col_note_has_alarm + " TEXT," +
                col_note_has_lock + " TEXT," +
                col_note_created_time + " VARCHAR," +
                col_note_modified_time + " VARCHAR)"
        p0?.execSQL(createNoteTable)

        var createAlarmTable = " CREATE TABLE " + alarm_table_name + "(" +
                col_alarm_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                col_alarm_date + " TEXT," +
                col_alarm_clock + " TEXT," +
                col_alarm_recurring + " TEXT," +
                col_alarm_created_time + " TEXT," +
                col_alarm_modified_time + " TEXT," +
                col_note_id + " INTEGER," +
                "FOREIGN KEY($col_note_id) REFERENCES $note_table_name($col_note_id))"
        p0?.execSQL(createAlarmTable)




    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun noteInsertData(note : Notes){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(col_note_name, note.noteName)
        cv.put(col_note_content, note.noteContent)
        cv.put(col_note_importance_level, note.noteImportanceLevel)
        cv.put(col_note_has_alarm, note.noteHasAlarm)
        cv.put(col_note_has_lock, note.noteHasLock)
        cv.put(col_note_created_time, note.noteCreatedTime)
        cv.put(col_note_modified_time, note.noteModifiedTime)
        db.insert(note_table_name,null,cv)
    }
    // Insert Alarm
    fun alarmInsertData(alarmDate : String , alarmClock : String , alarmRecurring : String ,
                        alarmCreatedTime : String , noteID : Int ){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(col_alarm_date, alarmDate)
        cv.put(col_alarm_clock , alarmClock)
        cv.put(col_alarm_recurring , alarmRecurring)
        cv.put(col_alarm_created_time , alarmCreatedTime)
        cv.put(col_alarm_modified_time , alarmCreatedTime)
        cv.put(col_note_id , noteID)

        db.insert(alarm_table_name,null,cv)

    }



    @SuppressLint("Range")
    fun readNoteData(): MutableList<Notes> {
        var list : MutableList<Notes> = ArrayList()
        val db = this.readableDatabase

        var getData = "SELECT * FROM " + note_table_name
        var result = db.rawQuery(getData,null)
        if (result.moveToFirst()){
            do {
                var noteName = result.getString(result.getColumnIndex(col_note_name))
                var noteContent = result.getString(result.getColumnIndex(col_note_content))
                var noteImportanceLevel = result.getString(result.getColumnIndex(
                    col_note_importance_level))
                var noteHasAlarm = result.getString(result.getColumnIndex(col_note_has_alarm))
                var noteHasLock = result.getString(result.getColumnIndex(col_note_has_lock))
                var noteCreatedTime = result.getString(result.getColumnIndex(col_note_created_time))
                var noteModifiedTime = result.getString(result.getColumnIndex(col_note_modified_time))
                var note = Notes(noteName,noteContent,noteImportanceLevel,noteHasAlarm,noteHasLock,noteCreatedTime,noteModifiedTime)
                note.noteID = result.getInt(result.getColumnIndex(col_note_id))
                list.add(note)

            }while (result.moveToNext())
        }
        result.close()
        db.close()

        return list

    }


    @SuppressLint("Range")
    fun readAlarmData(): MutableList<Alarm> {
        var list : MutableList<Alarm> = ArrayList()
        val db = this.readableDatabase

        var getData = "SELECT * FROM " + alarm_table_name
        var result = db.rawQuery(getData,null)
        if (result.moveToFirst()){
            do {
                var alarm = Alarm()

                alarm.alarmID = result.getInt(result.getColumnIndex(col_alarm_id))

                alarm.alarmDate = result.getString(result.getColumnIndex(col_alarm_date))

                alarm.alarmClock = result.getString(result.getColumnIndex(col_alarm_clock))

                alarm.alarmRecurring = result.getString(result.getColumnIndex(
                    col_alarm_recurring))

                alarm.alarmCreatedTime = result.getString(result.getColumnIndex(col_alarm_created_time))

                alarm.alarmModifiedTime = result.getString(result.getColumnIndex(
                    col_alarm_modified_time))

                alarm.noteID = result.getInt(result.getColumnIndex(col_note_id))

                list.add(alarm)

            }while (result.moveToNext())
        }
        result.close()
        db.close()

        return list

    }



    @SuppressLint("Range", "Recycle")
    fun readNote(tableName : String ,ID : Int) : Notes {
        val db = this.readableDatabase
        lateinit var note: Notes
        var getData = "SELECT * FROM $tableName WHERE $col_note_id = $ID"
        var result = db.rawQuery(getData,null)

        if (result.moveToFirst()){
            do {
                var noteName = result.getString(result.getColumnIndex(col_note_name))
                var noteContent = result.getString(result.getColumnIndex(col_note_content))
                var noteImportanceLevel = result.getString(result.getColumnIndex(
                    col_note_importance_level))
                var noteHasAlarm = result.getString(result.getColumnIndex(col_note_has_alarm))
                var noteHasLock = result.getString(result.getColumnIndex(col_note_has_lock))
                var noteCreatedTime = result.getString(result.getColumnIndex(col_note_created_time))
                var noteModifiedTime = result.getString(result.getColumnIndex(col_note_modified_time))
                note = Notes(noteName,noteContent,noteImportanceLevel,noteHasAlarm,noteHasLock,noteCreatedTime,noteModifiedTime)
                note.noteID = result.getInt(result.getColumnIndex(col_note_id))

            }while (result.moveToNext())
        }
        return note
    }

    fun updateData(tableName: String,ID : Int , colName : String , data : String){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(colName,data)
        db.update(tableName,cv , "$col_note_id = $ID ",null)
        db.close()
    }

    fun deleteData(tableName: String , ID: Int , IDColName: String ){
        val db = this.writableDatabase
        db.delete(tableName,"$IDColName = $ID", null)
        db.close()
    }


}






