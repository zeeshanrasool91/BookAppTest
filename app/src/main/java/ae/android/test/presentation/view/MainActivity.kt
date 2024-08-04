package ae.android.test.presentation.view

import ae.android.test.R
import ae.android.test.databinding.ActivityMainBinding
import ae.android.test.presentation.view.adapter.BookAdapter
import ae.android.test.presentation.viewmodel.BookViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Adding Changes for final Commit
    private val viewModel: BookViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val adapter = setUpAdapter()
        observerDataChange(adapter)
        observerLoader()
        observerUiMessage()
        performPullToRefresh()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchBooks()
    }

    private fun observerLoader() {
        viewModel.showLoader.observe(this) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
        }
    }

    private fun observerUiMessage() {
        viewModel.showUiMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun performPullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchBooks()
        }
    }

    private fun observerDataChange(adapter: BookAdapter) {
        viewModel.allBooks.observe(this) { books ->
            books?.let {
                adapter.submitList(it)
            }
        }
    }

    private fun setUpAdapter(): BookAdapter {
        val adapter = BookAdapter(onClick = { book ->
            showBookDetail(book.id)
        }, onFavoriteClick = { _, book ->
            viewModel.updateBook(book = book)
        })
        binding.rvBooks.adapter = adapter
        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.maxWidth = Int.MAX_VALUE
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
            .setBackgroundColor(Color.TRANSPARENT)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(searchString: String?): Boolean {
                viewModel.searchBooks(searchString)
                return false
            }

            override fun onQueryTextChange(searchString: String?): Boolean {
                viewModel.searchBooks(searchString)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun showBookDetail(bookId: String) {
        val customBottomSheet = DetailBottomSheetDialog.newInstance(
            bookId = bookId
        )
        customBottomSheet.show(supportFragmentManager, DetailBottomSheetDialog.TAG)
    }

}
