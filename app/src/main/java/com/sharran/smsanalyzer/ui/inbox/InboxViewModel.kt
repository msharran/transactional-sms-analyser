package com.sharran.smsanalyzer.ui.inbox

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharran.smsanalyzer.AppContext
import com.sharran.smsanalyzer.constant.SMS_INBOX_CONTENT
import com.sharran.smsanalyzer.domain.model.SMS
import com.sharran.smsanalyzer.mapper.SMSMapper
import io.realm.RealmResults


class InboxViewModel : ViewModel() {

    private val _SMSLiveData = MutableLiveData<RealmResults<SMS>>().apply {
        value = AppContext.smsRepository.findAll()
    }
    val SMSLiveData: LiveData<RealmResults<SMS>> = _SMSLiveData

    @SuppressLint("Recycle")
    fun refreshSmsInbox(context: Context) {
        val allTransactionalSMS = mutableListOf<SMS>()
        val contentResolver: ContentResolver = context.contentResolver
        val smsInboxCursor: Cursor? = contentResolver.query(Uri.parse(SMS_INBOX_CONTENT), null, null, null, null)

        smsInboxCursor?.let {
            if (isBodyEmpty(smsInboxCursor)) return
            do {
                val address = smsInboxCursor.getString(smsInboxCursor.getColumnIndex("address")).toString()
                val body = smsInboxCursor.getString(smsInboxCursor.getColumnIndex("body")).toString()
                if (isTransactionalMsg(address, body)) {
                    allTransactionalSMS.add(SMSMapper.mapToSMS(smsInboxCursor))
                }
            } while (smsInboxCursor.moveToNext())
            saveAndNotifyChange(allTransactionalSMS)
        }
    }

    fun searchByTag(tag: String){
        println("::::::: search::::::::: $tag")
        if (tag.isEmpty()){
            refreshSmsInbox(AppContext.context!!)
            return
        }
        saveAndNotifyChange(AppContext.smsRepository.findSMSByTag(tag))
    }

    private fun isTransactionalMsg(address: String, body: String): Boolean {
        return when {
            address.contains("+") -> false
            address.isDigitsOnly() -> false
            body.contains("debited", ignoreCase = true) || body.contains("credited", ignoreCase = true) -> true
            else -> false
        }
    }

    private fun isBodyEmpty(smsInboxCursor: Cursor): Boolean {
        val indexBody: Int = smsInboxCursor.getColumnIndex("body")
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return true
        return false
    }

    private fun saveAndNotifyChange(allTransactionalSMS: MutableList<SMS>) {
        println(":::::::::::::::: ${allTransactionalSMS.size}")
        _SMSLiveData.value = AppContext
            .smsRepository
            .saveAndGet(allTransactionalSMS)
    }
}