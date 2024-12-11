package com.example.cuenta_bancaria.extraActivities

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.utils.Utils

class ProgressBarActivity : AppCompatActivity() {
    private lateinit var loadingAnimation: Animation
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress_bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAtributtes()
        val targetActivity = intent.getSerializableExtra("targetActivity") as? Class<*>
        Utils.showProgressBar(progressBar,loadingAnimation){
            if (targetActivity != null) {
                Utils.goAnotherActivity(this, targetActivity)
            }
        }

    finishActivity(1)    }
    private fun initAtributtes() {
        // Initialize UI elements
        progressBar = findViewById(R.id.loading_spinner)
        loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.loader) // Initialize animation
    }

}