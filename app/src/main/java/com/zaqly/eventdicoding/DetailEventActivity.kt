package com.zaqly.eventdicoding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.zaqly.eventdicoding.api.model.Event
import com.zaqly.eventdicoding.databinding.ActivityDetailEventBinding


class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val viewModel: DetailEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val eventId = intent.getStringExtra("EVENT_ID") ?: ""
        if (eventId.isNotEmpty()) {
            viewModel.fetchEventDetails(eventId)
            observeViewModel()
        } else {
            showError("Invalid event ID")
            finish()
        }

        binding.main.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) { event ->
            event?.let { displayEventDetails(it) }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                showError(message)
            }
        }
    }

    private fun displayEventDetails(event: Event) {
        binding.apply {
            supportActionBar?.title = event.name
            tvJudulDetail.text = event.name ?: "No Name"
            tvSummaryDetail.text = event.summary ?: "No Summary"
            tvQuotaDetail.text = "Sisa Quota ${event.registrants ?: 0} / ${event.quota ?: 0}"
            tvTanggalJamDetail.text = event.beginTime ?: "-"

            tvDescription.text = HtmlCompat.fromHtml(
                event.description ?: "No Description",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            Glide.with(this@DetailEventActivity)
                .load(event.mediaCover)
                .centerCrop()
                .into(ivEventDetail)

            btnRegister.setOnClickListener {
                event.link?.let { link ->
                    openLinkInBrowser(link)
                } ?: run {
                    showError("No link available")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openLinkInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progresBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}