package com.example.i_tuition

data class PostClass (
    var Class : String?,
    var Subjects : String?,
    var WeekDays : ArrayList <Boolean>? = arrayListOf(),
    var Fee : String?,
    var Description : String?
        ) {

}
