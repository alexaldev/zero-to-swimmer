package com.alexallafi.app.data.database

import androidx.room.*
import com.alexallafi.app.domain.SwimmingWeek

@Dao
interface SwimSessionDao {

//    suspend fun getSessionsForWeek(week: SwimmingWeek): List<SwimSessionWithRounds>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwimSession(swimSession: SwimSession): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwimmingRounds(rounds: List<SwimmingRound>)

    @Transaction
    @Query("SELECT * FROM SwimSession WHERE id = :sessionId")
    suspend fun getSwimSessionWithRounds(sessionId: Int): SwimSessionWithRounds

    @Update
    suspend fun updateSwimSession(swimSession: SwimSession)

    @Delete
    suspend fun deleteSwimSession(swimSession: SwimSession)
}