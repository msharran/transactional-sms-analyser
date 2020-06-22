package com.sharran.smsanalyzer.ui.inbox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sharran.smsanalyzer.AppContext
import com.sharran.smsanalyzer.R
import com.sharran.smsanalyzer.constant.SMS_READ_PERMISSION_REQUEST_CODE
import com.sharran.smsanalyzer.domain.model.SMS
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.fragment_inbox.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.textChangedListener


class InboxFragment : Fragment() {

    private lateinit var inboxViewModel: InboxViewModel
    private lateinit var inboxRealmRecyclerAdapter: InboxRealmRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inboxViewModel = ViewModelProviders.of(this).get(InboxViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_inbox, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inboxRealmRecyclerAdapter = InboxRealmRecyclerAdapter(AppContext.smsRepository.findAll() as OrderedRealmCollection<SMS>)
        this@InboxFragment.activity?.apply {
            requestSMSReadPermission(this){
                initializeViews(this)
            }
        }
    }

    private fun initializeViews(fragmentActivity: FragmentActivity) {
        inboxRecyclerView.adapter = inboxRealmRecyclerAdapter
        inboxViewModel.refreshSmsInbox(fragmentActivity)
        inboxViewModel
            .SMSLiveData
            .observe(viewLifecycleOwner, Observer {
                inboxRealmRecyclerAdapter.updateData(it)
            })
        search_btn.setOnClickListener {
            inboxViewModel.searchByTag(search.text.toString())
        }
    }

    private fun requestSMSReadPermission(it: FragmentActivity, onSuccess: () -> Unit) {
        if (ContextCompat.checkSelfPermission(it, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            onSuccess()
        } else {
            ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_SMS), SMS_READ_PERMISSION_REQUEST_CODE)
        }
    }

}
