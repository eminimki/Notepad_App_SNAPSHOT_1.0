package com.eminimki.notepadapp.Fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.eminimki.notepadapp.DBHelper.DataBaseHelper
import com.eminimki.notepadapp.Notes
import com.eminimki.notepadapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates


class EditableNoteContentFragment : Fragment() {
    lateinit var note_NoteHeaderArea : TextInputEditText
    lateinit var note_NoteContentArea : TextInputEditText
    lateinit var saveButton : FloatingActionButton
    lateinit var note : Notes



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_editable_note_content, container, false)
        // Inflate the layout for this fragment
        note_NoteHeaderArea = rootView.findViewById(R.id.note_NoteHeaderArea)
        note_NoteContentArea = rootView.findViewById(R.id.note_NoteContentArea)
        saveButton = rootView.findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveNote(it)
        }

        return rootView

    }

    override fun onResume() {
        super.onResume()

        lateinit var db : DataBaseHelper
        var noteID by Delegates.notNull<Int>()
        context?.let {
            db = DataBaseHelper(it)
        }
        activity?.let {
            it.intent?.let {
                noteID = it.getIntExtra("choosedID", 1)
            }
        }
        note = db.readNote("notes",noteID)
        note_NoteHeaderArea.setText(note.noteName)
        note_NoteContentArea.setText(note.noteContent)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun saveNote(view: View) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS")
        val currentTime = LocalDateTime.now().format(formatter)
        lateinit var db : DataBaseHelper
        context?.let {
            db = DataBaseHelper(it)
        }
        db.updateData("notes",note.noteID,"note_name",note_NoteHeaderArea.text.toString())
        db.updateData("notes",note.noteID,"note_content",note_NoteContentArea.text.toString())
        db.updateData("notes", note.noteID ,"note_modified_time", currentTime.toString())
        activity?.let {
            it.finish()
        }

    }


}