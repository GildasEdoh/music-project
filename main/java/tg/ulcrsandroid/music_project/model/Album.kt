package tg.ulcrsandroid.music_project.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Album (): RealmObject() {
    @PrimaryKey var id: ObjectId? = null
    var nom: String = ""
    var chansons: RealmList<Chanson> =  RealmList()
    var artistePrincipal: Artiste? = null

    companion object {
        val ID = "id"
        val NOM = "nom"
        val CHANSONS = "chansons"
        val ARTISTEPRINCIPAL = "artistePrincipal"
    }
    fun addChansons(chanson :Chanson) {
        this.chansons.add(chanson)
    }
}