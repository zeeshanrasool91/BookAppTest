package ae.android.test.domain.usecase

import ae.android.test.data.model.Book
import ae.android.test.domain.repository.BookRepository

class UpdateBookUseCase(private val bookRepository: BookRepository) {
    suspend fun execute(book: Book) {
        bookRepository.updateBook(book)
    }
}