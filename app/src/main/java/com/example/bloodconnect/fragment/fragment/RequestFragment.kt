package com.example.bloodconnect.fragment.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.FragmentRequestBinding
import com.example.bloodconnect.fragment.Constants
import com.example.bloodconnect.fragment.model.Request
import com.example.bloodconnect.fragment.utils.Utils
import com.example.bloodconnect.fragment.viewModel.MainViewModel


class RequestFragment : Fragment() {


    private lateinit var binding: FragmentRequestBinding
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding=FragmentRequestBinding.inflate(layoutInflater)



        binding.editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
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

        return binding.root


    }

    private fun setList() {
        val bG = ArrayAdapter(requireContext(),R.layout.show_list , Constants.allBloodGroup)
        val bType = ArrayAdapter(requireContext(),R.layout.show_list , Constants.allBloodType)
        val unitsRequired = ArrayAdapter(requireContext(),R.layout.show_list , Constants.NoOfUnit)
        val city = ArrayAdapter(requireContext(),R.layout.show_list , Constants.allCities)

        binding.apply {
            bloodGrp.setAdapter(bG)
            bloodType.setAdapter(bType)
            units.setAdapter(unitsRequired)
            Address.setAdapter(city)


        }
    }

    private fun onSubmitClicked() {
        binding.submitBtn.setOnClickListener {
            Utils.showDialog(requireContext(),"Saving your request....")
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
                    Utils.showToast(requireContext(),"Please fill all the details")
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
                        Utils.showToast(requireContext(),"Your Request is Submitted....")
                        findNavController().navigate(R.id.action_request_to_home)


                    }

                }
            }
        }
    }

}