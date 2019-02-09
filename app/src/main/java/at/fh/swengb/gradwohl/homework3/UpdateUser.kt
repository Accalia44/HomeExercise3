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
import kotlinx.android.synthetic.main.activity_update_user.*


class UpdateUser : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var roomDB: RoomDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        roomDB = RoomDB.getDatabase(applicationContext)
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        setTitle("Edit User")
        if (sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)==null){
            val userName = sharedPreferences.getString(AddNoteActivity.SHARED_PREF_NAME_KEY, null)
            val userNameEdit = new_user_username as EditText
            userNameEdit.setText(roomDB.userDao.findUser(userName).userName, TextView.BufferType.EDITABLE)
        }
        val userName = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
        val userNameEdit = new_user_username as EditText
        userNameEdit.setText(roomDB.userDao.findUser(userName).userName, TextView.BufferType.EDITABLE)
        val userEmailEdit = new_user_email as EditText
        userEmailEdit.setText(roomDB.userDao.findUser(userName).email, TextView.BufferType.EDITABLE)
        val userFirstnameEdit = new_user_firstname as EditText
        userFirstnameEdit.setText(roomDB.userDao.findUser(userName).firstName, TextView.BufferType.EDITABLE)
        val userLastnameEdit = new_user_lastname as EditText
        userLastnameEdit.setText(roomDB.userDao.findUser(userName).lastName, TextView.BufferType.EDITABLE)
        val userAgeEdit = new_user_age as EditText
        userAgeEdit.setText(roomDB.userDao.findUser(userName).age.toString(), TextView.BufferType.EDITABLE)

    }

    fun update (view: View){
        val userNameOld = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
        val userNameEdit = new_user_username.text.toString()
        val userFirstnameEdit = new_user_firstname.text.toString()
        val userLastnameEdit = new_user_lastname.text.toString()
        val userAgeEdit = new_user_age.text.toString().toInt()
        val userEmailEdit = new_user_email.text.toString()
        val passwordChanged1 = new_user_password1.text.toString()
        val passwordChanged2 = new_user_password2.text.toString()
        if (userNameEdit==userNameOld && userFirstnameEdit==roomDB.userDao.findUser(userNameOld).firstName&&userLastnameEdit==roomDB.userDao.findUser(userNameOld).lastName && userAgeEdit==roomDB.userDao.findUser(userNameOld).age&&userEmailEdit==roomDB.userDao.findUser(userNameOld).email &&passwordChanged1.isBlank()){
            startNoteListActivity()
        }
        else {
        if (userNameEdit.isBlank() || userFirstnameEdit.isBlank() || userLastnameEdit.isBlank() || userAgeEdit ==null|| userEmailEdit.isBlank())
        {
            Toast.makeText(this,"All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        else if (passwordChanged1 != passwordChanged2){
            Toast.makeText(this,"Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
        

        else if(userNameEdit!=userNameOld&&roomDB.userDao.findByName(userNameEdit)!=0){
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Username already taken")
            dialogBuilder.setMessage("The username you chose already exits, please take another one.")
            dialogBuilder.setNegativeButton("Dismiss", null)
            dialogBuilder.show()
            return
        }
        else if (userNameEdit!=userNameOld&&roomDB.userDao.findByName(userNameEdit)==0){
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.allert_password, null)
            dialogBuilder.setView(dialogView)
            val password = dialogView.findViewById(R.id.alert_password) as EditText

            dialogBuilder.setTitle("All notes will be lost")
            dialogBuilder.setMessage("If you change your username, all your notes will be lost. For proceeding enter your old password. You have to login again")

            dialogBuilder.setPositiveButton(
                "Yes"
            ) { _, _ ->
                if (password.text.toString()==roomDB.userDao.findUser(userNameOld).password) {
                    val userNew = Users(userNameEdit,userAgeEdit,passwordChanged1,userFirstnameEdit,userLastnameEdit,userEmailEdit)
                    roomDB.userDao.insertUser(userNew)
                    roomDB.userDao.deleteUser(userNameOld)
                    val preferencesEditor = sharedPreferences.edit()
                    preferencesEditor.clear()
                    preferencesEditor.apply()
                    startNoteListActivity()
                }
                else {
                    Toast.makeText(this,"Wrong Password",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
            }
            dialogBuilder.setNegativeButton("No",null)
            dialogBuilder.show()
            return

        }

        alertMessagePassword()
        }
    }

    private fun alertMessagePassword (){
        val userNameOld = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)
        val userFirstnameEdit = new_user_firstname.text.toString()
        val userLastnameEdit = new_user_lastname.text.toString()
        val userAgeEdit = new_user_age.text.toString().toInt()
        val userEmailEdit = new_user_email.text.toString()
        val passwordChanged1 = new_user_password1.text.toString()
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.allert_password, null)
        dialogBuilder.setView(dialogView)
        val password = dialogView.findViewById(R.id.alert_password) as EditText

        dialogBuilder.setTitle("Password required to commit changes")
        dialogBuilder.setMessage("Please enter your old password to commit the changes.")

        dialogBuilder.setPositiveButton(
            "OK"
        ) { _, _ ->
            if (password.text.toString()==roomDB.userDao.findUser(userNameOld).password) {
                if (passwordChanged1.isBlank()){
                    roomDB.userDao.updateUser(userAgeEdit,password.text.toString(),userNameOld,userFirstnameEdit,userLastnameEdit,userEmailEdit)
                    startNoteListActivity()
                }
                else {
                    roomDB.userDao.updateUser(userAgeEdit,passwordChanged1,userNameOld,userFirstnameEdit,userLastnameEdit,userEmailEdit)
                    startNoteListActivity()
                }
            }
            else {
                Toast.makeText(this,"Wrong Password",Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
        }
        dialogBuilder.setNegativeButton("Back",null)
        dialogBuilder.show()
        return
    }

    fun delete (view:View){
        val userNameOld = sharedPreferences.getString(MainActivity.SHARED_PREF_NAME_KEY, null)

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.allert_password, null)
        dialogBuilder.setView(dialogView)
        val password = dialogView.findViewById(R.id.alert_password) as EditText

        dialogBuilder.setTitle("Password required to delete User")
        dialogBuilder.setMessage("Please enter your password to delete your account.")

        dialogBuilder.setPositiveButton(
            "OK"
        ) { _, _ ->
            if (password.text.toString()==roomDB.userDao.findUser(userNameOld).password) {
                    roomDB.userDao.deleteUser(userNameOld)
                    val preferencesEditor = sharedPreferences.edit()
                    preferencesEditor.clear()
                    preferencesEditor.apply()
                    val logInActivityIntent = Intent(this,MainActivity::class.java)
                    startActivity(logInActivityIntent)
                    finish()
            }
            else {
                Toast.makeText(this,"Wrong Password",Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
        }
        dialogBuilder.setNegativeButton("Back",null)
        dialogBuilder.show()
        return

    }

    private fun startNoteListActivity (){
        val startNoteActivityIntent = Intent(this,NoteListActivity::class.java)
        startActivity(startNoteActivityIntent)
        finish()
    }

}
