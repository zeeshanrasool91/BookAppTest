package ae.android.test.presentation.viewmodel

import ae.android.test.data.model.Book
import ae.android.test.domain.usecase.FetchBooksUseCase
import ae.android.test.domain.usecase.UpdateBookUseCase
import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val fetchBooksUseCase: FetchBooksUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    application: Application
) : AndroidViewModel(application) {

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _allBooks = MutableLiveData<List<Book>>()
    val allBooks: LiveData<List<Book>> = _allBooks

    private val _showUiMessage = MutableLiveData<String>()
    val showUiMessage: LiveData<String> = _showUiMessage

    private val _book = MutableLiveData<Book?>()
    val book: LiveData<Book?> = _book

    @SuppressLint("CheckResult")
    fun fetchBooks() {
        toggleLoader(true)
        fetchBooksUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ books ->
                toggleLoader(false)
                _allBooks.value = books.body()?.books
            }, { error ->
                toggleLoader(false)
                if (error is UnknownHostException) {
                    getAllOfflineBooks()
                }
            })
    }

    fun updateBook(book: Book) = viewModelScope.launch {
        updateBookUseCase.execute(book)
    }

    fun getBookById(id: String) = viewModelScope.launch {
        val book = withContext(dispatcher) {
            fetchBooksUseCase.getBookById(id)
        }
        withContext(Dispatchers.Main) {
            _book.value = book
        }
    }

    private fun getAllOfflineBooks() = viewModelScope.launch(dispatcher) {
        val books = withContext(dispatcher) {
            fetchBooksUseCase.getAllBooks()
        }
        withContext(Dispatchers.Main) {
            if (books.isNotEmpty()) {
                _showUiMessage.value = "No Internet Available Showing Offline data"
            }
            _allBooks.value = books
        }
    }

    private fun toggleLoader(show: Boolean = true) {
        _showLoader.value = show
    }

    fun searchBooks(searchString: String?) = viewModelScope.launch {
        val books = withContext(dispatcher) {
            when {
                searchString.isNullOrEmpty() -> {
                    fetchBooksUseCase.getAllBooks()
                }

                else -> {
                    fetchBooksUseCase.searchBooks(title = searchString, author = searchString)
                }
            }
        }
        _allBooks.value = books
    }
}
