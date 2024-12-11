package com.example.cuenta_bancaria

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.login.LoginActivity
import com.example.cuenta_bancaria.register.RegisterActivity
import com.example.cuenta_bancaria.utils.Utils


class MainActivity : AppCompatActivity() {
    // Attributes
    private lateinit var initSesionBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var exitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAtributtes()
        addClickListener()
    }

    private fun initAtributtes() {
        // Initialize UI elements
        initSesionBtn = findViewById(R.id.init_sesion_btn)
        registerBtn = findViewById(R.id.register_btn)
        exitBtn = findViewById(R.id.exit_btn)

    }

    private fun addClickListener() {
        initSesionBtn.setOnClickListener {

            Utils.goAnotherActivity(this,LoginActivity::class.java)
            finishActivity(1)
        }
        registerBtn.setOnClickListener {
            Utils.goAnotherActivity(this,RegisterActivity::class.java)
        }
        exitBtn.setOnClickListener {

        }
    }
}