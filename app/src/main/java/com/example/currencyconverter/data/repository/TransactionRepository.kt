package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    suspend fun getAllTransactions() = transactionDao.getAll()

    suspend fun insertAllTransactions(vararg transactions: TransactionDbo) = transactionDao.insertAll(*transactions)

    suspend fun getAllTransactionsAsFlow(): Flow<List<TransactionDbo>> = transactionDao.getAllAsFlow()
}