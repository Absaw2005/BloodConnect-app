package com.example.bloodconnect.fragment.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.FragmentInfoBinding
import com.example.bloodconnect.fragment.viewModel.AuthViewModel
import com.example.bloodconnect.fragment.Constants
import com.example.bloodconnect.fragment.activity.HomeActivity
import com.example.bloodconnect.fragment.model.User
import com.example.bloodconnect.fragment.utils.Utils


class InfoFragment : Fragment() {


    private lateinit var binding: FragmentInfoBinding
    private lateinit var userNumber:String
    private  val  viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentInfoBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment



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


        onLoginClicked()
        setList()



        return binding.root
    }

    private fun onLoginClicked() {
        binding.LoginBtn.setOnClickListener {
            Utils.showDialog(requireContext(),"Signing in....")
            binding.apply {

                val startName=firstName.text.toString()
                val lastName=LastName.text.toString()
                val dob=editTextDate.text.toString()
                val address=Address.text.toString()
                val bloodGroup=bloodGrp.text.toString()


                if (startName.isEmpty() || LastName.text.isEmpty() || Address.text.isEmpty() || bloodGrp.text.isEmpty() ||
                    editTextDate.text.isEmpty()){
                    Utils.showToast(requireContext(),"Please fill all the details")
                    Utils.hideDialog()
                }
                else{

                    val bundle=arguments
                    userNumber=bundle?.getString("number1").toString()
                    val user=User(
                        id=Utils.generateRandomId(), phoneNumber = userNumber , userFirstName = startName , userLastName = lastName
                        , userDOB = dob , userAddress = address , userBloodGroup = bloodGroup

                    )

                    viewModel.saveDetails(user)
                    lifecycle.apply {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(),"Successful....")
                        startActivity(Intent(context,HomeActivity::class.java))
                        activity?.finish()

                    }

                }
            }
        }
    }

    private fun setList() {
        val bG = ArrayAdapter(requireContext(),R.layout.show_list , Constants.allBloodGroup)
        val city = ArrayAdapter(requireContext(),R.layout.show_list , Constants.allCities)

        binding.apply {
           bloodGrp.setAdapter(bG)
            Address.setAdapter(city)

        }
    }

    private fun getUserNumber() {
        val bundle=arguments
        userNumber=bundle?.getString("number1").toString()
    }




}