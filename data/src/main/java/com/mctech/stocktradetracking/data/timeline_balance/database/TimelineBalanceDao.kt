package com.mctech.stocktradetracking.data.timeline_balance.database

import androidx.room.*

@Dao
interface TimelineBalanceDao {
    @Transaction
    @Query("SELECT * FROM timeline_balance ORDER BY periodTag DESC")
    suspend fun loadCurrentPeriod(): TimelineBalanceEmbedded?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(period: TimelineBalanceDatabaseEntity)

    @Transaction
    @Delete
    suspend fun delete(period: TimelineBalanceDatabaseEntity)
}