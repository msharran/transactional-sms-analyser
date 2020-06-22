package com.sharran.smsanalyzer.mapper

import android.database.Cursor
import androidx.core.text.isDigitsOnly
import com.sharran.smsanalyzer.AppContext
import com.sharran.smsanalyzer.domain.model.Expense
import com.sharran.smsanalyzer.domain.model.SMS
import java.util.*

/**
 * Created by SHARRAN on 21/6/20.
 */
object SMSMapper {
    fun mapToSMS(smsInboxCursor: Cursor): SMS {
        return SMS().apply {
            address = smsInboxCursor.getString(smsInboxCursor.getColumnIndex("address")).toString()
            body = smsInboxCursor.getString(smsInboxCursor.getColumnIndex("body")).toString()
            dateSent =
                Date(smsInboxCursor.getString(smsInboxCursor.getColumnIndex("date_sent")).toLong())
            id = "$address-${dateSent!!.time}"
            tag = AppContext.smsRepository.findTagBySmsId(id)?.tag
        }
    }

    fun getExpense(allSms: List<SMS>): Expense {
        var debit = 0L
        var credit = 0L
        for (sms in allSms) {
            var currencyDetected = false
            sms.body?.split(" ")?.forEach {
                if (currencyDetected && it.isDigitsOnly()){
                    if (sms.body!!.contains("debited", ignoreCase = true)){
                        debit += it.replace(",", "").replace(".", "").toLong()
                    }
                    if (sms.body!!.contains("credited", ignoreCase = true)){
                        credit += it.replace(",", "").replace(".", "").toLong()
                    }
                    return@forEach
                }
                currencyDetected = (it == "INR" || it == "Rs." || it.startsWith("Rs"))
            }
        }
        println(credit)
        println(debit)
        println(credit - debit)
        return Expense(total = credit - debit, credit = credit, debit = debit)
    }
}