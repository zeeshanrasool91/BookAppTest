import ae.android.test.RxSchedulersOverrideRule
import ae.android.test.data.model.ApiResponse
import ae.android.test.data.model.Book
import ae.android.test.domain.usecase.FetchBooksUseCase
import ae.android.test.domain.usecase.UpdateBookUseCase
import ae.android.test.presentation.viewmodel.BookViewModel
import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class BookViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulersOverrideRule = RxSchedulersOverrideRule()

    @Mock
    private lateinit var fetchBooksUseCase: FetchBooksUseCase

    @Mock
    private lateinit var updateBookUseCase: UpdateBookUseCase

    private lateinit var bookViewModel: BookViewModel

    @Mock
    private lateinit var showLoaderObserver: Observer<Boolean>

    @Mock
    private lateinit var allBooksObserver: Observer<List<Book>>

    @Mock
    private lateinit var showUiMessageObserver: Observer<String>

    @Mock
    private lateinit var bookObserver: Observer<Book?>

    private val dispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        bookViewModel = BookViewModel(
            fetchBooksUseCase = fetchBooksUseCase,
            updateBookUseCase = updateBookUseCase,
            dispatcher = dispatcher,
            application = mock(Application::class.java)
        )
        bookViewModel.showLoader.observeForever(showLoaderObserver)
        bookViewModel.allBooks.observeForever(allBooksObserver)
        bookViewModel.showUiMessage.observeForever(showUiMessageObserver)
        bookViewModel.book.observeForever(bookObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchBooks_success() = runTest {
        // Mock the use case response
        val books = listOf(Book("1", "Title1", "Author1", "Author Name"))
        `when`(fetchBooksUseCase.execute()).thenReturn(
            Observable.just(
                Response.success(
                    ApiResponse(
                        books = books.toMutableList()
                    )
                )
            )
        )

        // Call the method
        bookViewModel.fetchBooks()

        delay(1000)
        dispatcher.scheduler.advanceUntilIdle()

        // Verify the LiveData changes
        verify(showLoaderObserver).onChanged(true)
        verify(showLoaderObserver).onChanged(false)
        verify(allBooksObserver).onChanged(books)
    }

    @Test
    fun fetchBooks_error() = runBlocking {
        // Mock the use case response
        `when`(fetchBooksUseCase.execute()).thenReturn(Observable.error(UnknownHostException()))

        // Mock offline data response
        val offlineBooks = listOf(Book("2", "OfflineTitle", "OfflineSubtitle", "OfflineAuthor"))
        `when`(fetchBooksUseCase.getAllBooks()).thenReturn(offlineBooks)

        // Call the method
        bookViewModel.fetchBooks()

        delay(1000)
        dispatcher.scheduler.advanceUntilIdle()

        // Verify the LiveData changes
        verify(showLoaderObserver).onChanged(true)
        verify(showLoaderObserver).onChanged(false)
        verify(showUiMessageObserver).onChanged("No Internet Available Showing Offline data")
        verify(allBooksObserver).onChanged(offlineBooks)
    }

    @Test
    fun updateBook_success() = runTest {
        // Mock a book
        val book = Book("1", "Title1", "Author1", "Author Name")

        // Call the method
        bookViewModel.updateBook(book)

        advanceUntilIdle()

        // Verify the use case was called
        verify(updateBookUseCase).execute(book)
    }

    @Test
    fun getBookById_success() = runTest {
        // Mock the use case response
        val book = Book("1", "Title1", "Author1", "Author Name")
        `when`(fetchBooksUseCase.getBookById("1")).thenReturn(book)

        // Call the method
        bookViewModel.getBookById("1")

        advanceUntilIdle()

        // Verify the LiveData changes
        verify(bookObserver).onChanged(book)
    }

    @Test
    fun searchBooks_withQuery() = runTest {
        // Mock the use case response
        val books = listOf(Book("1", "Title1", "Author1", "Author Name"))
        `when`(fetchBooksUseCase.searchBooks(title = "Title1", author = "Title1")).thenReturn(books)

        // Call the method
        bookViewModel.searchBooks("Title1")

        advanceUntilIdle()

        // Verify the LiveData changes
        verify(allBooksObserver).onChanged(books)
    }

    @Test
    fun searchBooks_withoutQuery() = runTest {
        // Mock the use case response
        val books = listOf(Book("1", "Title1", "Author1", "Author Name"))
        `when`(fetchBooksUseCase.getAllBooks()).thenReturn(books)

        // Call the method
        bookViewModel.searchBooks(null)

        advanceUntilIdle()

        // Verify the LiveData changes
        verify(allBooksObserver).onChanged(books)
    }
}
