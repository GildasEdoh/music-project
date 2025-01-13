package tg.ulcrsandroid.music_project.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.adapter.PlaylistAdapter
import tg.ulcrsandroid.music_project.databinding.ActivityPlaylistBinding
import tg.ulcrsandroid.music_project.model.Playlist
import tg.ulcrsandroid.music_project.service.RequettesRealm

class PlaylistActivity : AppCompatActivity() {
    private lateinit var ui: ActivityPlaylistBinding
    private lateinit var playlistadapter: PlaylistAdapter
    private lateinit var realm: Realm
    lateinit var raquettesRealm: RequettesRealm
    lateinit var playlistAdapter: PlaylistAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(ui.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.recycler.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        realm = Realm.getDefaultInstance()
        raquettesRealm = RequettesRealm(realm)
        val playlists = raquettesRealm.getAllPlaylists()
        val ite = playlists?.count()
        Log.i("DEBUG Main", "plalist: $ite")

        // Init the adapter
        playlistAdapter = PlaylistAdapter(playlists as RealmResults<Playlist>)
        ui.recycler.adapter = playlistAdapter
        ui.recycler.setHasFixedSize(true)

        playlistAdapter.onItemClick = this::onItemClick
        ui.backIcon.setOnClickListener {
            finish()
        }
    }
    private fun onItemClick(idChanson: ObjectId?) {

    }
}