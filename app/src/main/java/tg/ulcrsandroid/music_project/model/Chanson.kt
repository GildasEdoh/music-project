public class Chanson() { // Constructeur sans argument requis par Realm

    var id: Long = 0
    var title: String = ""
    var artist: String = ""
    var album: String = ""
    var data: String = ""
    var duration: Long = 0

    // Constructeur personnalisé
    constructor(id: Long, title: String, artist: String, album: String, data: String, duration: Long) : this() {
        this.id = id
        this.title = title
        this.artist = artist
        this.album = album
        this.data = data
        this.duration = duration
    }
}
