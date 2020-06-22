package com.sharran.smsanalyzer.repository

import com.sharran.smsanalyzer.domain.model.SMS
import com.sharran.smsanalyzer.domain.model.Tag
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults

/**
 * Created by SHARRAN on 21/6/20.
 */

class SMSRepository(private val realm: Realm) {

    private fun where() = realm.where(SMS::class.java)

    fun findAll(): RealmResults<SMS> = where().findAll()

    fun saveAndGet(allSms: List<SMS>): RealmResults<SMS> {
        RealmList<SMS>()
        realm.executeTransaction {
            it.insertOrUpdate(allSms)
        }
        return where().`in`("id", allSms.map { it.id }.toTypedArray()).findAll()
    }

    fun findTagBySmsId(id: String): Tag? {
        return realm.where(Tag::class.java).equalTo("id", id).findFirst()
    }

    fun findSMSByTag(tag: String): RealmResults<SMS> {
        return where()
            .contains("tag", tag, Case.INSENSITIVE)
            .findAll()
    }

}

fun SMS.setTag(tag: String?){
    if (this.isManaged) {
        realm.executeTransaction {
            this.tag = tag
            it.insertOrUpdate(this)
            it.insertOrUpdate(Tag().also { tag ->
                tag.id = this.id
                tag.tag = this.tag
            })
        }
    }
}