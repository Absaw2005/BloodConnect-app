package com.example.bloodconnect.fragment.adapter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodconnect.databinding.RvDesignBinding
import com.example.bloodconnect.fragment.model.Request
import java.util.ArrayList

class RequestAdapter(
    private val requestList: ArrayList<Request>,
    val onAcceptClicked: (Request, RvDesignBinding) -> Unit
):
    RecyclerView.Adapter<RequestAdapter.ReqViewHolder>() {
    class ReqViewHolder(val  binding : RvDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ReqViewHolder {
    return ReqViewHolder(RvDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder:ReqViewHolder, position: Int) {
        val req=requestList[position]
        holder.binding.apply {
            BloodGrp.text=req.patientBloodGroup.toString()
            PatientName.text=req.requestName.toString()
            location.text=req.userAddress.toString()
            date.text=req.requiredDate.toString()
            reqUnit.text=req.unit.toString() + " " + "Unit" + " " + "(Blood)"
            if(!req.critical.toBoolean()){
                critical.visibility=View.GONE
            }



            acceptBtn.setOnClickListener {

                onAcceptClicked(req,this)
            }
            callBtn.setOnClickListener {


                if (ContextCompat.checkSelfPermission(  holder.itemView.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(holder.itemView.context as Activity, arrayOf(Manifest.permission.CALL_PHONE), 1)
                }

                else{
                    val phoneNumber = req.patientNumber
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.setData(Uri.parse("tel:$phoneNumber"))
                    holder.itemView.context.startActivity(intent)
                }



            }

        }

    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}