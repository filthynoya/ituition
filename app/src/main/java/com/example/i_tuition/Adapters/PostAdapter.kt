package com.example.i_tuition.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.i_tuition.PostItem
import com.example.i_tuition.R
import com.example.i_tuition.UidHashData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostAdapter (val posts : ArrayList <PostItem>, val uids : ArrayList <UidHashData>) : RecyclerView.Adapter<PostAdapter.ViewHolder> () {
    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)

        Auth = Firebase.auth
        Database = Firebase.database.reference

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        holder.fname.text = posts[position].FirstName
        holder.lname.text = posts[position].LastName
        holder.Class.text = posts[position].Class
        holder.fee.text = posts[position].Fee
        holder.weekDays.text = posts[position].WeekDays
        holder.sub.text = posts[position].Subjects
        holder.msg.text = posts[position].Description

        holder.btn.setOnClickListener {
            if (uids[position].uid != null && uids[position].hash != null) {
                Database.child("post").child("student").child(uids[position].uid.toString())
                    .child(uids[position].hash.toString()).removeValue()

                Database.child("post").child("teacher").child(uids[position].uid.toString())
                    .child(uids[position].hash.toString()).removeValue()

                val currUser = Auth.currentUser

                if (currUser != null) {
                    val uid = currUser.uid

                    Database.child ("userChatList").child(uid).child(uids[position].uid.toString())
                        .setValue("OK")

                    Database.child ("userChatList").child(uids[position].uid.toString()).child(uid)
                        .setValue("OK")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        var fname : TextView
        var lname : TextView
        var Class : TextView
        var fee : TextView
        var weekDays : TextView
        var sub : TextView
        var msg : TextView
        var btn : Button

        init {
            fname = itemView.findViewById(R.id.post_fname)
            lname = itemView.findViewById(R.id.post_lname)
            Class = itemView.findViewById(R.id.post_class)
            fee = itemView.findViewById(R.id.post_fee)
            weekDays = itemView.findViewById(R.id.all_weeks)
            sub = itemView.findViewById(R.id.post_sub)
            msg = itemView.findViewById(R.id.post_msg)
            btn = itemView.findViewById(R.id.accept_btn)
        }
    }
}