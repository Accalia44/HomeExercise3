package at.fh.swengb.gradwohl.homework3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val SHARED_PREF_NAME_KEY = "SHARED_PREF_NAME_KEY"
        val SHARED_PREF_NAMENEW_KEY = "SHARED_PREF_NAMENEW_KEY"
    }
    private lateinit var roomDB: RoomDB
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roomDB = RoomDB.getDatabase(applicationContext)
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setTitle("Login")

    }


    fun onSaveButtonClick(v: View) {
        val userNameInput = user_name.text.toString()
        val password = user_password.text.toString()

        if (userNameInput.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please enter a username and a Password", Toast.LENGTH_LONG).show()
            return
        }

        else if (roomDB.userDao.findByName(userNameInput) == 0){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("No user with that username")
            dialogBuilder.setMessage("Please try again or create a user with this name")
            dialogBuilder.setNegativeButton("Dismiss", null)
            dialogBuilder.show()
        }

        else if(userNameInput==roomDB.userDao.findUser(userNameInput).userName && password == roomDB.userDao.findUser(userNameInput).password){
            sharedPreferences.edit().putString(SHARED_PREF_NAME_KEY, userNameInput).apply()
            startNextActivit()
        }

        else {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Wrong Password or Wrong Username.")
            dialogBuilder.setMessage("Please try again")
            dialogBuilder.setNegativeButton("Dismiss", null)
            dialogBuilder.show()
        }
    }

    private fun startNextActivit (){
        val startNoteListActivityIntent = Intent(this, NoteListActivity::class.java)
        startActivity(startNoteListActivityIntent)
        finish()
    }

    fun createAccount(view:View){
        if (user_name.text.toString().isEmpty()) {
            val preferencesEditor = sharedPreferences.edit()
            preferencesEditor.clear()
            preferencesEditor.apply()
            callNewUserIntent()
        }
        else {
            val userName = user_name.text.toString()
            sharedPreferences.edit().putString(SHARED_PREF_NAMENEW_KEY,userName).apply()
            callNewUserIntent()
        }
    }

    private fun callNewUserIntent(){
        val intent = Intent(this, NewUser::class.java)
        startActivity(intent)
        finish()
    }
}
