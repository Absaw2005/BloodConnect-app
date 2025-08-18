package com.example.bloodconnect.fragment.viewModel

import androidx.lifecycle.ViewModel
import com.example.bloodconnect.fragment.model.Request
import com.example.bloodconnect.fragment.utils.Utils
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MainViewModel : ViewModel() {

    fun saveRequest(request: Request) {
        request.requestId?.let {
            FirebaseDatabase.getInstance().getReference().child("Requests").child(it)
                .setValue(request).addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Users").child(Utils.getUserUid())
                    .child("requests").child(request.requestId).setValue(request)
            }
        }
    }


    fun getRequests(): Flow<List<Request>> = callbackFlow {

    }


    fun acceptRequest(reqId: String, uploaderId: String? , request: Request) {

        FirebaseDatabase.getInstance().getReference().child("Users")
            .child(Utils.getUserUid()).child("Donated").child(Utils.getRandomId()).setValue(request).addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference().child("Requests").child(reqId.toString())
                    .removeValue().addOnSuccessListener {
                        FirebaseDatabase.getInstance().reference.child("Users")
                            .child(uploaderId.toString()).child("requests").child(reqId.toString())
                            .child("status").setValue("1").addOnSuccessListener {
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(uploaderId.toString()).child("requests")
                                    .child(reqId.toString())
                                    .child("donner").setValue(Utils.getUserUid())

                            }

                    }
            }





    }


}