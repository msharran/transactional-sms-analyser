package com.sharran.smsanalyzer.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.sharran.smsanalyzer.AppContext
import com.sharran.smsanalyzer.R
import com.sharran.smsanalyzer.domain.model.Expense
import com.sharran.smsanalyzer.mapper.SMSMapper
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allSMS = AppContext.smsRepository.findAll()
        buildExpenseChart(SMSMapper.getExpense(allSMS), chart)
    }

    private fun buildExpenseChart(expense: Expense, pieChart: PieChart) {
        val entries = ArrayList<PieEntry>();
        entries.add(PieEntry(expense.credit.toFloat(), "Credited (INR)"));
        entries.add(PieEntry(expense.debit.toFloat(), "Debited (INR)"));
        entries.add(PieEntry(expense.total.toFloat(), "Total Expense (INR)"));
        val dataSet = PieDataSet(entries, "Expense Chart")
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }
}
