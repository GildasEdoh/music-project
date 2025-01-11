package tg.ulcrsandroid.music_project.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.model.Album
import tg.ulcrsandroid.music_project.model.Artiste
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.model.SongData
import java.io.File

class RequettesRealm (realm: Realm){
    private var realm = realm
    fun getChansonById(idChanson: ObjectId?) : Chanson? {
        var chanson: Chanson? = null
        realm.executeTransaction {
            chanson = it.where(Chanson::class.java)
                .equalTo(Chanson.ID, idChanson)
                .findFirst()
        }
        return chanson
    }
    fun countRealmChanson() : Long {
        return realm.where(Chanson::class.java).count()
    }
    // Supprime la chanson de l'appareil
    fun printConfirmMessage(context: Context, chanson: Chanson?, onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation de suppression")
        builder.setMessage("Voulez-vous vraiment supprimer la chanson '${chanson?.titre}' ?")

        builder.setPositiveButton("Supprimer") { _, _ ->
            // Action à exécuter si l'utilisateur confirme
            onConfirm()
        }

        builder.setNegativeButton("Annuler") { dialog, _ ->
            // Annuler la suppression
            dialog.dismiss()
        }

        // Affichez la boîte de dialogue
        builder.create().show()
    }
    fun deleteSongById(context: Context, idChanson: ObjectId?) {
        val chanson = getChansonById(idChanson)
        printConfirmMessage(context, chanson) {
            realm.executeTransaction { transac ->
                val chansonAsupprimer = transac.where(Chanson::class.java)
                    .equalTo(Chanson.ID, chanson?.id)
                    .findFirst()
                chansonAsupprimer?.let {

                    /*if (deleteFromStorage(chansonAsupprimer.url!!)) {
                        println("Fichier supprimé avec succès.")
                    } else {
                        println("Erreur : Fichier introuvable ou non supprimé.")
                    }*/
                    it.deleteFromRealm()
                    Toast.makeText(context, "Chanson supprimée avec succès.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun deleteFromStorage(url: String) : Boolean{
        val fichier = File(url)
        if (fichier.exists()) {
            Log.i("SUPPRESION", "sup ${url}")

            return fichier.delete()
        }
        return false
    }
    fun editChansonById(title: String, artist: String, songId: ObjectId?) {
        realm.executeTransaction {
            val song = it.where(Chanson::class.java).equalTo("id", songId).findFirst()
            song?.let {
                it.titre = title
                it.artistePrincipal?.nom = artist
                //it.album = album
            }
        }
    }
    fun initListe() {
        realm.executeTransactionAsync { r ->
            // Supprime tous les X-Men existants dans Realm
            Log.i("Mise a jour .....", "maj")
            r.where(Chanson::class.java).findAll().deleteAllFromRealm()
        }
    }
    fun insertRealmObject(songs : List<SongData>): Realm {
        realm.executeTransactionAsync ({ transac ->
            for (song in songs) {
                // Recuperer l'artiste
                val art = transac.where(Artiste::class.java)
                    .equalTo(Artiste.NOM, song.artiste)
                    .findFirst() ?: transac.createObject(Artiste::class.java, ObjectId()).apply {
                    this.nom = song.artiste
                }

                // Recuperer ou creer l'album
                var alb = transac.where(Album::class.java)
                    .equalTo(Album.NOM, song.album)
                    .findFirst() ?: transac.createObject(Album::class.java, ObjectId()).apply {
                    this.nom = song.album
                }

                val id = ObjectId()
                // Creer la chanson
                var chanson = transac.createObject(Chanson::class.java, id).apply {
                    this.titre = song.titre
                    this.duree = song.duree
                    this.artistePrincipal = art/* Artiste().apply {
                        this.nom = song.artiste
                    }*/
                    this.album = alb/*Album().apply {
                        this.nom = song.album
                    }*/
                    this.url = song.url
                }
                Log.i("MUSIC", "titre deb: ${chanson.titre}")

                art.chansons.add(chanson)
                alb.chansons.add(chanson)
            }
        }, {
            Log.i("MUSIC", "Transaction reussi")

        }, { error ->
            Log.e("Realm", "Transaction échouée : ${error.localizedMessage}")
        })
        return realm
    }
     fun addSongToFavorite(idChanson: ObjectId?) {
         Log.i("Requettes Class", "Fonction addSongToFavorite")
        realm.executeTransaction {
            val chanson = it.where(Chanson::class.java).equalTo(Chanson.ID, idChanson)
                .findFirst()
            chanson?.isFavorite = true
        }
        val song = getChansonById(idChanson)
         Log.i("Requettes Realm", "chansonFav: ${song?.isFavorite}")
    }
    fun  getAllRealmSongs() : List<Chanson> {
        return realm.where(Chanson::class.java).findAllAsync()
    }
    fun getFavSong() : RealmResults<Chanson>? {
        realm = Realm.getDefaultInstance()
        var favSongs: RealmResults<Chanson>? = null
        realm.executeTransactionAsync {
            favSongs = it.where(Chanson::class.java)
                .equalTo(Chanson.ISFAV, true)
                .findAll()
            Log.i("Requette", "fsongs ${favSongs}")
        }
        return favSongs
    }
}