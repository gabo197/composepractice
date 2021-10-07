package pe.edu.upc.composepractice.data.remote

import pe.edu.upc.composepractice.data.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    fun fetchUsers() : Call<List<User>>

    @GET("users/{userId}")
    fun fetchUserById(@Path("userId") id: String ): Call<User>
}