package ae.android.test.presentation.view

import ae.android.test.R
import ae.android.test.databinding.ItemBookBinding
import ae.android.test.base.dislike
import ae.android.test.base.like
import ae.android.test.presentation.viewmodel.BookViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailBottomSheetDialog : BottomSheetDialogFragment() {
    private val viewModel: BookViewModel by viewModels()

    companion object {
        // Tag for identifying the dialog fragment
        val TAG = DetailBottomSheetDialog::class.java.simpleName
        const val BOOK_ID = "book_id"


        // Factory method to create a new instance with arguments
        fun newInstance(bookId: String) = DetailBottomSheetDialog().apply {
            arguments = bundleOf(BOOK_ID to bookId)
        }
    }

    // Lazily initialize the binding for the bottom sheet layout
    private val binding by lazy { ItemBookBinding.inflate(layoutInflater) }

    // Lazily retrieve the BottomSheetContent from arguments
    private val bookId by lazy {
        requireArguments().getString(BOOK_ID).orEmpty()
    }


    // onCreateView to inflate the layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    // onViewCreated to initialize UI components and set up actions
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    // Define the theme for the bottom sheet
    override fun getTheme() = R.style.Theme_BottomSheetDialog

    // onCreateDialog to customize the dialog appearance
    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).apply {
            // Set the dialog to not be dismissible on touch outside
            setCanceledOnTouchOutside(true)
        }

    // Bind UI components with data from BottomSheetContent
    private fun bindUi() = with(binding) {
        viewModel.getBookById(bookId)
        viewModel.book.observe(viewLifecycleOwner) { book ->
            book?.let {
                binding.txtBookName.text =
                    String.format(binding.root.context.getString(R.string.book_name), book.title)
                binding.txtAuthorName.text = String.format(
                    binding.root.context.getString(R.string.author_name),
                    book.authors
                )
                binding.imgBookImage.load(book.image) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    transformations(RoundedCornersTransformation())
                }
                if (book.isFavorite) {
                    binding.bookLikeButton.like()
                } else {
                    binding.bookLikeButton.dislike()
                }
                binding.bookLikeButton.isEnabled = false
            } ?: run {
                dismiss()
            }
        }
    }
}