package com.example.helloworld

import java.io.Serializable

data class Student(
    var name: String,
    var mssv: String,
    var email: String,
    var phone: String
): Serializable
