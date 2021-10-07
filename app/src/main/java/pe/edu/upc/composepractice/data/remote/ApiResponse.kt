package pe.edu.upc.composepractice.data.remote

import com.google.gson.annotations.SerializedName
import pe.edu.upc.composepractice.data.models.User

class ApiResponse(
    @SerializedName("results")
    val users: List<User>
)