package com.sharran.smsanalyzer.domain.model

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by SHARRAN on 21/6/20.
 */
open class SMS: RealmObject() {
    @PrimaryKey
    @Index
    var id: String = ""
    var address: String? = null
    var body: String? = null
    var dateSent: Date? = null
    var tag: String? = null
}