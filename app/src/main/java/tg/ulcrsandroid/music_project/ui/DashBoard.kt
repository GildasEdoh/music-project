package tg.ulcrsandroid.music_project.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import tg.ulcrsandroid.music_project.databinding.ActivityDashBoardBinding
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.service.RequettesRealm

class DashBoard : AppCompatActivity() {
    private lateinit var ui: ActivityDashBoardBinding
    var TAG = "MUSIC"
    private lateinit var realm: Realm
    private lateinit var requettesRealm : RequettesRealm
    private lateinit var realmResults: RealmResults<Chanson>
    private lateinit var realmChangeListenner: RealmChangeListener<RealmResults<Chanson>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(ui.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.root.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var musiqueLayout = ui.morceaux
        musiqueLayout.setOnClickListener {
            Log.i("DEBUG", "Clic sur morceaux")
            startMain()
        }

        var favorisLayout = ui.favoris
        favorisLayout.setOnClickListener {
            Log.i("DEBUG", "Clic sur favoris")
        }

        var recentLayout = ui.recents
        recentLayout.setOnClickListener{
            Log.i("DEBUG", "Clic sur recents")
        }

        var playlistLayout = ui.playlist
        playlistLayout.setOnClickListener{
            Log.i("DEBUG", "Clic sur playlist")
        }
        realm = Realm.getDefaultInstance()
        requettesRealm = RequettesRealm(realm)

        // Obtenir les chansons
        realmResults = realm.where(Chanson::class.java).findAll()


        realmChangeListenner = RealmChangeListener {
            // Met a jour le texte lorque les donnees changent
            ui.nbrSongs.text = realmResults.size.toString()
        }
        realmResults.addChangeListener(realmChangeListenner)

        // Nbre initial de morceaux
        ui.nbrSongs.text = realmResults.size.toString()
    }
    fun startMain() {
        Log.i(TAG, "Lancement du du main ")
        // Lancer l'activite main
        val intentMain = Intent(this, MainActivity::class.java)
        Log.i(TAG, "Fin de l'activite main ")
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intentMain)
        }, 100)
    }
    fun startFavoris() {

    }
    fun startPlaylist() {

    }
    fun startRecents() {

    }
}