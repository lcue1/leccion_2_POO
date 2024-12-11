package com.example.cuenta_bancaria.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.MainActivity
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.utils.Utils
import kotlinx.coroutines.MainScope

class LoginActivity : AppCompatActivity() {
    private lateinit var back_btn:ImageButton
    private lateinit var user:EditText
    private lateinit var password:EditText
    private lateinit var loginBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAtributes()
        addClickListener()
    }

    private fun addClickListener() {
        back_btn.setOnClickListener {
            Utils.goAnotherActivity(this,MainActivity::class.java)
            finish()
        }

        loginBtn.setOnClickListener {

        }

    }

    private fun initAtributes() {
        back_btn=findViewById(R.id.back_login_abtn)
        user=findViewById(R.id.user_edit_login)
        password=findViewById(R.id.pass_edit_login)
        loginBtn=findViewById(R.id.login_btn)
    }
}
