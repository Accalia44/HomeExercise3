package at.fh.swengb.gradwohl.homework3

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class NotesAndUsers(){
    @Embedded
    lateinit var user: Users
    @Relation(entity = Note::class, entityColumn = "userName",parentColumn = "userName")
    lateinit var note:List<Note>
}