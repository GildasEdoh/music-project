package tg.ulcrsandroid.music_project.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Playlist() : RealmObject() {
    @PrimaryKey var id: ObjectId? = null
    var nom:String? = null
    var dureeSecondes: Int = 0
    var dureeMinutes: Int = 0
    var chansons: RealmList<Chanson> =  RealmList()

    companion object {
        val ID = "id"
        val NOM = "nom"
        val CHANSONS = "chansons"
        val ARTISTEPRINCIPAL = "artistePrincipal"
    }
}