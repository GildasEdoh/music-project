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
import tg.ulcrsandroid.music_project.service.RequettesRealm

class EditActivity : AppCompatActivity() {

    private lateinit var ui: ActivityEditBinding
    private var songId: ObjectId? = null
    private lateinit var realm: Realm
    private lateinit var requettesRealm : RequettesRealm

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
        requettesRealm = RequettesRealm(realm)

        val idstr = intent.getStringExtra("idChanson")
        songId = if (idstr != null) ObjectId(idstr) else null
        Log.i("EDIT", "Edit $$songId")

        // Charger les détails de la chanson
        loadSongDetails()

        // Enregistrer les modifications
        ui.saveIcon.setOnClickListener {
           // saveModifs()
        }
        ui.submit.setOnClickListener {
            saveModifs()
            Log.i("EDIT ACT", "Submission")
        }
        ui.backIcon.setOnClickListener {
            finish()
        }
    }

    private fun loadSongDetails() {
        val song = requettesRealm.getChansonById(songId)
        Log.i("DEBUG", "song: ${song?.titre}")
            ui.songTitle.setText(song?.titre)
            ui.songArtist.setText(song?.artistePrincipal?.nom)
            //ui.songAlbum.setText(it.album)
    }

    private fun saveModifs() {
        val title = ui.songTitle.text.toString()
        val artist = ui.songArtist.text.toString()
        val album = ui.songAlbum.text.toString()

        if (title.isBlank() || artist.isBlank()) {
            Toast.makeText(this, "Les champs Titre et Artiste sont obligatoires", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the modifications
        requettesRealm.editChansonById(title, artist, songId)
        Toast.makeText(this, "Chanson sauvegardée avec succès", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
