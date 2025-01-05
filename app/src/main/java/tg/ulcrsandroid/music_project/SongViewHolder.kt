package tg.ulcrsandroid.music_project

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.SongBinding
import tg.ulcrsandroid.music_project.model.Chanson

class SongViewHolder(val ui: SongBinding): RecyclerView.ViewHolder(ui.root) {
    var idChanson: ObjectId? = null;
    var title: String? = null;
    lateinit var onEditClick: (ObjectId?) -> Unit
    lateinit var onItemClick: (ObjectId?) -> Unit
    lateinit var onDeleteClick: (Int) -> Unit // Callback pour la suppression
    init {
        ui.root.setOnClickListener { onClick() }
            // ui.menuIcon.setOnClickListener { view -> showPopupMenu(view) }
    }

    var song: Chanson? = null
        set(song) {
            if (song == null) return
            field = song
            idChanson = song.id
            ui.titre.text = song.titre
            val st = song.titre
            ui.duree.text = convertMilliSecondsToMin(song.duree)
            ui.artiste.text = song.artistePrincipal?.nom
            // ui.artiste.text = song.artistePrincipal?.nom
        }
    @SuppressLint("DefaultLocale")
    fun convertMilliSecondsToMin(duree: Long?) : String {
        val sec = duree?.div(1000)
        val min = (sec?.toFloat())?.div(60)
        val s = String.format("%.2f", min)
        return s.replace(',', ':')
    }
    private fun onClick() {
        Log.i("MYSIC", "clic sur une chanson $idChanson")
        onItemClick(idChanson)
    }
    // modification thibaute

    //Affiche le PopupMenu avec toutes les options

    /*
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.song_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    showDeleteConfirmationDialog(view.context)
                    Log.i("MUSIC", "supprimer la musique ")
                    true
                }
                R.id.edit -> {
                    onEditClick(idChanson)
                    Log.i("MUSIC", "Éditer la chanson")
                    true
                }
                R.id.add_to_favorites -> {
                    Log.i("MUSIC", "Ajouter aux favoris")
                    true
                }
                R.id.add_to_playlist -> {
                    Log.i("MUSIC", "Ajouter à la playlist")
                    true
                }
                else -> false
            }
        }
    }*/
    private fun showDeleteConfirmationDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmer la suppression")
        builder.setMessage("Supprimer cette chanson ?")
        builder.setPositiveButton("Oui") { dialog, _ ->
            onDeleteClick(adapterPosition) // Appel du callback pour supprimer
            dialog.dismiss()
        }
        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}