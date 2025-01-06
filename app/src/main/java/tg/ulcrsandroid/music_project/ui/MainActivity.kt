package tg.ulcrsandroid.music_project.ui
import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.AudioPlayerService
import tg.ulcrsandroid.music_project.adapter.SongAdapter
import tg.ulcrsandroid.music_project.databinding.ActivityMainBinding
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.model.SongData
import tg.ulcrsandroid.music_project.service.RequettesRealm

class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_CODE = 1001
    private lateinit var realm : Realm
    private lateinit var songAdapter: SongAdapter
    private lateinit var ui : ActivityMainBinding
    // private lateinit var audioPlayer : AudioPlayer
    private lateinit var requetteRealm : RequettesRealm
    private var chansonEnCous: Chanson? = null
    private var precChanson : Chanson? = null
    private var audioService: AudioPlayerService? = null
    private var isServiceBound = false

    var TAG = "MUSIC"

    companion object {
        val EXTRA_ID = "idChanson"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        //setSupportActionBar(ui.toolbar)
        setTitle("MYSIC")

        this.realm = Realm.getDefaultInstance()
        requetteRealm = RequettesRealm(realm)
        enableEdgeToEdge()
       // setContentView(R.layout.activity_main)
        Log.i("MUSIC", "COURS MMMMMMMMMMMMMM")
        checkAndRequestPermissions(this)

        // Connection au service de lecture
        val intent = Intent(this, AudioPlayerService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.recycler.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val songs = requetteRealm.getAllRealmSongs()
        val ite = songs.count()
        Log.i("DEBUG Main", "artiste: $ite")

        // Init the adapter
        songAdapter = SongAdapter(songs as RealmResults<Chanson>)
        ui.recycler.adapter = songAdapter
        ui.recycler.setHasFixedSize( true)

        // Ecouteur pour les clics sur les elements
        songAdapter.onItemClick = this::onItemClick
        songAdapter.onDeletClick = this::onDeleteClick
        songAdapter.onEditClick = this::onEditClick

        var search = ui.searchBar
    }
    // Lecture Chanson
    private fun onItemClick(idChanson: ObjectId?) {
        this.chansonEnCous = requetteRealm.getChansonById(idChanson)
        Log.i("ACTION", "Action sur la chanson ${chansonEnCous?.url}")
        if (this.chansonEnCous != null) {
            precChanson = this.chansonEnCous
            startLecteur(chansonEnCous)
            Log.i("ACTION", "Lecture de la chanson ${chansonEnCous?.url}")
        }
    }
    private fun onDeleteClick(idChanson: ObjectId?) {
        // Suppression Chanson
        Log.i("MAIN", "Suppression Chanson: $idChanson")
        val chanson = requetteRealm.getChansonById(idChanson)
        val check = audioService?.deleteFile(chanson?.url!!)
        Log.i("MAIN", "check: $check")
        Log.i("MAIN", "Suppression dans le stockage reussie: $idChanson")
        requetteRealm.deleteSongById(this, idChanson)
    }

    // Lancer edit activity
    private fun onEditClick(idChanson: ObjectId?) {
        Log.i("MAIN", "Onedit Click ${idChanson}")
        // Lancer l'activite main
        val intentEdit = Intent(this, EditActivity::class.java)
        intentEdit.putExtra(EXTRA_ID, idChanson?.toHexString())
        startActivity(intentEdit)
    }

    // Fonction pour lancer le lecteur
    private fun startLecteur(chanson: Chanson?) {
        Log.i(TAG, "Lancement du lecteur ")
        // Lancer le service lecteur
        val intentService = Intent(this, AudioPlayerService::class.java).apply {
            putExtra("url", chanson?.url)
        }
        startService(intentService)
        // Lancer l'activite Lecteur
        val intentLecteur = Intent(this, Lecteur::class.java)
        intentLecteur.putExtra("idChanson", chanson?.id?.toHexString())
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(intentLecteur)
        }, 1000)
    }
    //
    private fun checkAndRequestPermissions(context: Context) {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
           // val songs = scanMusicFiles(context)
            Log.i("PERMISSION", "Permissions accordées")
        } else {
            // Demander la permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_PERMISSION_CODE
            )
            Log.i("MUSIC", "PERMISION NON ACCORDEE ")
        }
    }
    //
    private fun scanMusicFiles(context: Context): List<Chanson> {
        var songs = mutableListOf<Chanson>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val realm = Realm.getDefaultInstance()
        cursor?.use {
            val songsData = getSongs(it)
            val count = songsData.size
            val countRealm = countRealmChanson(realm).toInt()

            requetteRealm.initListe()
            requetteRealm.insertRealmObject(songsData)

        }
        return songs
    }

    fun getSongs(cursor: Cursor): List<SongData> {
        var songs = mutableListOf<SongData>()
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val titre = cursor.getString(titleColumn)
            val artisteNom = cursor.getString(artistColumn)
            val albumNom = cursor.getString(albumColumn)
            val url = cursor.getString(dataColumn)
            val duree = cursor.getLong(durationColumn)

            songs.add(SongData(id, titre, artisteNom, albumNom, url, duree))
            //Log.i("MUSIC", "artiste: ${artisteNom}")
        }
        return songs
    }

    fun countRealmChanson(realm: Realm) : Long {
        return realm.where(Chanson::class.java).count()
    }
    //
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION", "Permissions accordées")
                // Recommencer la lecture des fichiers audio
                scanMusicFiles(this)
            } else {
                Log.i("MUSIC", "PERMISSION REFUSÉE")
            }
        }
    }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioPlayerService.AudioPlayerBinder
            audioService = binder.getService()
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
            isServiceBound = false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }
}