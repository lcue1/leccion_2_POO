package com.example.cuenta_bancaria.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuenta_bancaria.MainActivity
import com.example.cuenta_bancaria.R
import com.example.cuenta_bancaria.data.DatabaseHelper
import com.example.cuenta_bancaria.data.UserDao
import com.example.cuenta_bancaria.databinding.ActivityMainBinding
import com.example.cuenta_bancaria.databinding.ActivityUserBinding
import com.example.cuenta_bancaria.utils.Utils
import kotlin.math.log

class UserActivity : AppCompatActivity() {
    private  var userData:ArrayList<String>?=null
    private lateinit var binding: ActivityUserBinding
    private lateinit var helper:DatabaseHelper
    private lateinit var userDao:UserDao
    private lateinit var transferActivityLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        //Handle transfer activity
        transferActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val balanceUpdated = userDao.getOneUserInformation(userData?.get(1) ?: "undefined")

                binding.balace.text= balanceUpdated[2]


                    val data = result.data
                // Handle the result returned from TransferActivity
                val returnedData = data?.getStringExtra("result_key")
            }
        }


        // Initialize ViewBinding
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge content

        userData = intent.getStringArrayListExtra("data")
        initAtributes()
        loadInformationUser()
        addOnclickListener()
    }

    private fun initAtributes() {
        helper=DatabaseHelper(this)
        userDao=UserDao(helper)
    }

    private fun addOnclickListener() {
        binding.exitBtn.setOnClickListener { Utils.goAnotherActivity(
            this, MainActivity::class.java)
            finish()
        }
        binding.depositBtn.setOnClickListener {
            depositWithdrawAcount(this,true)
        }

        binding.withdrowBtn.setOnClickListener {
            depositWithdrawAcount(this,false)
        }
        binding.transferBtn.setOnClickListener {
            val intent = Intent(this, TranserActivity::class.java)
            val balanceUpdated = userDao.getOneUserInformation(userData?.get(1) ?: "undefined")
            intent.putStringArrayListExtra("data", java.util.ArrayList(balanceUpdated))
            transferActivityLauncher.launch(intent)

        }

    }



    private fun loadInformationUser() {

        binding.completeName.text = userData?.get(0) ?: "Undefined"
        binding.userName.text = userData?.get(3) ?: "Undefined"
        binding.acountType.text = userData?.get(4) ?: "Undefined"
        binding.numberAcount.text = userData?.get(1) ?: "Undefined"
        binding.balace.text = userData?.get(2) ?: "Undefined"
        val userInformation = userDao.getOneUserInformation(binding.numberAcount.text.toString())
        if(userInformation[5]!="null"){
            val imageUri = Uri.parse(userInformation[5])
            binding.logoUser.setImageURI(imageUri)
            Log.d("uri string",userInformation[5].toString())
            Log.d("uri uti",imageUri.toString())
        }
    }


    fun depositWithdrawAcount(context: Context, deposit:Boolean) {
        var title = "Depositar"

        if (!deposit){
            title="Retirar"
        }

        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            hint = "Ingrese monto"
        }

        // Create an AlertDialog Builder
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(input)  // Set the EditText view in the dialog
            .setPositiveButton("OK") { dialog, _ ->
                // Get the value entered by the user
                val inputText = input.text.toString()
                if (inputText.isNotEmpty()) {
                    try {
                        val depositEnter = inputText.toDouble()
                        val balance = binding.balace.text.toString().toDouble()
                        var operation=depositEnter+balance
                        if (!deposit ){
                            operation = balance-depositEnter
                            if (operation<0){
                                Toast.makeText(this,"Tu saldo es insuficiente",Toast.LENGTH_LONG).show()
                            }else{
                                var round=String.format("%.2f", operation).toDouble()
                                round = round.toDouble()
                                userDao.updateBalance(binding.numberAcount.text.toString().toString(),round)
                                binding.balace.text = round.toString()
                            }
                        }else{
                            var round=String.format("%.2f", operation).toDouble()
                            round = round.toDouble()
                            userDao.updateBalance(binding.numberAcount.text.toString().toString(),round)
                            binding.balace.text = round.toString()
                        }


                    } catch (e: NumberFormatException) {
                        // Handle invalid input (e.g., not a valid decimal)
                        Toast.makeText(context, "Invalid input. Please enter a valid decimal.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Input is empty. Please enter a value.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()  // Dismiss the dialog if canceled
            }

        // Show the dialog
        dialogBuilder.show()
    }

}