package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.LoginWriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

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
//            val database = FirebaseDatabase.getInstance("https://regsitertest-default-rtdb.asia-southeast1.firebasedatabase.app")
//            val myRef = database.getReference()

            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
            val curTime = dateFormat.format(Date(time))

            val dataInput = DataModel(
                uid,
                binding.loginWriteTitle.text.toString(),
                binding.loginWriteContext.text.toString()
            )

            val data = hashMapOf(
                "time" to curTime.toString(),
                "uid" to uid,
                "title" to binding.loginWriteTitle.text.toString(),
                "context" to binding.loginWriteContext.text.toString()
            )
//
//            myRef.child("board").push().setValue(dataInput)
//                .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    var intent = Intent(this, LoginMainActivity::class.java)
//                    intent.putExtra("uid", uid)
//                    Toast.makeText(this,"리얼타임 베이스에 저장완료.", Toast.LENGTH_LONG).show()
//                    startActivity(intent)
//                }
//                else {
//                    Toast.makeText(this,"리얼타임 베이스에 저장실패.", Toast.LENGTH_LONG).show()
//                }
//            }
            val database = Firebase.firestore

            database.collection("collect").add(data)
                .addOnCompleteListener{ it ->
                    Toast.makeText(this,"파이어 베이스에 저장완료.", Toast.LENGTH_LONG).show()
                    var intent = Intent(this, LoginMainActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                }
                .addOnFailureListener{ it ->
                    Toast.makeText(this,"파이어 베이스에 저장실패.", Toast.LENGTH_LONG).show()
                }
        }
    }
}