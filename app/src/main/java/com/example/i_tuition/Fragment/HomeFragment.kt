package com.example.i_tuition.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.i_tuition.Adapters.PostAdapter
import com.example.i_tuition.PostClass
import com.example.i_tuition.PostItem
import com.example.i_tuition.R
import com.example.i_tuition.UidHashData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference

    private var layoutManager : RecyclerView.LayoutManager? = null
    private var adapter : RecyclerView.Adapter<PostAdapter.ViewHolder>? = null
    private var list : ArrayList < PostItem > = ArrayList()


    private var uidhashlist : ArrayList < UidHashData > = ArrayList()

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.post_list)

        layoutManager = LinearLayoutManager(activity)

        recyclerView.layoutManager = layoutManager
        adapter = PostAdapter(list, uidhashlist)

        recyclerView.adapter = adapter

        return view
    }

    override fun onStart() {
        super.onStart()
        getPosts ()
    }

    private fun init () {
        Auth = Firebase.auth
        Database = Firebase.database.reference
    }

    private fun getPosts () {
        val curr = Auth.currentUser

        if (curr != null) {
            val uid = curr.uid

            Database.child("teacher").child(uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val key = postSnapshot.key

                        if (key == "institution") {
                            getPostStudent ()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            Database.child("student").child(uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val key = postSnapshot.key

                        if (key == "address") {
                            getPostTeacher ()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun getPostTeacher () {
        Database.child ("post").child("teacher").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    list.clear ()
                    uidhashlist.clear ()
                    val uid = postSnapshot.key

                    if (uid != null) {
                        Database.child("teacher").child(uid).addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val fname = snapshot.child("firstName").getValue(String::class.java)
                                val lname = snapshot.child("lastName").getValue(String::class.java)

                                for (postSnapshot1 in postSnapshot.children) {
                                    var newPost = PostItem (
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""
                                    )

                                    newPost.Class += postSnapshot1.child("class").getValue(String::class.java)
                                    newPost.Fee += postSnapshot1.child("fee").getValue(String::class.java)
                                    newPost.Subjects += postSnapshot1.child("subjects").getValue(String::class.java)
                                    newPost.Description += postSnapshot1.child("description").getValue(String::class.java)

                                    var str = ""

                                    var sun = postSnapshot1.child("weekDays/0").getValue(Boolean::class.java)
                                    var mon = postSnapshot1.child("weekDays/1").getValue(Boolean::class.java)
                                    var tue = postSnapshot1.child("weekDays/2").getValue(Boolean::class.java)
                                    var wed = postSnapshot1.child("weekDays/3").getValue(Boolean::class.java)
                                    var thu = postSnapshot1.child("weekDays/4").getValue(Boolean::class.java)
                                    var fri = postSnapshot1.child("weekDays/5").getValue(Boolean::class.java)
                                    var sat = postSnapshot1.child("weekDays/6").getValue(Boolean::class.java)

                                    if (sun != null && mon != null && tue != null && wed != null &&
                                        thu != null && fri != null && sat != null) {
                                        if (sun) {
                                            str += "SUN "
                                        }
                                        if (mon) {
                                            str += "MON "
                                        }
                                        if (tue) {
                                            str += "TUE "
                                        }
                                        if (wed) {
                                            str += "WED "
                                        }
                                        if (thu) {
                                            str += "THU "
                                        }
                                        if (fri) {
                                            str += "FRI "
                                        }
                                        if (sat) {
                                            str += "SAT "
                                        }
                                    }

                                    newPost.WeekDays += str
                                    newPost.FirstName += fname
                                    newPost.LastName += lname

                                    list.add(newPost)
                                    adapter?.notifyDataSetChanged()

                                    uidhashlist.add(UidHashData(uid, postSnapshot1.key))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getPostStudent () {
        Database.child ("post").child("student").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    list.clear ()
                    uidhashlist.clear ()
                    val uid = postSnapshot.key

                    if (uid != null) {
                        Database.child("student").child(uid).addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val fname = snapshot.child("firstName").getValue(String::class.java)
                                val lname = snapshot.child("lastName").getValue(String::class.java)

                                for (postSnapshot1 in postSnapshot.children) {
                                    var newPost = PostItem (
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""
                                    )

                                    newPost.Class += postSnapshot1.child("class").getValue(String::class.java)
                                    newPost.Fee += postSnapshot1.child("fee").getValue(String::class.java)
                                    newPost.Subjects += postSnapshot1.child("subjects").getValue(String::class.java)
                                    newPost.Description += postSnapshot1.child("description").getValue(String::class.java)

                                    var str = ""

                                    var sun = postSnapshot1.child("weekDays/0").getValue(Boolean::class.java)
                                    var mon = postSnapshot1.child("weekDays/1").getValue(Boolean::class.java)
                                    var tue = postSnapshot1.child("weekDays/2").getValue(Boolean::class.java)
                                    var wed = postSnapshot1.child("weekDays/3").getValue(Boolean::class.java)
                                    var thu = postSnapshot1.child("weekDays/4").getValue(Boolean::class.java)
                                    var fri = postSnapshot1.child("weekDays/5").getValue(Boolean::class.java)
                                    var sat = postSnapshot1.child("weekDays/6").getValue(Boolean::class.java)

                                    if (sun != null && mon != null && tue != null && wed != null &&
                                        thu != null && fri != null && sat != null) {
                                        if (sun) {
                                            str += "SUN "
                                        }
                                        if (mon) {
                                            str += "MON "
                                        }
                                        if (tue) {
                                            str += "TUE "
                                        }
                                        if (wed) {
                                            str += "WED "
                                        }
                                        if (thu) {
                                            str += "THU "
                                        }
                                        if (fri) {
                                            str += "FRI "
                                        }
                                        if (sat) {
                                            str += "SAT "
                                        }
                                    }

                                    newPost.WeekDays += str
                                    newPost.FirstName += fname
                                    newPost.LastName += lname

                                    list.add(newPost)
                                    adapter?.notifyDataSetChanged()

                                    uidhashlist.add(UidHashData(uid, postSnapshot1.key))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}