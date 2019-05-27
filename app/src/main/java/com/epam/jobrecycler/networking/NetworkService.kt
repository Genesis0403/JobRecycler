package com.epam.jobrecycler.networking

import com.epam.jobrecycler.model.Job
import com.epam.jobrecycler.utils.BASE_URL
import com.epam.jobrecycler.utils.LANGUAGE
import com.epam.jobrecycler.utils.LOCATION
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("/positions.json")
    fun getJobs(
        @Query("description") language: String = LANGUAGE,
        @Query("location") location: String = LOCATION
    ): Call<List<Job>>


    companion object {
        val retrofit = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .build()
            )
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}