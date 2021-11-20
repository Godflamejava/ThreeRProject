package com.example.a3rs

import java.util.ArrayList

data class RecycleOrder(
        val email:String?=null,
        val name : String? = null,
        val address : String? = null,
        val city : String? = null,
        val zipcode : String? = null,
        val phone : String? = null,
        val recycleItem : ArrayList<String>? = null,
        val date : String? = null,
        val time : String? = null

)
