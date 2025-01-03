package tg.ulcrsandroid.music_project

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
           // ui.artiste.text = song.arstistes[0]?.nom
        }
}