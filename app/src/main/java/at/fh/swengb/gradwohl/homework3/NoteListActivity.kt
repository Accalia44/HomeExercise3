package at.fh.swengb.gradwohl.homework3


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_list.*


class NoteListActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomDB: RoomDB
    private lateinit var notesAdapter: NotesAdapter
    private var userNameID : String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setTitle("Notes")
        notesAdapter = NotesAdapter({
            Toast.makeText(this, "Note clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("NoteID", it.noteId)
            startActivity(intent)
            }, {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Deleting Note")
            dialogBuilder.setMessage("Are you sure you want to delete the note?")
            dialogBuilder.setPositiveButton("Yes") { _, _ ->
                roomDB.notesDao.deleteSelectedNote(it.noteId)
                updateNoteList()
                }
            dialogBuilder.setNegativeButton("No", null)
            dialogBuilder.show()
            }
        )

        roomDB = RoomDB.getDatabase(applicationContext)
        if(sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null) == null) {
            userNameID=sharedPreferences.getString(AddNoteActivity.SHARED_PREF_NAME_KEY,null)
        }
        else {
            userNameID = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
        }
        notes_recycler_view.adapter = notesAdapter
        notes_recycler_view.layoutManager = LinearLayoutManager(this)
        userInfo()
        updateNoteList()

    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)==null) {
            val userName = sharedPreferences.getString(AddNoteActivity.SHARED_PREF_NAME_KEY, null)
            notesAdapter.updateList(roomDB.notesDao.selectAllNotes(userName))
        }
        else {
            val userName = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
            notesAdapter.updateList(roomDB.notesDao.selectAllNotes(userName))
        }
    }

    private fun updateNoteList(){
        val userNameUpdate = userNameID ?: return
        val findAllNotesForUserObject=roomDB.notesDao.findAllNotesForUsers(userNameUpdate)
        if (findAllNotesForUserObject !=null){
            notesAdapter.updateList(findAllNotesForUserObject.note)
            notesAdapter.updateList(roomDB.notesDao.selectAllNotes(userNameUpdate))
        }

    }

    private fun userInfo() {
        if (sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)==null){
            val userName = sharedPreferences.getString(AddNoteActivity.SHARED_PREF_NAME_KEY,null)
            val age = roomDB.userDao.findUser(userName).age
            user_info.text="Notes from ${userName}, ${age}"
        }
        else {
            val userName = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
            val age = roomDB.userDao.findUser(userName).age

            if (userName.isNullOrBlank()) {
                user_info.text = "Invalid username"
            } else {
                user_info.text = "Notes from ${userName}, ${age}"
            }
        }
    }

    fun addNote(v: View) {
        val addNoteIntent = Intent(this, AddNoteActivity::class.java)
        startActivity(addNoteIntent)
    }

    fun logout(view:View){
        val preferencesEditor = sharedPreferences.edit()
        preferencesEditor.clear()
        preferencesEditor.apply()

        val logoutIntent = Intent(this, MainActivity::class.java)
        startActivity(logoutIntent)
        finish()
    }

    fun changeUserDetails(view:View){
        val changeUserDetails = Intent(this, UpdateUser::class.java)
        startActivity(changeUserDetails)
    }
}
