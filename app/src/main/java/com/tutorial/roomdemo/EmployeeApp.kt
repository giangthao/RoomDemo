package com.tutorial.roomdemo

import android.app.Application

class EmployeeApp:Application() {
    //Todo 7: create the application class and initialize the database
    val db by lazy{
        EmployeeDataBase.getInstance(this)
    }
}