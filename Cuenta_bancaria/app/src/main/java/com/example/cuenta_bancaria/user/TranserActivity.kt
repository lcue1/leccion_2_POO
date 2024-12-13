package com.example.cuenta_bancaria.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.data.DatabaseHelper
import com.example.cuenta_bancaria.data.UserDao

class TranserActivity : AppCompatActivity() {
    private lateinit var dataUser:ArrayList<String>
    private lateinit var acountGroup:RadioGroup
    private lateinit var transferValue:EditText
    private lateinit var helper:DatabaseHelper
    private lateinit var userDao:UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transer)

        initAtributes()
        val radioButtons=generateRadioButtons()

        findViewById<ImageButton>(R.id.backBtn).setOnClickListener {
            val intent = Intent()
            intent.putExtra("result_key", "Data from TransferActivity")
            setResult(RESULT_OK, intent)
            finish()

        }
        findViewById<Button>(R.id.transfer_btn).setOnClickListener {
            if (validateFields(radioButtons)){
                //Excecute transfer
                val acountNumberSelected = radioButtons.find { it.isChecked }?.tag.toString()
                val valueToTransfer = transferValue.text.toString().toDouble()
                val debit = dataUser[2].toDouble()-valueToTransfer
                if(debit<0){
                    Toast.makeText(this,"Saldo insuficiente",Toast.LENGTH_SHORT).show()
                }else{
                    var round=String.format("%.2f", debit).toDouble()
                    round = round.toDouble()
                    userDao.updateBalance(dataUser[1], round)
                    val userInformationToTransfer=userDao.getOneUserInformation(acountNumberSelected)
                    val balanceDestinatario = userInformationToTransfer[2].toDouble()+valueToTransfer
                    userDao.updateBalance(userInformationToTransfer[1], balanceDestinatario)

                    //return to before activity
                    val intent = Intent()
                    intent.putExtra("result_key", "Data from TransferActivity")
                    setResult(RESULT_OK, intent)
                    finish()
                }

            }
        }
    }

    private fun initAtributes() {
        dataUser = intent.getStringArrayListExtra("data")!!
        findViewById<TextView>(R.id.completeNameUser).text = dataUser?.get(0) ?:"Undfined"
        findViewById<TextView>(R.id.acountNumber).text = dataUser?.get(1) ?:"Undfined"
        acountGroup = findViewById(R.id.acountsGroup)
        helper= DatabaseHelper(this)
        userDao = UserDao(helper)
        transferValue=findViewById(R.id.transferValue)
    }

    private fun generateRadioButtons() :MutableList<RadioButton>{
        val userAcounts = userDao.getAllUsers()
        val acountsRadoButtons: MutableList<RadioButton> = mutableListOf()
        userAcounts.forEach {
            var completeName = it["name"]
            var acountNumber = it["numberAcount"]
            if (acountNumber!=dataUser[1]){
                val radioButton = RadioButton(this).apply {
                    text = completeName.toString()+"Cuenta: "+acountNumber.toString()
                    id = View.generateViewId()
                    tag = acountNumber.toString()
                }
                acountsRadoButtons.add(radioButton)
                acountGroup.addView(radioButton)
            }
        }
        return  acountsRadoButtons
    }

    private fun validateFields(radioButtons:MutableList<RadioButton>):Boolean{
        if(transferValue.text.toString().isEmpty()){
            transferValue.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            transferValue.error = "Ingrese el monto"
            return false
        }
        val radioSelected=radioButtons.find{
            it.isChecked
        }

        Log.d("validation radio",radioSelected.toString())
        if(radioSelected==null){
            Toast.makeText(this,"Seleccione una cuenta",Toast.LENGTH_SHORT).show()
            return  false
        }

        return true
    }
}