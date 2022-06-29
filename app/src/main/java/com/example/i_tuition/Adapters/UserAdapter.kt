package com.example.i_tuition.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.i_tuition.ChatActivity
import com.example.i_tuition.MainActivity
import com.example.i_tuition.R
import com.example.i_tuition.SingleUserList

class UserAdapter (val context : Context, val userList : ArrayList <SingleUserList>, val uidList : ArrayList <String> )
    : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_list_single, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentName = userList[position].FullName

        holder.fullName.text = currentName

        holder.layout.setOnClickListener {
            val nextUid = uidList[position]
            val switchActivityIntent = Intent(context, ChatActivity::class.java)
            switchActivityIntent.putExtra("UID", nextUid)
            context.startActivity(switchActivityIntent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val fullName : TextView
        val layout : LinearLayout

        init {
            fullName = itemView.findViewById(R.id.chat_user_name)
            layout = itemView.findViewById(R.id.user_layout)
        }
    }
}