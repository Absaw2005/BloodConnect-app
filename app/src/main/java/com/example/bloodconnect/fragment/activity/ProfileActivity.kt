package com.example.bloodconnect.fragment.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.ActivityProfileBinding
import com.example.bloodconnect.fragment.model.User
import com.example.bloodconnect.fragment.utils.Utils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private var isExpand = true
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.imageButton.setOnClickListener {
            if (isExpand) {
                binding.expandableconstraintlayout.visibility = View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.up)
            }
            else {
                binding.expandableconstraintlayout.visibility = View.GONE
                binding.imageButton.setImageResource(R.drawable.down)

            }
            isExpand=!isExpand
        }

        Firebase.database.reference.child("Users")
            .child(Utils.getUserUid()).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user=snapshot.getValue<User>()
                        binding.Username.text=user?.userFirstName
                        binding.Name.text=user?.userFirstName + " " +  user?.userLastName
                        binding.Email.text=user?.phoneNumber
                        binding.Age.text=user?.userDOB
                        binding.password.text=user?.userBloodGroup
                        binding.textView8.text=user?.id


                    }

                    override fun onCancelled(error: DatabaseError) {
                        // TODO("Not yet implemented")
                    }

                }
            )

    }
}