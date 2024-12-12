package com.example.cuenta_bancaria.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.MainActivity
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.databinding.ActivityMainBinding
import com.example.cuenta_bancaria.databinding.ActivityUserBinding
import com.example.cuenta_bancaria.utils.Utils

class UserActivity : AppCompatActivity() {
    private  var userData:ArrayList<String>?=null
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge content

        userData = intent.getStringArrayListExtra("data")
        loadInformationUser()
        addOnclickListener()
    }

    private fun addOnclickListener() {
        binding.exitBtn.setOnClickListener { Utils.goAnotherActivity(
            this, MainActivity::class.java)
            finish()
        }



    }

    private fun loadInformationUser() {

        binding.completeName.text = userData?.get(0) ?: "Undefined"
        binding.userName.text = userData?.get(3) ?: "Undefined"
        binding.acountType.text = userData?.get(4) ?: "Undefined"
        binding.numberAcount.text = userData?.get(1) ?: "Undefined"
        binding.balace.text = userData?.get(2) ?: "Undefined"
    }



}