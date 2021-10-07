package pe.edu.upc.composepractice.screens.people

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import pe.edu.upc.composepractice.data.local.AppDatabase
import pe.edu.upc.composepractice.data.models.User
import pe.edu.upc.composepractice.data.remote.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun People() {
    val users = remember { mutableStateOf(listOf<User>()) }
    val apiService = ApiClient.build()
    val fetchUsers = apiService.fetchUsers()

    fetchUsers.enqueue(object : Callback<List<User>> {
        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
            Log.d("Popular", response.body().toString())

            if (response.isSuccessful) {
                users.value = response.body()!!
            }
        }

        override fun onFailure(call: Call<List<User>>, t: Throwable) {
            Log.d("Popular", t.toString())
        }

    })

    Scaffold {
        UserList(users.value)
    }
}


@Composable
fun UserList(users: List<User>) {
    LazyColumn {
        items(users) { user ->
            UserRow(user)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserRow(user: User) {
    val context = LocalContext.current
    val favorite = remember { mutableStateOf(false) }
    favorite.value = (AppDatabase.getInstance(context).UserDao().fetchById(user.id).isNotEmpty())
    Card(
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(user.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(7f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    user.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    user.createdAt,
                    maxLines = 2,
                    style = MaterialTheme.typography.caption
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .size(32.dp),
                onClick = {
                    if (favorite.value)
                        AppDatabase.getInstance(context).UserDao().delete(user)
                    else
                        AppDatabase.getInstance(context).UserDao().insert(user)
                    favorite.value = !favorite.value
                }) {

                Icon(
                    Icons.Filled.Favorite,
                    "",
                    tint = if (favorite.value) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            }
        }
    }
}