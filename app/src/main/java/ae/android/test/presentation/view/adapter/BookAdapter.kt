package ae.android.test.presentation.view.adapter

import ae.android.test.R
import ae.android.test.base.dislike
import ae.android.test.base.like
import ae.android.test.data.model.Book
import ae.android.test.databinding.ItemBookBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation

class BookAdapter(
    private val onClick: (Book) -> Unit,
    private val onFavoriteClick: (Boolean, Book) -> Unit
) :
    ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.txtBookName.text =
                String.format(binding.root.context.getString(R.string.book_name), book.title)
            binding.txtAuthorName.text =
                String.format(binding.root.context.getString(R.string.author_name), book.authors)
            binding.root.setOnClickListener { onClick(book) }
            binding.root.setOnLongClickListener {
                book.isFavorite = book.isFavorite.not()
                onFavoriteClick(book.isFavorite, book)
                book.isFavorite
            }
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
            binding.bookLikeButton.setOnCheckedChangeListener { buttonView, isChecked ->
                book.isFavorite = isChecked
                onFavoriteClick(isChecked, book)
            }
        }
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id || oldItem.isFavorite == newItem.isFavorite
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}
