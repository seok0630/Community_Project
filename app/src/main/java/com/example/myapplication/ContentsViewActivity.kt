package com.example.myapplication;

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ContentsViewActivityBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class ContentsViewActivity: ComponentActivity() {
        private var uid: String = ""
        private var pos: Int = 0
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                val binding = ContentsViewActivityBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val list = ArrayList<ViewData>()
                val adapter = CommentAdapter(list)
                val layoutManager = LinearLayoutManager(this)
                binding.recyclerCommnentView.adapter = adapter
                binding.recyclerCommnentView.layoutManager = layoutManager

                val database = Firebase.firestore
                val docRef =
                        database.collection("collect").orderBy("time", Query.Direction.DESCENDING)
                val comRef = database.collection("collect")
                var count = 0
                var time: String = ""
                var title: String = ""
                var id: String = ""
                var thumbU: Int = 0
                var thumbD: Int = 0


                if (intent.hasExtra("uid")) { //사용자의 UID를 받아오는 if else문이다.
                        uid = intent.getStringExtra("uid").toString()
                } else {
                        Toast.makeText(
                                this,
                                "유저 정보가 메인페이지로 넘어오지 못했습니다. 다시 시도해주세요.",
                                Toast.LENGTH_SHORT
                        ).show()
                        finish()
                }
                if (intent.hasExtra("pos")) { //사용자가 클릭한 글의 인덱스를 받아오는 if else문이다.
                        pos = intent.getIntExtra("pos", 0)
                } else {
                        Toast.makeText(this, "글내용이 페이지로 넘어오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        finish()
                }
                if (intent.hasExtra("time")) { //사용자가 클릭한 글의 인덱스를 받아오는 if else문이다.
                        time = intent.getStringExtra("time").toString()
                } else {
                        Toast.makeText(this, "리스트가 페이지로 넘어오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        finish()
                }

                docRef.get().addOnSuccessListener { QuerySnapshot ->
                        for (dc in QuerySnapshot) {
                                if (time == dc.data.getValue("time").toString()) {
                                        database.collection("collect").document(dc.id).update(
                                                "nov",
                                                dc.data.getValue("nov").toString().toInt() + 1
                                        )

                                        if (dc.data.getValue("id").toString().isEmpty()) {
                                                database.collection("collect").document(dc.id)
                                                        .update("id", dc.id)
                                        }
                                        database.collection("collect").document(dc.id).get()
                                                .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                                val document = task.result
                                                                id = document.id
                                                                title = document.get("title")
                                                                        .toString()
                                                        }
                                                }
                                        database.collection("collect").document(dc.id).collection("comment").get()
                                                .addOnCompleteListener { task ->
                                                        if(task.isSuccessful){
                                                                val doc = task.result
                                                                database.collection("collect").document(dc.id).update("noc", doc.size())
                                                        }}
                                        if(dc.data.containsKey("image_url") != false) {
                                                Glide.with(binding.root).load(dc.data.getValue("image_url").toString()).into(binding.contentsImage)
                                        }

                                        binding.contentsViewTitle.text =
                                                dc.data.getValue("title").toString()
                                        binding.contentsViewContext.text =
                                                dc.data.getValue("context").toString()
                                        binding.contextUid.text = dc.data.getValue("uid").toString()
                                        binding.contextTitle.text =
                                                dc.data.getValue("title").toString()
                                        binding.contextTime.text =
                                                dc.data.getValue("time").toString()
                                        binding.contextNov.text =
                                                "조회수 " + dc.data.getValue("nov").toString()
                                        binding.thumbUpTextview.text =
                                                dc.data.getValue("recom").toString()
                                        binding.thumbDownTextview.text =
                                                dc.data.getValue("not_recom").toString()

                                        comRef.document(dc.id).collection("comment").get()
                                                .addOnSuccessListener { QuerySnapshot ->
                                                        list.clear()
                                                        for (data in QuerySnapshot!!) {
                                                                list.add(
                                                                        ViewData(
                                                                                data.data.getValue("uid")
                                                                                        .toString(),
                                                                                data.data.getValue("comment")
                                                                                        .toString(),
                                                                                data.data.getValue("writetime")
                                                                                        .toString()
                                                                        )
                                                                )
                                                        }
                                                        adapter.notifyDataSetChanged()
                                                }
                                        break
                                } else if (count == QuerySnapshot.size() - 1) {
                                        Toast.makeText(this, "삭제된 글입니다.", Toast.LENGTH_SHORT).show()
                                        finish()
                                }
                                count++
                        }
                } //addOnSuccessListener

                binding.commentRefresh.setOnClickListener {
                        comRef.document(id).collection("comment").get()
                                .addOnSuccessListener { QuerySnapshot ->
                                        list.clear()
                                        for (data in QuerySnapshot!!) {
                                                list.add(
                                                        ViewData(
                                                                data.data.getValue("uid")
                                                                        .toString(),
                                                                data.data.getValue("comment")
                                                                        .toString(),
                                                                data.data.getValue("writetime")
                                                                        .toString()
                                                        )
                                                )
                                        }
                                        database.collection("collect").document(id)
                                                .update("noc", database.collection("collect").document(id).collection("comment").get()
                                                        .result.size().toString().toInt())
                                        adapter.notifyDataSetChanged()
                                }
                }

                binding.commentButton.setOnClickListener {
                        if (binding.conentsViewComment.text != null) {
                                writeComment(
                                        binding,
                                        binding.conentsViewComment.text.toString(),
                                        time,
                                        title,
                                        id
                                )
                        } else {
                                Toast.makeText(this, "입력란을 작성해주세요.", Toast.LENGTH_SHORT).show()
                        }
                }

                binding.conentsViewBack.setOnClickListener {
                        finish()
                }

                binding.thumbUpButton.setOnClickListener {
                        database.collection("collect").document(id).get()
                                .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                                val doc = task.result
                                                thumbU = doc.get("recom").toString().toInt()
                                                var pre_thumbU = thumbU + 1
                                                database.collection("collect").document(id)
                                                        .update("recom", thumbU + 1)
                                                binding.thumbUpTextview.text = pre_thumbU.toString()
                                        }
                                }
                }
                binding.thumbDownButton.setOnClickListener {
                        database.collection("collect").document(id).get()
                                .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                                val doc = task.result
                                                thumbD = doc.get("not_recom").toString().toInt()
                                                var pre_thumbD = thumbD + 1
                                                database.collection("collect").document(id)
                                                        .update("not_recom", thumbD + 1)
                                                binding.thumbDownTextview.text =
                                                        pre_thumbD.toString()
                                        }
                                }

                }

        } //onCreate

        fun writeComment(
                binding: ContentsViewActivityBinding,
                comment: String,
                time: String,
                title: String,
                id: String
        ) {
                val database = Firebase.firestore
                val uid = uid
                val writeTime = LocalDateTime.now()
                val data = hashMapOf(
                        "writetime" to writeTime.toString(),
                        "uid" to uid,
                        "comment" to comment,
                )

                database.collection("collect").document(id).collection("comment").add(data)
                        .addOnCompleteListener { it ->
                                Toast.makeText(this, "작성완료.", Toast.LENGTH_SHORT).show()
                                binding.conentsViewComment.text.clear()
                        }
                        .addOnFailureListener { it ->
                                Toast.makeText(
                                        this,
                                        "작성실패. ErrorCode: addOnFailureListener",
                                        Toast.LENGTH_LONG
                                ).show()
                        }
        }
}

