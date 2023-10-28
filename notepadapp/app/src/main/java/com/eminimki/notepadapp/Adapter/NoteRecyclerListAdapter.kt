package com.eminimki.notepadapp.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.eminimki.notepadapp.DBHelper.DataBaseHelper
import com.eminimki.notepadapp.NoteViewActivity
import com.eminimki.notepadapp.Notes
import com.eminimki.notepadapp.R

class NoteRecyclerListAdapter(var noteList : ArrayList<Notes>) : RecyclerView.Adapter<NoteRecyclerListAdapter.NoteHolder>() {
    class NoteHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        //initilization
        val noteNameArea: TextView = itemView.findViewById(R.id.noteIcon_NoteHeaderArea)
        val noteContentArea: TextView = itemView.findViewById(R.id.noteIcon_NoteContentArea)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
        val settingsButton : ImageView = itemView.findViewById(R.id.noteIcon_SettingsButton)
        val importanceLevel: Array<String> = itemView.resources.getStringArray(R.array.importanceLevel)
        val deleteButton : ImageView = itemView.findViewById(R.id.noteIcon_DeleteButton)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_notes_recycler_row,parent, false)
        return NoteHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val calledNote = noteList.get(position)
        if (calledNote.noteName.length > 24){
            holder.noteNameArea.text =calledNote.noteName.take(24)
        }else{
            holder.noteNameArea.text = calledNote.noteName
        }


        if (calledNote.noteContent.length > 256){
            holder.noteContentArea.text =calledNote.noteContent.take(256)
        }else{
            holder.noteContentArea.text = calledNote.noteContent
        }
        if (calledNote.noteImportanceLevel.equals(holder.importanceLevel[0])){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#96ceb4"))
        }else if (calledNote.noteImportanceLevel.equals(holder.importanceLevel[1])){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffeead"))
        }else if (calledNote.noteImportanceLevel.equals(holder.importanceLevel[2])){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffcc5c"))
        }else if (calledNote.noteImportanceLevel.equals(holder.importanceLevel[3])){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ff6f69"))

        }
        holder.cardView.setOnClickListener{
            val intent = Intent(it.context, NoteViewActivity::class.java)
            intent.putExtra("choosedID", calledNote.noteID)
            it.context.startActivity(intent)

        }
        holder.settingsButton.setOnClickListener {
            Toast.makeText(it.context,"Settings Clicked", Toast.LENGTH_SHORT).show()

        }

        holder.deleteButton.setOnClickListener {
            // delete are you sure alert dialog builder
            val alertDialogBuilder = AlertDialog.Builder(it.context)
            alertDialogBuilder.setTitle(R.string.delete_alert_title)
            alertDialogBuilder.setMessage(R.string.delete_alert_message)
            alertDialogBuilder.setPositiveButton(R.string.delete_alert_positive_button) { dialog, which ->
                var db : DataBaseHelper = DataBaseHelper(it.context)
                db.deleteData("notes", calledNote.noteID,"note_id")
                noteList.removeAt(position)
                notifyDataSetChanged()

            }


            alertDialogBuilder.setNegativeButton(R.string.delete_alert_negative_button) { dialog, which ->

            }

            alertDialogBuilder.show()

        }

    }
    override fun getItemCount(): Int {
        return noteList.size
    }



}






