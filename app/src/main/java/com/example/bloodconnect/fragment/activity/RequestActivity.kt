package com.example.bloodconnect.fragment.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.ActivityRequestBinding
import com.example.bloodconnect.databinding.FragmentRequestBinding
import com.example.bloodconnect.fragment.Constants
import com.example.bloodconnect.fragment.model.Request
import com.example.bloodconnect.fragment.utils.Utils
import com.example.bloodconnect.fragment.viewModel.MainViewModel

class RequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }





        binding.editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    binding.editTextDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth))
                },
                year,
                month,
                day
            )

            datePickerDialog.show()


        }

        setList()
        onSubmitClicked()

    }
    private fun setList() {
        val bG = ArrayAdapter(this,R.layout.show_list , Constants.allBloodGroup)
        val bType = ArrayAdapter(this,R.layout.show_list , Constants.allBloodType)
        val unitsRequired = ArrayAdapter(this,R.layout.show_list , Constants.NoOfUnit)
        val city = ArrayAdapter(this,R.layout.show_list , Constants.allCities)

        binding.apply {
            bloodGrp.setAdapter(bG)
            bloodType.setAdapter(bType)
            units.setAdapter(unitsRequired)
            Address.setAdapter(city)


        }
    }

    private fun onSubmitClicked() {
        binding.submitBtn.setOnClickListener {
            Utils.showDialog(this,"Saving your request....")
            binding.apply {

                val startName=firstName.text.toString()
                val date=editTextDate.text.toString()
                val address=Address.text.toString()
                val bloodGroup=bloodGrp.text.toString()
                val bloodType=bloodType.text.toString()
                val unit=units.text.toString()
                val phoneNumber=phoneNumber.text.toString()
                val critical = Critical.isChecked.toString()



                if (startName.isEmpty() || date.isEmpty() || Address.text.isEmpty() || bloodGrp.text.isEmpty() ||
                    editTextDate.text.isEmpty() || bloodType.isEmpty() || unit.isEmpty() || phoneNumber.isEmpty() ){
                    Utils.showToast(this@RequestActivity,"Please fill all the details")
                    Utils.hideDialog()

                }
                else{

                    val request= Request(
                        requestId = Utils.getRandomId() , patientNumber = phoneNumber , requestName = startName , patientBloodType = bloodType
                        , patientBloodGroup = bloodGroup , userAddress = address, critical = critical , unit = unit
                        , requiredDate = date , uploaderUid = Utils.getUserUid() , status = "0"

                    )

                    viewModel.saveRequest(request)
                    lifecycle.apply {
                        Utils.hideDialog()
                        Utils.showToast(this@RequestActivity,"Your Request is Submitted....")
                       startActivity(Intent(this@RequestActivity,HomeActivity::class.java))


                    }

                }
            }
        }
    }
}