package com.example.bloodconnect.fragment.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bloodconnect.R
import com.example.bloodconnect.fragment.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        Handler(Looper.getMainLooper()).postDelayed({

            if (FirebaseAuth.getInstance().currentUser!=null){
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                requireActivity().finish()
            }
            else{
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

            }

        },3000)


        return inflater.inflate(R.layout.fragment_splash, container, false)


    }


}