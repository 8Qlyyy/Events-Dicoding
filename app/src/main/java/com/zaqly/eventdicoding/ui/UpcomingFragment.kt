package com.zaqly.eventdicoding.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaqly.eventdicoding.DetailEventActivity
import com.zaqly.eventdicoding.R
import com.zaqly.eventdicoding.api.adapter.AdapterActivity
import com.zaqly.eventdicoding.api.model.EventModel
import com.zaqly.eventdicoding.api.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpcomingFragment : Fragment() {

    private lateinit var adapter: AdapterActivity
    private lateinit var rvEvents: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvEvents = view.findViewById(R.id.rv_event_upcoming)
        progressBar = view.findViewById(R.id.progresbar_upcoming)

        setupRecyclerView()
        loadEvents()
    }

    private fun setupRecyclerView() {
        adapter = AdapterActivity(emptyList()) { eventId ->
            val intent = Intent(activity, DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
        }
        rvEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.adapter
        }
    }

    private fun loadEvents() {
        showLoading(true)
        ApiConfig.eventApiService.getUpcomingEvents().enqueue(object : Callback<EventModel> {
            override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                    adapter.updateEvents(events)
                } else {
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EventModel>, t: Throwable) {
                showLoading(false)
                showError("Network error: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}