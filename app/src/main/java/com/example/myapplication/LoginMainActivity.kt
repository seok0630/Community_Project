package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.LoginMainBinding
import com.example.myapplication.ui.theme.MyApplicationTheme

class LoginMainActivity : ComponentActivity () {

    private var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = ArrayList<ViewData>()
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))
        list.add(ViewData("박석규", "안녕하세요ㅎㅎ"))

        val adapter = ViewAdpater(list)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

        binding.loginMainWrite.setOnClickListener {
            var intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

        if(intent.hasExtra("uid")) { //MainActivity에서 사용자의 UID를 받아오는 if else문이다.
            uid = intent.getStringExtra("uid")
        }
        else {
            Toast.makeText(this,"유저 정보가 메인페이지로 넘어오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.loginMainBack.setOnClickListener { // BACK 버튼을 누르면 메인화면으로 넘어간다.
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}