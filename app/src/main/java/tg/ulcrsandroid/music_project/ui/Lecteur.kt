package tg.ulcrsandroid.music_project.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.AudioPlayerService
import tg.ulcrsandroid.music_project.R
import tg.ulcrsandroid.music_project.databinding.ActivityLecteurBinding
import tg.ulcrsandroid.music_project.service.RequettesRealm

class Lecteur : AppCompatActivity() {
    private var audioPlayerService: AudioPlayerService? = null
    private var isBound = false
    private lateinit var ui : ActivityLecteurBinding
    private var binder : AudioPlayerService.AudioPlayerBinder? = null
    private lateinit var requettesRealm: RequettesRealm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityLecteurBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.root.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Lier le service
        Intent(this, AudioPlayerService::class.java).also { intent ->
            startService(intent)  // Démarre le service
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        val idstr = intent.getStringExtra("idChanson")
        val songId = if (idstr != null) ObjectId(idstr) else null

        var realm = Realm.getDefaultInstance()
        requettesRealm = RequettesRealm(realm)
        val chansonRealm = requettesRealm.getChansonById(songId)
        chansonRealm?.let {
            // println("ID: ${it.id}, Titre: ${it.titre}, Artiste: ${it.artistePrincipal}")
        }
        ui.songTitle.text = chansonRealm?.titre

        // Boutton play/pause
        ui.play.setOnClickListener {
            if (isBound && audioPlayerService != null) {
                Log.e("MUSIC", "Service ${audioPlayerService?.isPlaying()}")
                if (audioPlayerService!!.isPlaying()) {
                    Log.i("DEBUG", "audioPlayerService $audioPlayerService")
                    audioPlayerService?.pause()
                    ui.play.setImageResource(R.drawable.jouer)
                    Log.i("MUSIC", "PAUSE de ${audioPlayerService}")
                } else {
                    audioPlayerService?.resume()
                    ui.play.setImageResource(R.drawable.pause_button)
                    Log.i("MUSIC", "RESUME de ${audioPlayerService}")
                }
            } else {
                Log.e("MUSIC", "Service non disponible")
            }
        }
        ui.backIcon.setOnClickListener {
            finishActivity()
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("LECTEUR", "Service connecté")
            binder = service as AudioPlayerService.AudioPlayerBinder
            audioPlayerService = binder!!.getService()

            // Vérifie la disponibilité du service
            if (audioPlayerService != null) {
                updateProgress()

                val duration = audioPlayerService?.getDuration() ?: 0
                ui.endTime.text = formatTime(duration)
                Log.i("LECTEUR", "Service prêt")
            }
            isBound = true
            Log.i("DEBUG", "Demarrage MAJ")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("DEBUG", "Pas deDemarrage MAJ")
            audioPlayerService = null
            isBound = false
        }
    }
    private fun updateProgress() {
        val progressBar = ui.progressBar
        val duration = audioPlayerService?.getDuration() ?: 0
        progressBar.max = duration

        val handler = Handler(mainLooper)
        val updateTask = object : Runnable {
            override fun run() {
                if (isBound) {
                    val currentPosition = audioPlayerService?.getCurrentPosition() ?: 0
                    progressBar.progress = currentPosition
                    ui.startTime.text = formatTime(currentPosition)
                    handler.postDelayed(this, 500)
                }
            }
        }
        handler.post(updateTask)
    }
    @SuppressLint("DefaultLocale")
    private fun formatTime(milliseconds: Int): String {
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
    private fun finishActivity () {
        finish()
    }

}