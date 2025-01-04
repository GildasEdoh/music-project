package tg.ulcrsandroid.music_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.SongBinding
import tg.ulcrsandroid.music_project.model.Chanson

class SongAdapter(val songs: RealmResults<Chanson>) : RecyclerView.Adapter<SongViewHolder>(){
    lateinit var onItemClick: (ObjectId?) -> Unit
    init {
        songs.addChangeListener { _, changeSet ->
            for (change in changeSet.deletionRanges) {
                notifyItemRangeRemoved(change.startIndex, change.length)
            }
            for (change in changeSet.insertionRanges) {
                notifyItemRangeInserted(change.startIndex, change.length)
            }
            for (change in changeSet.changeRanges) {
                notifyItemRangeChanged(change.startIndex, change.length)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val ui = SongBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SongViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.song = songs[position]
        holder.onItemClick = onItemClick
    }

}