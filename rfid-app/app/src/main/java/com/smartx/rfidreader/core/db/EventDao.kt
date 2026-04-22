package com.smartx.rfidreader.core.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM rfid_events ORDER BY savedAt DESC")
    fun allFlow(): Flow<List<EventEntity>>

    @Query("SELECT * FROM rfid_events WHERE isSynced = 0 ORDER BY savedAt ASC")
    suspend fun pending(): List<EventEntity>

    @Query("SELECT COUNT(*) FROM rfid_events WHERE isSynced = 0")
    fun pendingCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM rfid_events")
    fun totalCountFlow(): Flow<Int>

    @Insert
    suspend fun insert(event: EventEntity): Long

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM rfid_events")
    suspend fun deleteAll()
}
