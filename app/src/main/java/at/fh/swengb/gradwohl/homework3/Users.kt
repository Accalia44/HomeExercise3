package at.fh.swengb.gradwohl.homework3

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
class Users(@PrimaryKey val userName: String,
            var age: Int,
            var password: String,
            val firstName: String,
            val lastName: String,
            val email: String)


