package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
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
    private var pre_image_url: String? = ""
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference.child("images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            activityPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        }

        binding.previewImage.setOnClickListener{
            val builder = AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("이미지를 삭제하시려면 '확인'버튼을 눌러주세요.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, which ->
                        binding.previewImage.setImageDrawable(null)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which -> })
            builder.show()

        }

        binding.loginWriteButton.setOnClickListener {
            val time = LocalDateTime.now()
            val database = Firebase.firestore
            if (pre_image_url != null) {
                var uploadTask = storageRef.child(time.toString()).putFile(pre_image_url!!.toUri())
                uploadTask.addOnSuccessListener {
                    storageRef.child(time.toString()).downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            image_url = task.result.toString()

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

                            if (binding.loginWriteTitle.text != null && binding.loginWriteContext.text != null) {
                                database.collection("collect").add(data)
                                    .addOnCompleteListener { it ->
                                        Toast.makeText(this, "저장완료.", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    .addOnFailureListener { it ->
                                        Toast.makeText(this, "저장실패.", Toast.LENGTH_SHORT).show()
                                    }
                            } //if-else (if)
                            else {
                                Toast.makeText(this, "글의 제목과 내용을 전부 입력해주세요", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
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
                    "image_url" to ""
                )

                if (binding.loginWriteTitle.text != null && binding.loginWriteContext.text != null) {
                    database.collection("collect").add(data)
                        .addOnCompleteListener { it ->
                            Toast.makeText(this, "저장완료.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { it ->
                            Toast.makeText(this, "저장실패.", Toast.LENGTH_SHORT).show()
                        }
                } //if-else (if)
                else {
                    Toast.makeText(this, "글의 제목과 내용을 전부 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        } //buttonClickListener
    } //onCreate


    fun startProcess() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activityResult.launch(intent)
    }

    val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 OK , 결가값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            pre_image_url  = it.data!!.data.toString()

            //화면에 보여주기
            Glide.with(this)
                .load(pre_image_url) //이미지
                .into(binding.previewImage) //보여줄 위치
        }
        else {

        }
    }
}