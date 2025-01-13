package tg.ulcrsandroid.music_project.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.ActivityCreatePlaylistBinding
import tg.ulcrsandroid.music_project.service.RequettesRealm

class CreatePlaylist : AppCompatActivity() {
    lateinit var ui: ActivityCreatePlaylistBinding
    lateinit var realm: Realm
    lateinit var requetteRealm: RequettesRealm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityCreatePlaylistBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.root.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        realm = Realm.getDefaultInstance()
        requetteRealm = RequettesRealm(realm)
        val idstr = intent.getStringExtra("idChanson")
        val songId = if (idstr != null) ObjectId(idstr) else null

        ui.btnCreatePlaylist.setOnClickListener{
            val nom = ui.etPlaylistName.text.toString()
            createPlaylist(nom, songId)
            finish()
        }
    }
    private fun createPlaylist(nom: String, idChanson: ObjectId?) {
        requetteRealm.createPlaylist(nom, idChanson)
        Toast.makeText(this, "Chanson ajoutée aux favoris", Toast.LENGTH_SHORT).show()
    }
}