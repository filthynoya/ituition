package com.example.i_tuition.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.i_tuition.MainActivity
import com.example.i_tuition.PostClass
import com.example.i_tuition.R
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

class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ClassEditText : EditText
    private lateinit var SubEditText : EditText
    private lateinit var Sun : CheckBox
    private lateinit var Mon : CheckBox
    private lateinit var Tue : CheckBox
    private lateinit var Wed : CheckBox
    private lateinit var Thu : CheckBox
    private lateinit var Fri : CheckBox
    private lateinit var Sat : CheckBox
    private lateinit var SalEditText : EditText
    private lateinit var MsgEditText : EditText
    private lateinit var AddBtn : Button

    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference

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
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        ClassEditText = view.findViewById(R.id.add_class_edit)
        SubEditText = view.findViewById(R.id.add_sub_edit)
        Sun = view.findViewById(R.id.sunday)
        Mon = view.findViewById(R.id.monday)
        Tue = view.findViewById(R.id.tuesday)
        Wed = view.findViewById(R.id.wednesday)
        Thu = view.findViewById(R.id.thursday)
        Fri = view.findViewById(R.id.friday)
        Sat = view.findViewById(R.id.saturday)
        SalEditText = view.findViewById(R.id.add_fee_edit)
        MsgEditText = view.findViewById(R.id.add_msg_edit)
        AddBtn = view.findViewById(R.id.add_post_btn)


        return view
    }

    override fun onStart() {
        super.onStart()

        checkForPost ()
    }

    private fun init () {
        Auth = Firebase.auth
        Database = Firebase.database.reference
    }

    private fun checkForPost () {
        AddBtn.setOnClickListener {
            if (checkForError ()) {
                pushPost ()
            }
        }
    }

    private fun checkForError () : Boolean {
        if (!Sun.isChecked && !Mon.isChecked && !Tue.isChecked && !Wed.isChecked &&
            !Thu.isChecked && !Fri.isChecked && !Sat.isChecked) {
            Toast.makeText(activity, "Please Select a Weekday", Toast.LENGTH_SHORT).show ()

            return false
        }

        if (ClassEditText.text.toString() == "" || SalEditText.text.toString() == "" ||
                MsgEditText.text.toString() == "" || SubEditText.text.toString() == "") {
            Toast.makeText(activity, "Please Fill up all Info", Toast.LENGTH_SHORT).show ()

            return false
        }

        return true
    }

    private fun pushPost () {
        val hash = getRandomString(20)
        val weekDays = arrayListOf<Boolean>(
            Sun.isChecked,
            Mon.isChecked,
            Tue.isChecked,
            Wed.isChecked,
            Thu.isChecked,
            Fri.isChecked,
            Sat.isChecked
        )

        var uid = ""
        val currUser = Auth.currentUser

        if (currUser != null) {
            uid = currUser.uid
        }

        Database.child ("teacher").child (uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val key = postSnapshot.key

                    if (key == "institution") {
                        Database.child("post").child("teacher").child(uid).child(hash).setValue(
                            PostClass (
                                ClassEditText.text.toString(),
                                SubEditText.text.toString(),
                                weekDays,
                                SalEditText.text.toString(),
                                MsgEditText.text.toString()
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(activity, "Post Added", Toast.LENGTH_SHORT).show()
                            ClassEditText.text.clear()
                            SubEditText.text.clear()
                            Sun.isChecked = false
                            Mon.isChecked = false
                            Tue.isChecked = false
                            Wed.isChecked = false
                            Thu.isChecked = false
                            Fri.isChecked = false
                            Sat.isChecked = false
                            SalEditText.text.clear()
                            MsgEditText.text.clear()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        Database.child ("student").child (uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val key = postSnapshot.key

                    if (key == "address") {
                        Database.child("post").child("student").child(uid).child(hash).setValue(
                            PostClass (
                                ClassEditText.text.toString(),
                                SubEditText.text.toString(),
                                weekDays,
                                SalEditText.text.toString(),
                                MsgEditText.text.toString()
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(activity, "Post Added", Toast.LENGTH_SHORT).show()
                            ClassEditText.text.clear()
                            SubEditText.text.clear()
                            Sun.isChecked = false
                            Mon.isChecked = false
                            Tue.isChecked = false
                            Wed.isChecked = false
                            Thu.isChecked = false
                            Fri.isChecked = false
                            Sat.isChecked = false
                            SalEditText.text.clear()
                            MsgEditText.text.clear()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}