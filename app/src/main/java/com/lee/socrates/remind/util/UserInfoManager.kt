package com.lee.socrates.remind.util

import com.lee.socrates.remind.entity.User

/**
 * Created by lee on 2017/6/27.
 */
object UserInfoManager {

    var userMap: HashMap<String, User> = HashMap()
    var currentUserName: String = ""

    fun hasLoginUser(): Boolean {
        return getCurrentUser().isLogin
    }


    fun addUser(user: User?) {
        if (user !== null) {
            currentUserName = user.userAccount
            userMap.put(user.userAccount, user)
        }
    }

    fun getCurrentUser(): User {
        var user: User? =null
        if (currentUserName.isNotEmpty()){
            user = userMap[currentUserName]
        }
        if(user === null){
            user = User()
        }
        return user
    }

}