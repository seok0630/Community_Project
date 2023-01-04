package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.LoginWriteBinding

class WriteActivity : ComponentActivity() {
    private var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("uid")) { //MainActivity에서 사용자의 UID를 받아오는 if else문이다.
            uid = intent.getStringExtra("uid")
            binding.loginWriteUidText.text = "사용자 UID: "+uid
        }
        else {
            Toast.makeText(this,"유저 정보가 글쓰기페이지로 넘어오지 못했습니다. 다시 로그인해주세요.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}