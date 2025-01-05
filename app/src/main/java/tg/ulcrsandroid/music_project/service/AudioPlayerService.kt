package tg.ulcrsandroid.music_project

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class AudioPlayerService : Service() {
    private val binder = AudioPlayerBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var urlPrec = ""

    inner class AudioPlayerBinder : Binder() {
        fun getService(): AudioPlayerService = this@AudioPlayerService
    }

    override fun onBind(intent: Intent): IBinder = binder

    fun play(url: String?) {
        this.stop()
        Log.i("CLASS SERVICEABILITY", "PLAY METHOD")
        if (mediaPlayer == null ) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener { start() }
                setOnErrorListener { _, _, _ -> false }
                prepareAsync()
            }
            isPlaying = true
        } else {
            mediaPlayer?.start()
            isPlaying = true
        }
    }
    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
        Log.i("CLASSE AUDIOPLAYERSERVICE", "PAUSE METHOD")
    }
    fun resume() {
        mediaPlayer?.start()
        isPlaying = true
        Log.i("CLASSE AUDIOPLAYERSERVICE", "RESTART METHOD")
    }

    fun stop() {
        Log.i("CLASSE AUDIOPLAYERSERVICE", "STOP METHOD")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun getDuration(): Int = mediaPlayer?.duration ?: 0
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying?: false
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url")  // Récupère l'URL envoyée par l'intention
        if (url != null) {
            play(url)  // Appelle la méthode play() pour lire la chanson
        }
        return START_STICKY  // Indique que le service doit être relancé si le système le tue
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("AUDIO_SERVICE", "Service créé")
    }
}
