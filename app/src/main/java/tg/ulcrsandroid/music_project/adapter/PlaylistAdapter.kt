package tg.ulcrsandroid.music_project.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.PlaylistBinding
import tg.ulcrsandroid.music_project.model.Playlist
import tg.ulcrsandroid.music_project.view_holder.PlaylistViewHolder

class PlaylistAdapter(val playlists: RealmResults<Playlist>) : RecyclerView.Adapter<PlaylistViewHolder>(){
    lateinit var onItemClick: (ObjectId?) -> Unit
    lateinit var onEditClick: (ObjectId?) -> Unit
    lateinit var onDeletClick: (ObjectId?) -> Unit


    init {
        playlists.addChangeListener { _, changeSet ->
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val ui = PlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
    // modification ajouté :thibaute
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.playlist = playlists[position]
        holder.onItemClick = onItemClick
        /*holder.onEditClick = onEditClick
        holder.onDeleteClick = onDeletClick*/
    }

}