package at.fh.swengb.gradwohl.homework3

import android.arch.persistence.room.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Query("SELECT * FROM note WHERE userName=:username")
    fun selectAllNotes(username:String ?): List<Note>

    @Query("DELETE FROM note WHERE noteId=:id")
    fun deleteSelectedNote (id: Long)

    @Query("SELECT * FROM users WHERE userName=:username")
    fun findAllNotesForUsers(username:String): NotesAndUsers?

    @Query("SELECT * FROM note WHERE noteId=:id")
    fun editNote(id:Long): Note

    @Query("UPDATE note SET title = :title,content = :content WHERE noteId=:id")
    fun updateNotes (title:String,content:String,id:Long)

    @Query("SELECT max(noteId) FROM note")
    fun getLatestID ():Long

}