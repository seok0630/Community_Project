package com.example.myapplication;

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.ContentsViewActivityBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContentsViewActivity: ComponentActivity() {
        private var uid: String = ""
        private var pos: Int = 0
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                val binding = ContentsViewActivityBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val database = Firebase.firestore
                val docRef = database.collection("collect").orderBy("time", Query.Direction.DESCENDING)
                var count = 0

                if (intent.hasExtra("uid")) { //사용자의 UID를 받아오는 if else문이다.
                        uid = intent.getStringExtra("uid").toString()
                } else {
                        Toast.makeText(this, "유저 정보가 메인페이지로 넘어오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        finish()
                }
                if (intent.hasExtra("pos")) { //사용자가 클릭한 글의 인덱스를 받아오는 if else문이다.
                        pos = intent.getIntExtra("pos", 0)
                } else {
                        Toast.makeText(this, "글내용이 페이지로 넘어오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
                        finish()
                }

                docRef.get().addOnSuccessListener { QuerySnapshot ->
                        for(dc in QuerySnapshot){
                                if(count == pos){
                                        binding.contentsViewTitle.text = dc.data.getValue("title").toString()
                                        binding.contentsViewContext.text = dc.data.getValue("context").toString()
                                        binding.contextUid.text = dc.data.getValue("uid").toString()
                                        binding.contextTitle.text = dc.data.getValue("title").toString()
                                        binding.contextTime.text = dc.data.getValue("time").toString()
                                }
                                count++
                        }
                }

        }
}
