package tg.ulcrsandroid.music_project.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.SongBinding
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.view_holder.SongViewHolder

class SongAdapter(var songs: RealmResults<Chanson>) : RecyclerView.Adapter<SongViewHolder>(){
    lateinit var onItemClick: (ObjectId?) -> Unit
    lateinit var onEditClick: (ObjectId?) -> Unit
    lateinit var onDeletClick: (ObjectId?) -> Unit
    lateinit var onAddToFavorites: (ObjectId?) -> Unit
    private var filteredSongs: List<Chanson> = songs
    private var currentSongIndex = 0
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
        return filteredSongs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        Log.i("ViewHolder", "position $position")
        holder.song = filteredSongs[position]
        holder.onItemClick = onItemClick
        holder.onEditClick = onEditClick
        holder.onDeleteClick = onDeletClick
        holder.addToFavorites = onAddToFavorites
    }
    fun filter(query: String) {
        val searchQuery = query.lowercase()
        filteredSongs = if (searchQuery.isEmpty()) {
            songs.toList() // Convertir RealmResults en liste
        } else {
            songs.filter { chanson ->
                chanson.titre?.lowercase()?.contains(searchQuery) == true ||
                        chanson.artistePrincipal?.nom?.lowercase()?.contains(searchQuery) == true
            }
        }
        notifyDataSetChanged()
    }
    fun updateSongs(newSongs: RealmResults<Chanson>) {
        songs = newSongs
        filteredSongs = songs.toList() // Met à jour la liste filtrée
        notifyDataSetChanged()
    }
}