package com.example.cuenta_bancaria.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.data.DatabaseHelper
import com.example.cuenta_bancaria.data.UserDao
import com.example.cuenta_bancaria.extraActivities.ProgressBarActivity
import com.example.cuenta_bancaria.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var editTexts:List<EditText>// used to validate edittext in foreach
    private lateinit var editTextData:MutableList<String>
    private lateinit var savingRadio:RadioButton
    private lateinit var checkRadio:RadioButton
    private lateinit var registerBtn:Button

    //db
    private  lateinit var dbHealper:DatabaseHelper
    private lateinit var userDao:UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initAtributes()
        addEventListeners()
    }

    private fun initAtributes() {
        editTexts = listOf( findViewById(R.id.name_editTtext),
                     findViewById(R.id.balance_etitText),
                    findViewById(R.id.user_etitText),
                    findViewById(R.id.password_editText)
        )
        editTextData= emptyList<String>().toMutableList()

        savingRadio = findViewById(R.id.radio_savings)
        checkRadio = findViewById(R.id.radio_checking)
        registerBtn = findViewById(R.id.save_btn)

        //db
        dbHealper=DatabaseHelper(this)
        userDao=UserDao(dbHealper)
    }

    private fun addEventListeners() {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && editText.text.isBlank()) {
                    editText.setHintTextColor(ContextCompat.getColor(this, R.color.red))
                    editText.error = "Este campo es obligatorio"
                } else {
                    editText.setHintTextColor(ContextCompat.getColor(this, R.color.blue_2))
                }
            }
        }

        registerBtn.setOnClickListener {

            insertUserInBD()
        }
    }

    private fun insertUserInBD() {

        if (validateFields()) {
            if (savingRadio.isChecked){
                editTextData.add("Ahorros")
            }else{
                editTextData.add("Corriente")
            }
            val loadData=userDao.insertUser(
                name = editTextData[0],
                numberAcount = generateNumberAcount(),
                balance = editTextData[1].toDouble(),
                username = editTextData[2],
                password = editTextData[3],
                typeAcount = editTextData[4]
            )
            if(loadData>=0){
                val intent = Intent(this, ProgressBarActivity::class.java)
                intent.putExtra("targetActivity", MainActivity::class.java) // Replace with your actual target activity
                startActivity(intent)
                Toast.makeText(this,"Usuario agregado exitosamente!",Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(this, ProgressBarActivity::class.java)
                intent.putExtra("targetActivity", RegisterActivity::class.java) // Replace with your actual target activity
                startActivity(intent)
                Toast.makeText(this,"Intenta nuevamente",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun generateNumberAcount(): String {
       return  (1000..10000).random().toString()
    }

    private fun validateFields(): Boolean {
        editTextData.clear()
        var isValid = true
        editTexts.forEach { editText ->
            if (editText.text.isBlank()) {
                editText.setHintTextColor(ContextCompat.getColor(this, R.color.red))
                editText.error = "Este campo es obligatorio"
                isValid = false
            }else{
                editTextData.add(editText.text.toString())
            }
        }
        return isValid
    }
}

//package:mine