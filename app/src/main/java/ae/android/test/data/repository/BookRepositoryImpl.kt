package ae.android.test.data.repository

import ae.android.test.data.db.BookDao
import ae.android.test.data.model.ApiResponse
import ae.android.test.data.model.Book
import ae.android.test.data.api.BookApiService
import ae.android.test.domain.repository.BookRepository
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Response

class BookRepositoryImpl(
    private val bookDao: BookDao,
    private val bookApiService: BookApiService
) : BookRepository {
    override fun fetchBooks(): Observable<Response<ApiResponse>> {
        return bookApiService.getBooks()
            .doOnNext { response: Response<ApiResponse> ->
                response.body()?.let { responseBody ->
                    MainScope().launch {
                        //bookDao.insertBooks(responseBody.books)
                        bookDao.updateIfExists(responseBody.books)
                    }
                }
            }
    }

    override suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    override fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks()
    }

    override fun searchBooks(tite: String, author: String): List<Book> {
        return bookDao.searchBooks(titleSearchQuery = tite, authorSearchQuery = author)
    }

    override fun getBookById(id: String): Book? {
        return bookDao.getBookById(searchId = id)
    }

    override fun getFavoriteBooks(): LiveData<List<Book>> {
        return bookDao.getFavoriteBooks()
    }
}
