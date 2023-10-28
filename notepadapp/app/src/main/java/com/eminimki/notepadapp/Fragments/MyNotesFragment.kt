package com.eminimki.notepadapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eminimki.notepadapp.Adapter.NoteRecyclerListAdapter
import com.eminimki.notepadapp.DBHelper.DataBaseHelper
import com.eminimki.notepadapp.Notes
import com.eminimki.notepadapp.R


class MyNotesFragment : Fragment() {
    var noteList = ArrayList<Notes>()
    private lateinit var recyclerView: RecyclerView
    var adapter = NoteRecyclerListAdapter(noteList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_notes, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView) // Burada recyclerView'ı tanımlar

        // RecyclerView'ı yapılandırma ve yönetici (LayoutManager) ataması yapabilirsiniz

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        //getData()

    }

    override fun onResume() {
        super.onResume()
        createList()
    }

    fun createList(){
        lateinit var db : DataBaseHelper
        context?.let {
            db = DataBaseHelper(it)
        }
        noteList = db.readNoteData() as ArrayList<Notes>

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        adapter = NoteRecyclerListAdapter(noteList)
        recyclerView.adapter = adapter


    }









}