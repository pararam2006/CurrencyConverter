package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountDao: AccountDao) {
    suspend fun getAllAccounts(): List<AccountDbo> = accountDao.getAll()

    suspend fun insertAllAccounts(vararg accounts: AccountDbo) = accountDao.insertAll(*accounts)

    fun getAllAccountsAsFlow(): Flow<List<AccountDbo>> = accountDao.getAllAsFlow() // Добавляем Flow-метод
}