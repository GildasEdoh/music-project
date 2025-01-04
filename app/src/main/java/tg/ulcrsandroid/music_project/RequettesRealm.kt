package tg.ulcrsandroid.music_project

import io.realm.Realm
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.model.Chanson

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

}