package ae.android.test.domain.usecase

import ae.android.test.data.model.ApiResponse
import ae.android.test.data.model.Book
import ae.android.test.domain.repository.BookRepository
import io.reactivex.Observable
import retrofit2.Response

class FetchBooksUseCase(private val bookRepository: BookRepository) {
    fun execute(): Observable<Response<ApiResponse>>{
        return bookRepository.fetchBooks()
    }

    fun searchBooks(title:String,author:String): List<Book>{
        return bookRepository.searchBooks(title = title, author = author)
    }

    fun getAllBooks(): List<Book>{
        return bookRepository.getAllBooks()
    }

    fun getBookById(id:String): Book?{
        return bookRepository.getBookById(id)
    }
}
