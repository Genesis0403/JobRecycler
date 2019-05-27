package com.epam.jobrecycler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.epam.jobrecycler.ui.JobsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainActivity, JobsFragment.newInstance())
                .commit()
        }
    }
}
