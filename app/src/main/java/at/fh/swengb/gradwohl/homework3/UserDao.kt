package at.fh.swengb.gradwohl.homework3

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: Users)

    @Query("SELECT COUNT(*) FROM users WHERE userName LIKE '%' || :searchString || '%'")
    fun findByName(searchString: String): Int

    @Query("SELECT userName,age,password,email,firstName,lastName FROM users WHERE userName LIKE '%' || :searchString || '%'")
    fun findUser(searchString: String): Users

    @Query("UPDATE users SET age=:age,password=:password,firstname=:firstname,lastname=:lastname,email=:email WHERE userName=:username")
    fun updateUser (age:Int,password:String,username:String,firstname:String,lastname:String,email:String)

    @Query("DELETE FROM users WHERE userName=:username")
    fun deleteUser (username: String)

}
