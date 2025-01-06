package tg.ulcrsandroid.music_project.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.ActivityEditBinding
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.service.RequettesRealm

class EditActivity : AppCompatActivity() {

    private lateinit var ui: ActivityEditBinding
    private var songId: ObjectId? = null
    private var realm: Realm = Realm.getDefaultInstance()
    private val requettesRealm = RequettesRealm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityEditBinding.inflate(layoutInflater)
        setContentView(ui.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.root.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        realm = Realm.getDefaultInstance()
        val idstr = intent.getStringExtra("idChanson")
        songId = if (idstr != null) ObjectId(idstr) else null
        Log.i("EDIT", "Edit $$songId")

        // Charger les détails de la chanson
        loadSongDetails()

        // Enregistrer les modifications
        ui.saveIcon.setOnClickListener {
           // saveSongDetails()
        }
        ui.submit.setOnClickListener {
            saveSongDetails()
            Log.i("EDIT ACT", "Submission")
        }
    }

    private fun loadSongDetails() {
        val song = requettesRealm.getChansonById(songId, realm)
        Log.i("DEBUG", "song: ${song?.titre}")
            ui.songTitle.setText(song?.titre)
            ui.songArtist.setText(song?.artistePrincipal?.nom)
            //ui.songAlbum.setText(it.album)
    }

    private fun saveSongDetails() {
        val title = ui.songTitle.text.toString()
        val artist = ui.songArtist.text.toString()
        val album = ui.songAlbum.text.toString()

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
