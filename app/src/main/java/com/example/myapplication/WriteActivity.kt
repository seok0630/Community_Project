package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.LoginWriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WriteActivity : ComponentActivity() {
    private var uid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("uid")) { //MainActivity에서 사용자의 UID를 받아오는 if else문이다.
            uid = intent.getStringExtra("uid").toString()
            binding.loginWriteUidText.text = "사용자 UID: "+uid
        }
        else {
            Toast.makeText(this,"유저 정보가 글쓰기페이지로 넘어오지 못했습니다. 다시 로그인해주세요.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.loginWriteButton.setOnClickListener {
            val database = FirebaseDatabase.getInstance("https://regsitertest-default-rtdb.asia-southeast1.firebasedatabase.app")
            val myRef = database.getReference()

            val dataInput = DataModel(
                binding.loginWriteTitle.text.toString(),
                binding.loginWriteContext.text.toString()
            )

            myRef.child(uid).setValue(dataInput)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var intent = Intent(this, LoginMainActivity::class.java)
                    intent.putExtra("uid", uid)
                    Toast.makeText(this,"리얼타임 베이스에 저장완료.", Toast.LENGTH_LONG).show()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this,"리얼타임 베이스에 저장실패.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}