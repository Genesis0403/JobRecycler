package com.epam.jobrecycler.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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

    private lateinit var connectivityManager: ConnectivityManager
    private var isDownloaded = false

    private val networkRequest =
        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            super.onAvailable(network)
            if (isDownloaded) return
            Toast.makeText(context, getString(R.string.retrying_download), Toast.LENGTH_SHORT).show()
            val recyclerView = view?.findViewById<RecyclerView>(R.id.jobsRecycler)
            loadJobs(recyclerView?.adapter, view?.findViewById(R.id.progressBar))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, connectivityCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(connectivityCallback)
    }

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

    private fun loadJobs(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?, progressBar: ProgressBar?) {
        val call: Call<List<Job>> = NetworkService.retrofit.getJobs()
        call.enqueue(object : Callback<List<Job>> {
            override fun onFailure(call: Call<List<Job>>, t: Throwable) {
                Toast.makeText(context, getString(R.string.faild_to_download), Toast.LENGTH_LONG).show()
                Log.e(TAG, t.toString())
            }

            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {
                if (response.isSuccessful) {
                    progressBar?.visibility = View.GONE
                    _jobs.addAll(response.body() as List<Job>)
                    adapter?.notifyDataSetChanged()
                    isDownloaded = true
                }
            }

        })
    }

    companion object {

        private const val TAG = "JOBS FRAGMENT"

        fun newInstance() = JobsFragment()
    }
}