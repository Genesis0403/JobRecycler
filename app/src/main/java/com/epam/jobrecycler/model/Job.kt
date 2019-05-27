package com.epam.jobrecycler.model

import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("company") val companyName: String,
    @SerializedName("company_logo") val companyLogo: String?,
    @SerializedName("title") val job: String,
    @SerializedName("location") val location: String
)