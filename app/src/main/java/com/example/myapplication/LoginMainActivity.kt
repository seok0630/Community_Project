package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.LoginMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginMainActivity : ComponentActivity () {
    private var uid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance("https://regsitertest-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("board")
        val list = ArrayList<ViewData>()

        val adapter = ViewAdpater(binding.root, list)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

//        database.addChildEventListener(object : ChildEventListener{
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val modelResult = snapshot.getValue(DataModel::class.java)
//
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(parent, "Error: "+error.toString(), Toast.LENGTH_LONG).show()
//            }
//
//        })

        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {

                    val modelResult = data.getValue(DataModel::class.java)
                    list.add(0,
                        ViewData(
                            modelResult?.uid.toString(),
                            modelResult?.title.toString()
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
        })

        adapter.setItemClickListener(object : ViewAdpater.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                var intent = Intent(view.context, ContentsViewActivity::class.java)
                intent.putExtra("uid", uid)
                startActivity(intent)
            }
        })

        binding.loginMainWrite.setOnClickListener { //쓰기버튼 클릭리스너
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