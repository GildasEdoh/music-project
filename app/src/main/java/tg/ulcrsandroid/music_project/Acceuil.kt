package tg.ulcrsandroid.music_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tg.ulcrsandroid.music_project.databinding.ActivityAcceuilBinding

class Acceuil : AppCompatActivity() {
    lateinit var ui : ActivityAcceuilBinding
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
        ui.startButton.setOnClickListener(this::onButtonStart)
    }


    private fun onButtonStart(view: View?) {
        Log.i(TAG, "Clic sur le button commencer ")
        // Lancer l'activite main
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
        Log.i(TAG, "Fin de l'activite main ")
    }
}