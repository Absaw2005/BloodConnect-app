package com.example.bloodconnect.fragment.fragment


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.FragmentOtpBinding
import com.example.bloodconnect.fragment.activity.HomeActivity
import com.example.bloodconnect.fragment.viewModel.AuthViewModel
import com.example.bloodconnect.fragment.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class OtpFragment : Fragment() {

    private lateinit var otp: String
    private lateinit var binding: FragmentOtpBinding
    private lateinit var userNumber:String
    private  val  viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentOtpBinding.inflate(layoutInflater)

        getUserNumber()
        customizingEnteringOtp()
        sendOtp()
        onLoginButtonClicked()



        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
        }
        return binding.root
    }

    private fun onLoginButtonClicked() {
        binding.loginBtn.setOnClickListener {
            Utils.showDialog(requireContext(),"Wait...")
            val editTexts= arrayOf(binding.otp1,binding.otp2,binding.otp3,binding.otp4,binding.otp5,binding.otp6)
             otp= editTexts.joinToString(""){it.text.toString()}

            if (otp.length < editTexts.size){
                Utils.showToast(requireContext(),"Please enter right OTP")
            }
            else{
                editTexts.forEach { it.text?.clear() ; it.clearFocus() }
                verifyOtp(otp)
            }
        }
    }

    private fun verifyOtp(otp: String) {

       // val user = User(id = Utils.getUserUid(), phoneNumber = userNumber , userAddress="")
        viewModel.signInWithPhoneAuthCredential(otp,userNumber)

        lifecycleScope.launch {
            viewModel.isSignedUp.collect{
                if (it){

                    Utils.showToast(requireContext(),"Successful ...")
                    Utils.hideDialog()


                        FirebaseDatabase.getInstance().reference.child("Users")
                            .orderByChild("phoneNumber")
                            .equalTo(userNumber)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        startActivity(Intent(requireContext(),HomeActivity::class.java))
                                        activity?.finish()
                                    } else {
                                        val bundle1=Bundle()
                                        bundle1.putString("number1",userNumber)
                                        findNavController().navigate(R.id.action_otpFragment_to_infoFragment)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle error
                                }
                            })



                }
            }
        }

    }

    private fun sendOtp() {
        Utils.showDialog(requireContext(),"Sending OTP...")
        viewModel.apply {
            sendOtp(userNumber,requireActivity())
            lifecycleScope.launch {
                Utils.hideDialog()
                Utils.showToast(requireContext(),"OTP sent..")
            }
        }
    }

    private fun customizingEnteringOtp() {
        val editTexts= arrayOf(binding.otp1,binding.otp2,binding.otp3,binding.otp4,binding.otp5,binding.otp6)

        for (i in editTexts.indices){
            editTexts[i].addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length ==1 ){
                        if (i<editTexts.size-1){
                            editTexts[i+1].requestFocus()
                        }
                    }
                    else if (s?.length ==0){
                        if (i>0){
                            editTexts[i-1].requestFocus()
                        }
                    }
                }

            })
        }
    }

    private fun getUserNumber() {
        val bundle=arguments
        userNumber=bundle?.getString("number").toString()
    }

}