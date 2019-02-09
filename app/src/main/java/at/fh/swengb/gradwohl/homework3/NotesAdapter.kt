package at.fh.swengb.gradwohl.homework3

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_note.view.*


class NotesAdapter(val clickListener:(note: Note) -> Unit, val longClickListener:(note: Note) -> Unit): RecyclerView.Adapter<NotesViewHolder>() {
    private var notesList: List<Note> = emptyList()

    fun updateList(newList: List<Note>) {
        notesList = newList.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val Noteview = inflater.inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(Noteview, clickListener, longClickListener)
    }

    override fun getItemCount() = notesList.size

    override fun onBindViewHolder(viewHolder: NotesViewHolder, position: Int) {
        val item = notesList[position]
        viewHolder.bind(item)
    }
}

class NotesViewHolder(
    view: View,
    val clickListener: (note: Note) -> Unit, val longClickListener: (note: Note) -> Unit): RecyclerView.ViewHolder(view) {
    fun bind(note: Note) {
        itemView.note_title.text = note.title
        itemView.note_content.text = note.content
        itemView.setOnClickListener {
            clickListener(note)
        }
        itemView.setOnLongClickListener {
            longClickListener(note)
            true }
    }
}
