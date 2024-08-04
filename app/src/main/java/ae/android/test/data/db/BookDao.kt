package ae.android.test.data.db

import ae.android.test.data.model.Book
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books WHERE isFavorite = 1")
    fun getFavoriteBooks(): LiveData<List<Book>>


    @Query("SELECT * FROM books WHERE title LIKE '%' || :titleSearchQuery || '%' OR authors LIKE '%' || :authorSearchQuery || '%'")
    fun searchBooks(titleSearchQuery: String,authorSearchQuery:String): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    @Transaction
    suspend fun updateIfExists(books: List<Book>) {
        books.forEach { bookFromApi ->
            getBookById(bookFromApi.id)?.let { book ->
                bookFromApi.isFavorite = book.isFavorite
                updateBook(bookFromApi)
            } ?: run {
                insertBook(bookFromApi)
            }
        }
    }

    @Query("SELECT * FROM books WHERE id = :searchId")
    fun getBookById(searchId: String): Book?

    @Update
    suspend fun updateBook(book: Book)

    @Insert
    suspend fun insertBook(book: Book)
}