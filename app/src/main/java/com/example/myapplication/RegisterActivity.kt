package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.RegisterActivityBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : ComponentActivity () {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.regBtn.setOnClickListener {
            if (binding.regPass.text.toString() == binding.regPass1.text.toString()) {
                signup(binding.regId.text.toString(), binding.regPass.text.toString())
            }
            else{
                Toast.makeText(this, "입력하신 패스워드가 서로 맞지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signup(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        emailVerify(email)
                    }
                    else{
                        Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else {
            Toast.makeText(
                this, "계정 생성 실패",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun emailVerify(email: String){
        auth?.currentUser?.sendEmailVerification()?.addOnCompleteListener { verifiTask ->
            if(verifiTask.isSuccessful){
                Toast.makeText(this, "이메일 전송완료. 이메일을 확인해 주세요", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(this, "이메일 전송실패. 회원가입을 다시 진행 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}