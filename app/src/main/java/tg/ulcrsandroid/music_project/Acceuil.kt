package tg.ulcrsandroid.music_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tg.ulcrsandroid.music_project.databinding.ActivityAcceuilBinding

class Acceuil : AppCompatActivity() {

    private lateinit var ui: ActivityAcceuilBinding
    private val totalDuration = 5000L // Temps total de chargement (en ms)
    private val updateInterval = totalDuration / 100 // Intervalle de mise à jour
    private var progress = 0 // Progression initiale
    var TAG = "MUSIC"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ui = ActivityAcceuilBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Configurer la barre de chargement et le texte
        val progressBar: ProgressBar = ui.loadingBar
        val loadingText: TextView = ui.loadingText

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (progress <= 100) {
                    // Mettre à jour la barre de progression
                    progressBar.progress = progress
                    // Mettre à jour le texte de progression
                    loadingText.text = "Loading... $progress%"
                    // Incrémenter la progression
                    progress++
                    handler.postDelayed(this, updateInterval)
                } else {
                    // Lorsque le chargement est terminé, passer à l'écran suivant
                    startActivity(Intent(this@Acceuil, DashBoard::class.java))
                    finish()
                }
            }
        }

        // Démarrer le chargement
        handler.post(runnable)

    }


    private fun onButtonStart(view: View?) {
        Log.i(TAG, "Clic sur le button commencer ")
        // Lancer l'activite main
        val intentMain = Intent(this, DashBoard::class.java)
        startActivity(intentMain)
        Log.i(TAG, "Fin de l'activite main ")
    }
}