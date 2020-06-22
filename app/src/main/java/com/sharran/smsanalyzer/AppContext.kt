package com.sharran.smsanalyzer

import android.content.Context
import com.sharran.smsanalyzer.repository.SMSRepository
import io.realm.Realm

/**
 * Created by SHARRAN on 21/6/20.
 */

object AppContext {
    var context: Context? = null

    val realm: Realm by lazy {
        Realm.init(context!!)
        Realm.getDefaultInstance()
    }

    val smsRepository: SMSRepository by lazy { SMSRepository(realm) }
}