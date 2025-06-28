package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo

class AccountRepository(private val accountDao: AccountDao) {
    suspend fun getAllAccounts() = accountDao.getAll()

    suspend fun insertAllAccounts(vararg accounts: AccountDbo) = accountDao.insertAll(*accounts)
}