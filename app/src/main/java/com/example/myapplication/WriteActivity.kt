package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.get
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.LoginWriteBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask.TaskSnapshot
import java.time.LocalDateTime

class WriteActivity : ComponentActivity() {
    lateinit var binding: LoginWriteBinding
    lateinit var activityPermission: ActivityResultLauncher<String>
    private var uid: String = ""
    private var image_url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images")

        if(intent.hasExtra("uid")) { //MainActivity에서 사용자의 UID를 받아오는 if else문이다.
            uid = intent.getStringExtra("uid").toString()
            binding.loginWriteUidText.text = "사용자 UID: "+uid
        }
        else {
            Toast.makeText(this,"유저 정보가 글쓰기페이지로 넘어오지 못했습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        activityPermission = registerForActivityResult(ActivityResultContracts
            .RequestPermission()) { isGratned ->
            if(isGratned) {
                startProcess()
            }
            else {
                Toast.makeText(this,"권한승인 요청 실패", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.writeImageUp.setOnClickListener {
            binding.imageLinear.visibility = View.VISIBLE
            activityPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.imageUploadButton.setOnClickListener {
            val time = LocalDateTime.now()
            var uploadTask = storageRef.child(time.toString()).putFile(image_url.toUri())
            uploadTask.addOnSuccessListener {
                storageRef.child(time.toString()).downloadUrl.addOnCompleteListener{ task ->
                    if(task.isSuccessful) {
                        image_url = task.result.toString()
                        binding.imageLinear.visibility = View.GONE
                    }
                    else {
                        Toast.makeText(this,"업로드 실패", Toast.LENGTH_SHORT).show()
                    }
                }
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
                        Toast.makeText(this,"파이어 베이스에 저장완료.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener{ it ->
                        Toast.makeText(this,"파이어 베이스에 저장실패.", Toast.LENGTH_SHORT).show()
                    }
            } //if-else (if)
            else {
                Toast.makeText(this,"글의 제목과 내용을 전부 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        } //buttonClickListener
    } //onCreate

    fun startProcess() {
        Toast.makeText(this,"카메라 실행", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityResult.launch(intent)
    }

    val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 OK , 결가값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            image_url  = it.data!!.data.toString()

            //화면에 보여주기
            Glide.with(this)
                .load(image_url) //이미지
                .into(binding.previewImage) //보여줄 위치

            Toast.makeText(this,image_url, Toast.LENGTH_SHORT).show()
        }
    }
}