package tg.ulcrsandroid.music_project.view_holder

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.R
import tg.ulcrsandroid.music_project.databinding.SongBinding
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.model.Playlist

class PlaylistViewHolder(val ui: SongBinding): RecyclerView.ViewHolder(ui.root) {
    var idPlaylist: ObjectId? = null;
    var nom: String? = null;
    /*
    lateinit var onEditClick: (ObjectId?) -> Unit
    lateinit var onItemClick: (ObjectId?) -> Unit
    lateinit var onDeleteClick: (ObjectId?) -> Unit // Callback pour la suppression */

    companion object {
        val MENU_DELETE = 1
        val MENU_EDIT = 2
        val MENU_ADD_TO_FAV = 3
        val MENU_ADD_TO_PLAYLIST = 4
        val MENU_DETAILS = 5
        val MENU_PLAY_NEXT = 6
    }
    init {
        // ui.root.setOnClickListener { onClick() }
        // ui.menuIcon.setOnClickListener { view -> showPopupMenu(view) }
    }

    var playlist: Playlist? = null
        set(playlist) {
            if (playlist == null) return
            field = playlist
            idPlaylist = playlist.id
        }
    @SuppressLint("DefaultLocale")
    fun convertMilliSecondsToMin(duree: Long?) : String {
        val sec = duree?.div(1000)
        val min = (sec?.toFloat())?.div(60)
        val s = String.format("%.2f", min)
        return s.replace(',', ':')
    }
    private fun onClick() {
        Log.i("MYSIC", "clic sur une chanson $idPlaylist")
        // onItemClick(idChanson)
    }

    // modification thibaute
    //Affiche le PopupMenu avec toutes les option

    private fun onEdit() {
        Log.i("MYSIC", "Edition d'une chanson $idPlaylist")
        // onEditClick(idChanson)
    }
}