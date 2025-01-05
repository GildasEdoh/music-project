package tg.ulcrsandroid.music_project.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.ActivityEditBinding
import tg.ulcrsandroid.music_project.model.Chanson

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var songId: ObjectId? = null
    private var realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songId = intent.getStringExtra("SONG_ID")?.let { ObjectId(it) }

        // Charger les détails de la chanson
        loadSongDetails()

        // Enregistrer les modifications
        binding.saveButton.setOnClickListener {
            saveSongDetails()
        }
    }

    private fun loadSongDetails() {
        val song = realm.where(Chanson::class.java).equalTo("id", songId).findFirst()
        song?.let {
            binding.songTitle.setText(it.titre)
            binding.songArtist.setText(it.artistePrincipal?.nom)
            //binding.songAlbum.setText(it.album)
        }
    }

    private fun saveSongDetails() {
        val title = binding.songTitle.text.toString()
        val artist = binding.songArtist.text.toString()
        val album = binding.songAlbum.text.toString()

        if (title.isBlank() || artist.isBlank()) {
            Toast.makeText(this, "Les champs Titre et Artiste sont obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        realm.executeTransaction {
            val song = it.where(Chanson::class.java).equalTo("id", songId).findFirst()
            song?.let {
                it.titre = title
                it.artistePrincipal?.nom = artist
               //it.album = album
            }
        }

        Toast.makeText(this, "Chanson sauvegardée avec succès", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
