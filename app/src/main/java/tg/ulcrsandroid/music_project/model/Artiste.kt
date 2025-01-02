package tg.ulcrsandroid.music_project.model

import io.realm.RealmObject

class Artiste (
    var id : Int? = null,
    var nom: String = "",
    var albums: List<Album> = emptyList(),
    var chansons: String = ""
): RealmObject() {

}