package com.example.i_tuition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.i_tuition.Adapters.ChatAdapter
import com.example.i_tuition.Adapters.PostAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var UserName : TextView
    private lateinit var ChatBox : EditText
    private lateinit var SendBtn : Button

    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference

    private var list = ArrayList < ChatClass > ()

    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        init ()
        getUserName()
        getMsg ()
        SendBtn.setOnClickListener {
            val msg = ChatBox.text.toString()

            if (msg == "") {
                Toast.makeText(this, "Empty MSG", Toast.LENGTH_SHORT).show()
            } else {
                val senderUid = Auth.currentUser?.uid
                val recivedUid = intent.getStringExtra("UID")

                if (senderUid != null && recivedUid != null) {
                    sendMsg (senderUid, recivedUid, msg)
                }
            }
        }
    }

    private fun init () {
        Auth = Firebase.auth
        Database = Firebase.database.reference

        UserName = findViewById(R.id.chat_user_othername)
        ChatBox = findViewById(R.id.chat_box)
        SendBtn = findViewById(R.id.send_btn)

        recyclerView = findViewById(R.id.chat_content_list)

        layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        adapter = ChatAdapter (list)

        recyclerView.adapter = adapter
    }

    private fun getUserName () {
        val uid = intent.getStringExtra("UID")

        if (uid != null) {
            Database.child("teacher").child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fname = snapshot.child("firstName").getValue(String::class.java)
                    val lname = snapshot.child("lastName").getValue(String::class.java)

                    if (fname != null && lname != null) {
                        var name = fname + " " + lname

                        UserName.text = name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            Database.child("student").child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fname = snapshot.child("firstName").getValue(String::class.java)
                    val lname = snapshot.child("lastName").getValue(String::class.java)

                    if (fname != null && lname != null) {
                        var name = fname + " " + lname

                        UserName.text = name
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun sendMsg (senderUid : String, recivedUid : String, msg : String) {
        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderUid)
        hashMap.put("receiverId", recivedUid)
        hashMap.put("message", msg)

        Database.child("chat").push().setValue(hashMap)
        ChatBox.text.clear()
    }

    private fun getMsg () {
        Database.child("chat").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (snap in snapshot.children) {
                    val ssid = snap.child ("senderId").getValue(String::class.java)
                    val rsid = snap.child ("receiverId").getValue(String::class.java)
                    val msg = snap.child ("message").getValue(String::class.java)


                    val chat = ChatClass (
                        ssid,
                        rsid,
                        msg
                            )

                    val suid = Auth.currentUser?.uid.toString()
                    val ruid = intent.getStringExtra("UID").toString()

                    if (chat!!.senderId.equals(suid) && chat!!.receiverId.equals(ruid) ||
                        chat!!.senderId.equals(ruid) && chat!!.receiverId.equals(suid)
                    ) {
                        list.add(chat)
                    }
                }

                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}