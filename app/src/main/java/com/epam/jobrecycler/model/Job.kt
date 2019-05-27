package com.epam.jobrecycler.model

data class Job(
    val companyName: String,
    val companyLogo: String?,
    val job: String,
    val location: String
)