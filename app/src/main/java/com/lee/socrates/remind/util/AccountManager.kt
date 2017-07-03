package com.lee.socrates.remind.util

import com.lee.socrates.remind.entity.Account

/**
 * Created by lee on 2017/6/27.
 */
object AccountManager {

    var accountMap: HashMap<String, Account> = HashMap()
    var currentAccountName: String = ""

    fun hasLoginUser(): Boolean {
        return getCurrentAccount().isLogin
    }


    fun addUser(account: Account?) {
        if (account !== null) {
            currentAccountName = account.accountName
            accountMap.put(account.accountName, account)
        }
    }

    fun getCurrentAccount(): Account{
        var account: Account? =null
        if (currentAccountName.isNotEmpty()){
            account = accountMap[currentAccountName]
        }
        if(account === null){
            account = Account()
        }
        return account
    }

}