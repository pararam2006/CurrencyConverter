package com.example.currencyconverter.data.dataSource.room.transaction.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertAll(vararg transactions: TransactionDbo)

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionDbo>

    @Query("SELECT * FROM transactions")
    fun getAllAsFlow(): Flow<List<TransactionDbo>>
}