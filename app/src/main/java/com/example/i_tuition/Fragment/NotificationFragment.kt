package com.example.i_tuition.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.i_tuition.Adapters.PostAdapter
import com.example.i_tuition.Adapters.UserAdapter
import com.example.i_tuition.PostItem
import com.example.i_tuition.R
import com.example.i_tuition.SingleUserList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotificationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference

    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<UserAdapter.ViewHolder>? = null
    private var list : ArrayList <SingleUserList> = ArrayList()
    private var uidlist : ArrayList < String > = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        init ()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        recyclerView = view.findViewById(R.id.chat_user_list)

        layoutManager = LinearLayoutManager(activity)

        recyclerView.layoutManager = layoutManager
        adapter = activity?.let { UserAdapter (it, list, uidlist) }

        recyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()

        getUserList ()
    }

    private fun init () {
        Auth = Firebase.auth
        Database = Firebase.database.reference
    }

    private fun getUserList () {
        val currentUser = Auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            Database.child ("userChatList").child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapShot in snapshot.children) {
                        list.clear()
                        uidlist.clear()
                        val key = postSnapShot.key

                        if (key != null) {
                            Database.child("teacher").child(key).addValueEventListener(
                                object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val fname = snapshot.child("firstName").getValue(String::class.java)
                                        val lname = snapshot.child("lastName").getValue(String::class.java)

                                        if (fname != null && lname != null) {
                                            var name = fname + " " + lname

                                            list.add(SingleUserList(name))
                                            uidlist.add(key)

                                            adapter?.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            )

                            Database.child("student").child(key).addValueEventListener(
                                object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val fname = snapshot.child("firstName").getValue(String::class.java)
                                        val lname = snapshot.child("lastName").getValue(String::class.java)

                                        if (fname != null && lname != null) {
                                            var name = fname + " " + lname

                                            list.add(SingleUserList(name))
                                            uidlist.add(key)

                                            adapter?.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}