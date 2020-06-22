package com.sharran.smsanalyzer.ui.inbox

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.sharran.smsanalyzer.AppContext
import com.sharran.smsanalyzer.R
import com.sharran.smsanalyzer.constant.DATE_FORMAT_WITH_TIME
import com.sharran.smsanalyzer.domain.model.SMS
import com.sharran.smsanalyzer.repository.setTag
import com.sharran.smsanalyzer.util.toStringFormat
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.sms_inbox.view.*
import org.jetbrains.anko.padding


/**
 * Created by SHARRAN on 21/6/20.
 */

class InboxRealmRecyclerAdapter(
    data: OrderedRealmCollection<SMS>
)
    : RealmRecyclerViewAdapter<SMS, InboxRealmRecyclerAdapter.InboxViewHolder>(data, true) {

    inner class InboxViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(sms: SMS, itemView: View) {
            view.address.text = sms.address
            view.body.text = sms.body
            view.date.text = sms.dateSent?.toStringFormat(format = DATE_FORMAT_WITH_TIME)
            if (!sms.tag.isNullOrEmpty()) {
                view.sms_tag.visibility = View.VISIBLE
                view.sms_tag.text = "TAG: ${sms.tag}"
            }
            view.row_list_container.setOnClickListener {
                openTagPrompt(sms)
            }
        }

        private fun openTagPrompt(sms: SMS) {
            val context = AppContext.context
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Enter Tag")
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.setPadding(10,10,10,10)
            builder.setView(input)
            builder.setPositiveButton("save") { dialog, which ->
                if (input.text.toString().isNullOrEmpty()) {
                    view.sms_tag.visibility = View.GONE
                    sms.setTag(null)
                } else {
                    view.sms_tag.visibility = View.VISIBLE
                    view.sms_tag.text = "TAG: ${input.text.toString().toUpperCase()}"
                    sms.setTag(input.text.toString().toUpperCase())
                }
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sms_inbox, parent, false)
        return InboxViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InboxViewHolder, position: Int) {
        holder.bind(getItem(position)!!, holder.itemView)
    }
}