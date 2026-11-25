package com.example.quizapp

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "Users")
data class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "highScore") val highScore: Int = 0,
)

data class LeaderboardEntry (
    val username: String,
    val highScore: Int,
)

@Dao
interface UserDao {
    @Insert fun insertUser(vararg c: User)

    @Query("SELECT username, highScore FROM Users ORDER BY highScore DESC")
    fun getAllLeaderboard(): Flow<List<LeaderboardEntry>>

    @Query("SELECT password FROM Users WHERE username=:username")
    suspend fun getUserPassword(username: String): String?
}

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase:: class.java, "people_db"
                ).build()
            }
        }
    }
}