package tg.ulcrsandroid.music_project.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.ActivityAddToPlaylistBinding

class AddToPlaylist : AppCompatActivity() {
    lateinit var ui: ActivityAddToPlaylistBinding
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityAddToPlaylistBinding.inflate(layoutInflater)
        setContentView(ui.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.main.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val idstr = intent.getStringExtra("idChanson")
        val songId = if (idstr != null) ObjectId(idstr) else null
        realm = Realm.getDefaultInstance()
        ui.create.setOnClickListener{
            startCreatePlaylist(songId)
        }
        ui.favBox.setOnClickListener {

        }
        ui.backIcon.setOnClickListener {
            finish()
        }
        ui.recentsBox.setOnClickListener {

        }
        ui.add.setOnClickListener {

        }
    }
    private fun startCreatePlaylist(idChanson: ObjectId?) {
        Log.i("TAG", "Lancement de Create playlist ")
        // Lancer l'activite main
        val intentPlaylist = Intent(this, CreatePlaylist::class.java)
        intentPlaylist.putExtra(MainActivity.EXTRA_ID, idChanson?.toHexString())
        startActivity(intentPlaylist)
        Log.i("TAG", "Fin de l'activite main ")
    }
}