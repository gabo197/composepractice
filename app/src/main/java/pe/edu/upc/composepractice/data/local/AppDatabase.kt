package pe.edu.upc.composepractice.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.edu.upc.composepractice.data.models.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao() : UserDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, AppDatabase::class.java, "userapp.db")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}