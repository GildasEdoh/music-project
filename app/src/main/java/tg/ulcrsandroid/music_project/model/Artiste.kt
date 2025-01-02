package tg.ulcrsandroid.music_project.model

import io.realm.RealmList
import io.realm.RealmObject

open class Artiste (): RealmObject() {
    var id : Int? = null
    var nom: String = ""
    var albums: RealmList<Album> = RealmList()
    var chansons: String = ""
}