package com.example.bloodconnect.fragment.activity

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodconnect.databinding.ActivityDonateBinding
import com.example.bloodconnect.databinding.RvDesignBinding
import com.example.bloodconnect.fragment.adapter.RequestAdapter
import com.example.bloodconnect.fragment.model.Request
import com.example.bloodconnect.fragment.utils.Utils
import com.example.bloodconnect.fragment.viewModel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class DonateActivity : AppCompatActivity() {

    private lateinit var request:ArrayList<Request>
    private lateinit var adapter: RequestAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityDonateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityDonateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        fetchAllRequest()

    }

    private  fun fetchAllRequest() {

        FirebaseDatabase.getInstance().getReference().child("Requests").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                request.clear()
                val temperList=ArrayList<Request>()
                for (dataSnapshot in snapshot.children) {
                    val req: Request? = dataSnapshot.getValue(Request::class.java)
                    if (req?.uploaderUid!= Utils.getUserUid()) {
                        if (req != null) {
                            temperList.add(req)
                        }
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
        binding.reqRv.layoutManager= LinearLayoutManager(this)
        binding.reqRv.adapter=adapter


    }
    private fun onAcceptClicked(request: Request,binding: RvDesignBinding){

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Do You want to accept the request !!!")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->

            lifecycleScope.launch {
                viewModel.acceptRequest(request.requestId.toString(),request.uploaderUid,request)
                Utils.showToast(this@DonateActivity,"Accepted")
                dialog.dismiss()
            }


        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
        })
        val dialog: android.app.AlertDialog? = builder.create()
        dialog?.show()


    }
}