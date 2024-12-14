package ae.android.test.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String="",
    val authors: String="",
    val image: String="",
    val url: String="",
    var isFavorite: Boolean=false


  /*
  //Sample response
  "id": "1098127463",
"title": "Security as Code",
"subtitle": "DevSecOps Patterns with AWS",
"authors": "BK Sarthak Das, Virginia Chu",
"image": "https://www.dbooks.org/img/books/1098127463s.jpg",
"url": "https://www.dbooks.org/security-as-code-1098127463/"*/

)