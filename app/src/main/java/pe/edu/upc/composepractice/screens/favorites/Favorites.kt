package pe.edu.upc.composepractice.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import pe.edu.upc.composepractice.data.local.AppDatabase
import pe.edu.upc.composepractice.data.models.User

@Composable
fun Favorites() {
    val context = LocalContext.current
    val users = remember { mutableStateOf(listOf<User>()) }
    users.value = AppDatabase.getInstance(context).UserDao().fetchUsers()

    Scaffold {
        UserList(users.value) { user ->
            users.value = users.value.filter { it != user }.toMutableList()
        }
    }
}


@Composable
fun UserList(users: List<User>, deleteUser: (User) -> Unit) {
    LazyColumn {
        items(users) { user ->
            UserRow(user) {
                deleteUser(it)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserRow(user: User, deleteUser: (User) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
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
                    AppDatabase.getInstance(context).UserDao().delete(user)
                    deleteUser(user)
                }) {
                Icon(Icons.Filled.Delete, null)
            }
        }
    }
}