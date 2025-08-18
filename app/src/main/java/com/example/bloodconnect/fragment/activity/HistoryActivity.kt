package com.example.bloodconnect.fragment.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.ActivityDonateBinding
import com.example.bloodconnect.databinding.ActivityHistoryBinding
import com.example.bloodconnect.databinding.RvDesignBinding
import com.example.bloodconnect.fragment.adapter.RequestAdapter
import com.example.bloodconnect.fragment.model.Request
import com.example.bloodconnect.fragment.utils.Utils
import com.example.bloodconnect.fragment.viewModel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : AppCompatActivity() {

    private lateinit var request: ArrayList<Request>
    private lateinit var adapter: RequestAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.hisToolbar.setNavigationOnClickListener {
            finish()
        }
        fetchAllRequest()
    }

    private  fun fetchAllRequest() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(Utils.getUserUid()).child("Donated").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                request.clear()
                val temperList=ArrayList<Request>()
                for (dataSnapshot in snapshot.children) {
                    val req: Request? = dataSnapshot.getValue(Request::class.java)
                        if (req != null) {
                            temperList.add(req)

                    }
                }
                request.addAll(temperList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        request = ArrayList<Request>()
        adapter = RequestAdapter(request, ::onAcceptClicked)
        binding.donatedRv.layoutManager= LinearLayoutManager(this)
        binding.donatedRv.adapter=adapter


    }

    private fun onAcceptClicked(request: Request, rvDesignBinding: RvDesignBinding) {

      Utils.showToast(this,"You already Accepted this request. ")


    }


}