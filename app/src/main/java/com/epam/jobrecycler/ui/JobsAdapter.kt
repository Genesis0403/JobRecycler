package com.epam.jobrecycler.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.epam.jobrecycler.R
import com.epam.jobrecycler.model.Job

class JobsAdapter(private val jobs: List<Job>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return JobViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val jobItem = holder as JobViewHolder
        val job = jobs[position]
        job.apply {
            loadCompanyLogo(job.companyLogo, jobItem.companyLogo)
            jobItem.companyName.text = job.companyName
            jobItem.location.text = job.location
            jobItem.job.text = job.job
        }

    }

    inner class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyName: TextView = view.findViewById(R.id.companyName)
        val companyLogo: ImageView = view.findViewById(R.id.jobLogo)
        val location: TextView = view.findViewById(R.id.location)
        val job: TextView = view.findViewById(R.id.jobName)
    }

    private fun loadCompanyLogo(uri: String?, imageView: ImageView) {
        Glide.with(imageView)
            .load(uri)
            .apply(
                RequestOptions.centerCropTransform()
                    .fitCenter()
            )
            .error(R.drawable.photo_error_24dp)
            .into(imageView)
    }
}