package pe.edu.upc.composepractice.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
fun Search(selectUser: (id: String)-> Unit) {

    val newuser = User(0, "", "", "")
    var user by remember { mutableStateOf(newuser) }

    var id by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current

    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = id,
            onValueChange = {
                id = it
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, null)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    val apiService = ApiClient.build()
                    val fetchUserById = apiService.fetchUserById(id)

                    fetchUserById.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            Log.d("Popular", response.body().toString())

                            if (response.isSuccessful) {
                                user = response.body()!!
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.d("Popular", t.toString())
                        }

                    })
                }
            )
        )
        //UserList(users)
        UserRow(user)
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