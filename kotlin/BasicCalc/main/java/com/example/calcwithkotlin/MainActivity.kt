package com.example.calcwithkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        plus.setOnClickListener(){
            val a = number1.text.toString().toInt()
            val b = number2.text.toString().toInt()
            result.text = (a+b).toString()
        }
        minus.setOnClickListener(){
            val a = number1.text.toString().toInt()
            val b = number2.text.toString().toInt()
            result.text = (a-b).toString()
        }
    }
}