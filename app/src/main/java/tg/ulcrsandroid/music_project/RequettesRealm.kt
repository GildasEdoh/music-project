package tg.ulcrsandroid.music_project

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.model.Chanson
import java.io.File

class RequettesRealm {

    fun getChansonById(idChanson: ObjectId?, realm: Realm) : Chanson? {
        var chanson: Chanson? = null
        realm.executeTransaction {
            chanson = it.where(Chanson::class.java)
                .equalTo(Chanson.ID, idChanson)
                .findFirst()
        }
        return chanson
    }
    fun countRealmObject(realm: Realm) : Long {
        return realm.where(Chanson::class.java).count()
    }
    fun increaseNbrPlay(idChanson: ObjectId?, realm: Realm) {
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
        var realm = Realm.getDefaultInstance()
        val chanson = getChansonById(idChanson, realm)

        printConfirmMessage(context, chanson) {
            realm.executeTransaction { transac ->
                val chansonAsupprimer = transac.where(Chanson::class.java)
                    .equalTo(Chanson.ID, chanson?.id)
                    .findFirst()
                chansonAsupprimer?.let {
                    if (deleteFromStorage(chansonAsupprimer.url!!)) {
                        println("Fichier supprimé avec succès.")
                    } else {
                        println("Erreur : Fichier introuvable ou non supprimé.")
                    }
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
}