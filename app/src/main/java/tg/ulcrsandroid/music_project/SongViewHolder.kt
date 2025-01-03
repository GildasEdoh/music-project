package tg.ulcrsandroid.music_project

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.SongBinding
import tg.ulcrsandroid.music_project.model.Chanson

class SongViewHolder(val ui: SongBinding): RecyclerView.ViewHolder(ui.root) {
    var idChanson: ObjectId? = null
    var title: String? = null

    var song: Chanson? = null
        set(song) {
            if (song == null) return
            field = song
            ui.titre.text = song.titre
            val st = song.titre
            ui.duree.text = convertMilliSecondsToMin(song.duree)
            //ui.artiste.text = song.artistePrincipal?.nom
        }
    @SuppressLint("DefaultLocale")
    fun convertMilliSecondsToMin(duree: Long?) : String {
        val sec = duree?.div(1000)
        val min = (sec?.toFloat())?.div(60)
        val s = String.format("%.2f", min)
        return s.replace(',', ':')
    }
}