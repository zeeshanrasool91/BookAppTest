package ae.android.test.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ApiResponse(
    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("total")
    @Expose
    val total: Int = 0,

    @SerializedName("books")
    @Expose
    val books: MutableList<Book> = mutableListOf(),
)