package pe.edu.upc.composepractice.data.models

import android.media.Image
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "users")
data class User (

    @PrimaryKey
    val id: Int,

    val createdAt: String,

    val name: String,

    val avatar: String
) {

}