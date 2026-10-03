package com.dev.timeflow.Data.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dev.timeflow.Data.Model.CountDown
import kotlinx.coroutines.flow.Flow

@Dao
interface CountdownDao {

    @Query("SELECT * FROM countdown_table ORDER BY endTime ASC")
    fun getAll(): Flow<List<CountDown>>

    @Query("SELECT * FROM countdown_table WHERE id = :id")
    suspend fun getById(id: Long): CountDown?

    @Insert
    suspend fun insert(countdown: CountDown) : Long

    @Update
    suspend fun update(countdown: CountDown)

    @Delete
    suspend fun delete(countdown: CountDown)
}