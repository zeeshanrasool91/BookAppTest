package ae.android.test.data.api

import ae.android.test.data.model.ApiResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET


interface BookApiService {
    @GET("recent")
    fun getBooks(): Observable<Response<ApiResponse>>
}