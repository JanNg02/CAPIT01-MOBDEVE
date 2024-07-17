package com.example.s11.ng.jan.capit01_mobdeve.dashboard

data class EmergencyContact(val name: String, val phoneNumber: String){
    override fun toString(): String{
        return "$name. $phoneNumber"
    }
}