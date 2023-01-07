package com.example.myapplication;

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.ContentsViewActivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContentsViewActivity: ComponentActivity() {
        private var uid: String = ""
        val list = ArrayList<DataModel>()
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                val binding = ContentsViewActivityBinding.inflate(layoutInflater)
                setContentView(binding.root)
                val database =
                        FirebaseDatabase.getInstance("https://regsitertest-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference().child("board")

                if (intent.hasExtra("uid")) { //MainActivity에서 사용자의 UID를 받아오는 if else문이다.
                        uid = intent.getStringExtra("uid").toString()
                } else {
                        Toast.makeText(
                                this,
                                "유저 정보가 메인페이지로 넘어오지 못했습니다. 다시 시도해주세요.",
                                Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this, MainActivity::class.java))
                }

        }
}
