package com.sharran.smsanalyzer.domain.model

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

/**
 * Created by SHARRAN on 22/6/20.
 */
open class Tag: RealmObject() {
    @PrimaryKey
    @Index
    var id: String = ""
    var tag: String? = null
}