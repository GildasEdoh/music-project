package tg.ulcrsandroid.music_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tg.ulcrsandroid.music_project.databinding.ActivityDashBoardBinding

class DashBoard : AppCompatActivity() {
    private lateinit var ui: ActivityDashBoardBinding
    var TAG = "MUSIC"
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
        var musique_layout = ui.morceaux
        musique_layout.setOnClickListener {
            Log.i("DEBUG", "Clic sur morceaux")
            startMain()
        }

        var favoris_layout = ui.favoris
        favoris_layout.setOnClickListener {
            Log.i("DEBUG", "Clic sur favoris")
        }

        var recent_layout = ui.recents
        recent_layout.setOnClickListener{
            Log.i("DEBUG", "Clic sur recents")
        }

        var playlist_layout = ui.playlist
        playlist_layout.setOnClickListener{
            Log.i("DEBUG", "Clic sur playlist")
        }

    }
    fun startMain() {
        Log.i(TAG, "Lancement du du main ")
        // Lancer l'activite main
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
        // finish()
        Log.i(TAG, "Fin de l'activite main ")
    }
    fun startFavoris() {

    }
    fun startPlaylist() {

    }
    fun startRecents() {

    }
}