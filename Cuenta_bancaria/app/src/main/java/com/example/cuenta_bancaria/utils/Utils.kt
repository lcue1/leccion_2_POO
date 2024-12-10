package com.example.cuenta_bancaria.utils

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

object Utils {

    fun showProgressBar(progressBar:ProgressBar,animation:Animation, function:()->Unit){
        // Show and animate the ProgressBar
        progressBar.visibility = View.VISIBLE
        progressBar.startAnimation(animation)

        // Simulate loading process before navigating to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the new activity
            //    val intent = Intent(this, UserActivity::class.java)
            //     startActivity(intent)

            // Hide the ProgressBar (optional)
            progressBar.visibility = View.GONE
            function()
        }, 2000) // Adjust delay as needed
    }
    fun goAnotherActivity(activity: AppCompatActivity, targetActivity: Class<*>) {
        val intent = Intent(activity, targetActivity)
        activity.startActivity(intent)
        activity.finish() // Finish the current activity to remove it from the back stack
    }
}