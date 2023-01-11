package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.LoginWriteBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class WriteActivity : ComponentActivity() {
    private var uid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var image_url: String = ""

        if(intent.hasExtra("uid")) { //MainActivity에서 사용자의 UID를 받아오는 if else문이다.
            uid = intent.getStringExtra("uid").toString()
            binding.loginWriteUidText.text = "사용자 UID: "+uid
        }
        else {
            Toast.makeText(this,"유저 정보가 글쓰기페이지로 넘어오지 못했습니다. 다시 로그인해주세요.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.writeImageUp.setOnClickListener {
            binding.imageLinear.visibility = View.VISIBLE
        }

        binding.imageUploadButton.setOnClickListener {
            if(binding.imageUrlEdittext.text.isNotEmpty()){
                image_url = binding.imageUrlEdittext.text.toString()
                Glide.with(binding.root).load(image_url).into(binding.previewImage)
                binding.imageUrlEdittext.text.clear()
                binding.imageLinear.visibility = View.GONE
            }
            else{
                Toast.makeText(this,"이미지 Url을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginWriteButton.setOnClickListener {
            val time = LocalDateTime.now()
            val database = Firebase.firestore
            val data = hashMapOf(
                "time" to time.toString(),
                "uid" to uid,
                "title" to binding.loginWriteTitle.text.toString(),
                "context" to binding.loginWriteContext.text.toString(),
                "recom" to 0, //Recommend
                "not_recom" to 0,
                "nov" to 0, //Number Of View
                "id" to "",
                "noc" to "0",
                "image_url" to image_url
            )

            if(binding.loginWriteTitle.text != null && binding.loginWriteContext.text != null) {
                database.collection("collect").add(data)
                    .addOnCompleteListener{ it ->
                        Toast.makeText(this,"파이어 베이스에 저장완료.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener{ it ->
                        Toast.makeText(this,"파이어 베이스에 저장실패.", Toast.LENGTH_LONG).show()
                    }
            } //if-else (if)
            else {
                Toast.makeText(this,"글의 제목과 내용을 전부 입력해주세요", Toast.LENGTH_LONG).show()
            }
        } //buttonClickListener
    } //onCreate
}