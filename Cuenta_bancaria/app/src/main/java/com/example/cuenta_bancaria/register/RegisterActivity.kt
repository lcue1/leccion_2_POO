package com.example.cuenta_bancaria.register

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.red
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var editTexts:List<EditText>// used to validate edittext in foreach
    private lateinit var editTextData:MutableList<String>
    private lateinit var savingRadio:RadioButton
    private lateinit var checkRadio:RadioButton
    private lateinit var registerBtn:Button
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

            if (validateFields()) {
                if (savingRadio.isChecked){
                    editTextData.add("Ahorros")
                }else{
                    editTextData.add("Corriente")
                }
                Log.d("data",editTextData.toString())
            }
        }
    }

    private fun validateFields(): Boolean {
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