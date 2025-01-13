package tg.ulcrsandroid.music_project.view_holder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.PlaylistBinding
import tg.ulcrsandroid.music_project.model.Playlist

class PlaylistViewHolder(val ui: PlaylistBinding): RecyclerView.ViewHolder(ui.root) {
    var idPlaylist: ObjectId? = null;
    var nom: String? = null;

    lateinit var onEditClick: (ObjectId?) -> Unit
    lateinit var onItemClick: (ObjectId?) -> Unit
    /*lateinit var onDeleteClick: (ObjectId?) -> Unit // Callback pour la suppression */

    init {
        ui.root.setOnClickListener { onClick() }
        // ui.menuIcon.setOnClickListener { view -> showPopupMenu(view) }
    }

    var playlist: Playlist? = null
        set(playlist) {
            if (playlist == null) return
            field = playlist
            idPlaylist = playlist.id
            ui.nom.text = playlist.nom
            ui.nbrSongs.text = playlist.chansons.count().toString()
        }
    private fun onClick() {
        Log.i("MYSIC", "clic sur une chanson $idPlaylist")
        onItemClick(idPlaylist)
    }

    // modification thibaute
    //Affiche le PopupMenu avec toutes les option

    private fun onEdit() {
        Log.i("MYSIC", "Edition d'une chanson $idPlaylist")
        // onEditClick(idChanson)
    }
}