package com.example.cuenta_bancaria.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.extraActivities.ProgressBarActivity
import com.example.cuenta_bancaria.MainActivity
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.data.DatabaseHelper
import com.example.cuenta_bancaria.data.UserDao
import com.example.cuenta_bancaria.register.RegisterActivity
import com.example.cuenta_bancaria.user.UserActivity
import com.example.cuenta_bancaria.utils.Utils
import java.util.ArrayList

class LoginActivity : AppCompatActivity() {
    private lateinit var back_btn:ImageButton
    private lateinit var user:EditText
    private lateinit var password:EditText
    private lateinit var loginBtn:Button
    private lateinit var databaseHelper:DatabaseHelper
    private lateinit var userDao:UserDao

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
            if (validationEditText()) {
                val dataLogin = userDao.getUserInformation(user.text.toString(), password.text.toString())
                if(dataLogin.isEmpty()) {
                    val intent =Intent(this,ProgressBarActivity::class.java)
                    intent.putExtra("targetActivity", LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this,"Usuario o password incorrectos",Toast.LENGTH_LONG).show()
                }else{
                    val intent = Intent(this, ProgressBarActivity::class.java)
                    intent.putExtra("targetActivity",UserActivity::class.java)
                    intent.putStringArrayListExtra("data", ArrayList(dataLogin))
                    startActivity(intent)
                    finish()
                }
            }

        }

    }

    private fun validationEditText() :Boolean{
        if(user.text.isEmpty()){
            user.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            user.error = "Campo obligatorio"
            return false
        }else if(password.text.isEmpty()){
            password.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            password.error = "Campo obligatorio"
            return false
        }
        return true
    }

    private fun initAtributes() {
        back_btn=findViewById(R.id.back_login_abtn)
        user=findViewById(R.id.user_edit_login)
        password=findViewById(R.id.pass_edit_login)
        loginBtn=findViewById(R.id.login_btn)
        databaseHelper=DatabaseHelper(this)
        userDao=UserDao(databaseHelper)

    }
}
