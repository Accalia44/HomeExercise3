package at.fh.swengb.gradwohl.homework3


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*


class AddNoteActivity : AppCompatActivity() {
    private lateinit var roomDB: RoomDB
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        val SHARED_PREF_NAME_KEY = "SHARED_PREF_NAME_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setTitle("Notes")
        roomDB = RoomDB.getDatabase(applicationContext)
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val user = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY,null)
        val preferencesEditor = sharedPreferences.edit()
        preferencesEditor.clear()
        preferencesEditor.apply()
        sharedPreferences.edit().putString(AddNoteActivity.SHARED_PREF_NAME_KEY,user).apply()
        loadNote()
    }

    fun saveNote(v: View) {
        val title = add_note_title.text.toString()
        val content = add_note_content.text.toString()
        val user = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
        if (title.isNotBlank() || content.isNotBlank()) {
            val noteID=intent.getLongExtra("NoteID",-1).toInt()
            if (noteID ==-1){
                val note = Note(title, content, user)
                roomDB.notesDao.insertNote(note)
                Toast.makeText(this, "Note was created", Toast.LENGTH_LONG).show()
                sharedPreferences.edit().putLong("NoteIDInsert",roomDB.notesDao.getLatestID()).apply()
            }
            else {
                val noteIDNeu=noteID.toLong()
                roomDB.notesDao.updateNotes(title,content,noteIDNeu)
                Toast.makeText(this, "Note was updated", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Please add a title and content", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadNote(){
        val noteId= intent.getLongExtra("NoteID",-1).toInt()
        if (noteId == -1 ){
        }
        else {
            val noteID = intent.getLongExtra("NoteID",-1)
            val content = add_note_content as EditText
            content.setText(roomDB.notesDao.editNote(noteID).content, TextView.BufferType.EDITABLE)
            val title = add_note_title as EditText
            title.setText(roomDB.notesDao.editNote(noteID).title, TextView.BufferType.EDITABLE)
        }
    }

    fun share (view:View){
        val noteId= intent.getLongExtra("NoteID",-1).toInt()
        if (noteId==-1){
            val noteIDNeu= sharedPreferences.getLong("NoteIDInsert",-1).toInt()
            if (noteIDNeu == -1)
            {
                Toast.makeText(this,"Please save the note before you share it.",Toast.LENGTH_LONG).show()
                return
            }
        }
        else {
            val noteIdLong=intent.getLongExtra("NoteID",-1)
            val titleOld = roomDB.notesDao.editNote(noteIdLong).title
            val contentOld=roomDB.notesDao.editNote(noteIdLong).content
            if(titleOld != add_note_title.text.toString() || contentOld != add_note_content.text.toString()){
                Toast.makeText(this,"Please safe the note before you share it.",Toast.LENGTH_LONG).show()
                return
                }
            }
        sharingIntent()
    }

    fun close (view: View){
        val title = add_note_title.text.toString()
        val content = add_note_content.text.toString()
        val noteId= intent.getLongExtra("NoteID",-1).toInt()
        if (noteId==-1){
            val noteIDNeu= sharedPreferences.getLong("NoteIDInsert",-1).toInt()
            if (noteIDNeu == -1 && title.isNotEmpty() || content.isNotEmpty())
            {
                unsavedNoteMessage()
                return
            }
        }

        else {
            val noteIdLong=intent.getLongExtra("NoteID",-1)
            val titleOld = roomDB.notesDao.editNote(noteIdLong).title
            val contentOld=roomDB.notesDao.editNote(noteIdLong).content
            if(titleOld != add_note_title.text.toString() || contentOld != add_note_content.text.toString())
            {
                unsavedNoteMessage()
                return
            }
        }
        val startMainIntent=Intent(this,NoteListActivity::class.java)
        startActivity(startMainIntent)
        finish()
    }


   private fun unsavedNoteMessage(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Unsaved note")
        dialogBuilder.setMessage("Are you sure you want to close without saving?")
        dialogBuilder.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        dialogBuilder.setNegativeButton("No", null)
        dialogBuilder.show()
    }

    private fun sharingIntent(){
        val title = add_note_title.text.toString()
        val content = add_note_content.text.toString()
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,"Title: ${title}, Content:${content}")
        intent.type="text/plain"
        val chooserIntent = Intent.createChooser(intent,"Select an App to share your Notes")
            startActivity(chooserIntent)

    }
}
