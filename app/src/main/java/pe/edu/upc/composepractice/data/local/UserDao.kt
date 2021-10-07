package pe.edu.upc.composepractice.data.local

import androidx.room.*
import pe.edu.upc.composepractice.data.models.User

@Dao
interface UserDao {

    @Query("select * from users")
    fun fetchUsers(): List<User>

    @Query("select * from users where id=:id")
    fun fetchById(id: Int): List<User>

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)
}