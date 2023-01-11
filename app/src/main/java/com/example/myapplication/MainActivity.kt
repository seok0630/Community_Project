package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.databinding.TesBinding
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                val Tesbinding = TesBinding.inflate(layoutInflater)
                setContentView(Tesbinding.root)

                val user = Firebase.auth.currentUser

                if (user != null) {
                    val intent = Intent(this, LoginMainActivity::class.java)
                    intent.putExtra("uid", auth.currentUser?.uid)
                    startActivity(intent)
                }

                Tesbinding.btnLogin.setOnClickListener {
                    var pre_email: String? = Tesbinding.edId.text.toString()
                    var pre_password: String? = Tesbinding.edPass.text.toString()
                    if (pre_email!!.isNotEmpty() && pre_password!!.isNotEmpty()){
                        var email = pre_email
                        var password = pre_password

                        login(email,password)
                    }
                    else{
                        Toast.makeText(this, "이메일과 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                Tesbinding.tesSignupTextview.setOnClickListener {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser != null) {
                        if (auth.currentUser!!.isEmailVerified) {
                            val intent = Intent(this, LoginMainActivity::class.java)
                            intent.putExtra("uid", auth.currentUser?.uid)
                            Toast.makeText(this, email + "님 환영합니다", Toast.LENGTH_LONG).show()
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, "이메일 인증을 받지않은 계정입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(this, "로그인에 실패 하였습니다.(UID에러!)", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

