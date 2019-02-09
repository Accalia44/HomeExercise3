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
import kotlinx.android.synthetic.main.activity_new_user.*

class NewUser : AppCompatActivity() {
    companion object {
        val SHARED_PREF_NAMENEW_KEY = "SHARED_PREF_NAMENEW_KEY"
    }
    private lateinit var roomDB: RoomDB
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        roomDB = RoomDB.getDatabase(applicationContext)
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setTitle("New User")
        val userNameNew = sharedPreferences.getString(MainActivity.SHARED_PREF_NAMENEW_KEY, null)
        if (userNameNew != null){
            val userName = new_user_username as EditText
            userName.setText(userNameNew, TextView.BufferType.EDITABLE)
        }
        else {
        }
    }

    fun createUser(view: View){
        val userNameNew = new_user_username.text.toString()
        val firstNameNew = new_user_firstname.text.toString()
        val lastNameNew = new_user_lastname.text.toString()
        val email = new_user_email.text.toString()
        val ageNew = new_user_age.text.toString().toIntOrNull()
        val passwordNew1 = new_user_password1.text.toString()
        val passwordNew2 = new_user_password2.text.toString()

        if (userNameNew.isBlank() || firstNameNew.isBlank() || lastNameNew.isBlank() || ageNew ==null|| passwordNew1.isBlank()||passwordNew2.isBlank() || email.isBlank())
        {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("All fields must be filled")
            dialogBuilder.setMessage("Please make sure that you filled in all of the required fields.")
            dialogBuilder.setNegativeButton("Dismiss", null)
            dialogBuilder.show()
        }
        else if(roomDB.userDao.findByName(userNameNew)>0){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Username already exists")
            dialogBuilder.setMessage("The username is already used, take another one or go back to login.")
            dialogBuilder.setNegativeButton("Dismiss", null)
            dialogBuilder.show()
        }

        else if ( passwordNew1!=passwordNew2){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Password does not match")
            dialogBuilder.setMessage("Please make sure that your passwords match.")
            dialogBuilder.setNegativeButton("Dismiss", null)
            dialogBuilder.show()
        }

        else if (passwordNew1.length in 1..5 ){
            val user = Users(userNameNew,ageNew,passwordNew1,firstNameNew,lastNameNew,email)
            Toast.makeText(this,"Password is very weak",Toast.LENGTH_SHORT).show()
            roomDB.userDao.insertUser(user)
            backToLogin(view)
        }

        else {
            val user = Users(userNameNew,ageNew,passwordNew1,firstNameNew,lastNameNew,email)
            roomDB.userDao.insertUser(user)
            backToLogin(view)
        }
    }

    fun backToLogin(view:View){
        val preferencesEditor = sharedPreferences.edit()
        preferencesEditor.clear()
        preferencesEditor.apply()
        val startMainActivity = Intent(this, MainActivity::class.java)
        startActivity(startMainActivity)
        finish()
    }
}
