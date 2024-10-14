package com.zaqly.eventdicoding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.zaqly.eventdicoding.api.model.ListEventsItem
import com.zaqly.eventdicoding.api.service.ApiConfig
import com.zaqly.eventdicoding.databinding.ActivityDetailEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", -1)
        if (eventId != -1) {
            fetchEventDetails(eventId)
        } else {
            Toast.makeText(this, "Invalid event ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchEventDetails(eventId: Int) {
        val call = ApiConfig.eventApiService.getEventDetails(eventId)
        call.enqueue(object : Callback<ListEventsItem> {
            override fun onResponse(call: Call<ListEventsItem>, response: Response<ListEventsItem>) {
                if (response.isSuccessful) {
                    val eventDetails = response.body()
                    eventDetails?.let { displayEventDetails(it) }
                } else {
                    Toast.makeText(this@DetailEventActivity, "Failed to fetch event details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ListEventsItem>, t: Throwable) {
                Toast.makeText(this@DetailEventActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayEventDetails(event: ListEventsItem) {
        binding.apply {
            tvJudulDetail.text = event.name ?: "No Name"
            tvSummaryDetail.text = event.summary ?: "No Summary"
            tvQuotaDetail.text = "Sisa Quota ${event.registrants ?: 0} / ${event.quota ?: 0}"
            tvJamDetail.text = "${event.beginTime ?: ""} - ${event.endTime ?: ""}"

            tvDescription.text = HtmlCompat.fromHtml(
                event.description ?: "No Description",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            Glide.with(this@DetailEventActivity)
                .load(event.mediaCover) // Ganti dengan URL gambar yang sesuai
                .centerCrop() // Atur sesuai dengan kebutuhan
                .into(ivDetail)

            btnRegister.setOnClickListener {
                event.link?.let { link ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                } ?: run {
                    Toast.makeText(this@DetailEventActivity, "No link available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}