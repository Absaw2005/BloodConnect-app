package com.example.bloodconnect.fragment.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.bloodconnect.databinding.DialogLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object Utils {

    fun showToast(context: Context, message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    private var dialog:AlertDialog?=null
    fun showDialog(context: Context,message: String){
        val progress= DialogLayoutBinding.inflate(LayoutInflater.from(context))
        progress.dialogTxt.text=message
        dialog=AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog!!.show()

    }

    fun hideDialog(){
        dialog?.dismiss()
    }

    private var firebaseAuthInstance:FirebaseAuth? = null
    fun getAuthInstance() : FirebaseAuth{
        if (firebaseAuthInstance == null){
            firebaseAuthInstance = FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!!
    }
    fun getUserUid() : String {
       return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun getCurrentDate(): String? {
        val currentDate = LocalDate.now()
        val formatter =DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }

    fun getRandomId():String{
        return (1 .. 25).map { (('A'..'Z') + ('a'..'z')+('0'..'9')).random() }.joinToString("")
    }

    fun generateRandomId(): String {
        return "USER${String.format("%06d", Random.nextInt(1000000))}"
    }


}