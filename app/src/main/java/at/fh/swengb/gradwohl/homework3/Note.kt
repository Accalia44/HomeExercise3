package at.fh.swengb.gradwohl.homework3

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "note",
    foreignKeys = [ForeignKey(
        entity = Users::class,
        parentColumns = ["userName"],
        childColumns = ["userName"],
        onDelete = ForeignKey.CASCADE
    )]
)
class Note(val title: String,
           val content: String,val userName :String?) {
    @PrimaryKey(autoGenerate = true)
    var noteId:Long=0
}