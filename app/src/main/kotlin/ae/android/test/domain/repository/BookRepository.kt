package ae.android.test.domain.repository

import ae.android.test.data.model.ApiResponse
import ae.android.test.data.model.Book
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import retrofit2.Response

interface BookRepository {
    fun fetchBooks(): Observable<Response<ApiResponse>>
    suspend fun updateBook(book: Book)
    fun getAllBooks(): List<Book>
    fun getFavoriteBooks(): LiveData<List<Book>>
    fun searchBooks(title:String,author:String): List<Book>
    fun getBookById(id:String): Book?
}