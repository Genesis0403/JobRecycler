package com.epam.jobrecycler.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epam.jobrecycler.R
import com.epam.jobrecycler.model.Job
import com.epam.jobrecycler.networking.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsFragment : Fragment() {

    private val _jobs: MutableList<Job> = mutableListOf()
    val jobs: List<Job> = _jobs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.jobs_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.jobsRecycler)
        recycler.apply {

            adapter = JobsAdapter(jobs)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
            loadJobs(adapter, view.findViewById(R.id.progressBar))
        }
    }

    private fun loadJobs(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?, progressBar: ProgressBar) {
        val call: Call<List<Job>> = NetworkService.retrofit.getJobs()
        call.enqueue(object : Callback<List<Job>> {
            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                Toast.makeText(context, "Failed downloading jobs...", Toast.LENGTH_LONG).show()
                Log.e(TAG, t.toString())
            }

            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    _jobs.addAll(response.body() as List<Job>)
                    adapter?.notifyDataSetChanged()
                }
            }

        })
    }

    companion object {

        private const val TAG = "JOBS FRAGMENT"

        fun newInstance() = JobsFragment()
    }
}