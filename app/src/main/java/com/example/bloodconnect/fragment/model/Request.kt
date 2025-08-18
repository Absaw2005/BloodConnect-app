package com.example.bloodconnect.fragment.model

data class Request (
    val requestId:String?=null,
    val patientNumber:String?=null,
    val requestName:String?=null,
    val patientBloodGroup:String?=null,
    val patientBloodType:String?=null,
    val userAddress: String?=null,
    val requiredDate:String?=null,
    val critical : String? =null,
    val unit : String?=null,
    val uploaderUid : String ? =null,
    val status:String?="0"


)
