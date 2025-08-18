package com.example.bloodconnect.fragment.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding=FragmentLoginBinding.inflate(layoutInflater)

        getUserNumber()
        onContinueButtonClick()

        return binding.root






    }

    private fun onContinueButtonClick() {
        binding.continueBtn.setOnClickListener {
            val number=binding.phoneNumber.text.toString()

            if (number.isEmpty() || number.length != 10 ){
                Toast.makeText(context,"Fill phone number properly",Toast.LENGTH_SHORT).show()

            }
            else{
                val bundle=Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_loginFragment_to_otpFragment,bundle)
            }
        }
    }


    private fun getUserNumber(){
        binding.phoneNumber.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(number: CharSequence?, start: Int, before: Int, count: Int) {
                val len = number?.length

                if (len==10){
                    binding.continueBtn.setBackgroundColor(
                        ContextCompat.getColor(requireContext(),
                            R.color.Green))
                }
                else{
                    binding.continueBtn.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.red))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }


        })
    }

}