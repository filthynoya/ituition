package com.example.i_tuition.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.i_tuition.Login
import com.example.i_tuition.MainActivity
import com.example.i_tuition.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var ProfilePic : ImageView
    private lateinit var NameTextView : TextView
    private lateinit var AgeTextView : TextView
    private lateinit var EmailTextView : TextView
    private lateinit var ProTextView : TextView
    private lateinit var InsTextView : TextView
    private lateinit var UnderTextView : TextView
    private lateinit var AreaTextView : TextView
    private lateinit var VerifyTextView : TextView
    private lateinit var LogOut : Button
    private lateinit var Verify : Button
    private lateinit var ProgressBar: ProgressBar

    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference
    private lateinit var Storage : StorageReference
    private lateinit var UID : String

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        NameTextView = view.findViewById(R.id.profile_name)
        AgeTextView = view.findViewById(R.id.profile_age)
        EmailTextView = view.findViewById(R.id.profile_email)
        ProTextView = view.findViewById(R.id.profile_profession)
        InsTextView = view.findViewById(R.id.profile_ins)
        UnderTextView = view.findViewById(R.id.profile_under)
        AreaTextView = view.findViewById(R.id.profile_area)
        LogOut = view.findViewById(R.id.log_out_btn)
        Verify = view.findViewById(R.id.verify_btn)
        VerifyTextView = view.findViewById(R.id.verify_text)
        ProfilePic = view.findViewById(R.id.profile_avater)
        ProgressBar = view.findViewById(R.id.progressbarID)

        return view
    }

    override fun onStart() {
        super.onStart()
        ProgressBar.visibility =View.VISIBLE
        checkIfUserLoggedIn ()
        checkForVerify()
        checkForLogOut()
    }

    private fun init () {
        Auth = Firebase.auth
        Database = Firebase.database.reference

        val currUser = Auth.currentUser

        if (currUser != null) {
            UID = currUser.uid
        }

        Storage = Firebase.storage.reference

        fetchAllRelevantData ()
    }

    private fun checkForVerify () {
        val currentUser = Auth.currentUser

        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                Verify.visibility = View.GONE
                VerifyTextView.visibility = View.GONE
            } else {
                Verify.setOnClickListener {
                    currentUser.sendEmailVerification().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Email Sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "Error sending Email.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkForLogOut () {
        val currentUser = Auth.currentUser

        if (currentUser != null) {
            LogOut.setOnClickListener {
                Auth.signOut()

                val switchActivityIntent = Intent(activity, Login::class.java)
                startActivity(switchActivityIntent)
            }
        }
    }

    private fun fetchAllRelevantData () {
        studentData()
        teacherData()
        updateProfilePic()
    }

    private fun studentData () {
        Database.child ("student").child (UID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val key = postSnapshot.key
                    val data = postSnapshot.value
                    var temp = ""

                    when (key) {
                        "age" -> {
                            temp = ""
                            temp += AgeTextView.text as String
                            temp += data.toString()
                            AgeTextView.text = temp
                        }
                        "email" -> {
                            temp = ""
                            temp += EmailTextView.text as String
                            temp += data.toString()
                            EmailTextView.text = temp
                        }
                        "firstName" -> {
                            temp = ""
                            temp += NameTextView.text as String
                            temp += data.toString()
                            temp += " "
                            NameTextView.text = temp
                        }
                        "lastName" -> {
                            temp = ""
                            temp += NameTextView.text as String
                            temp += data.toString()
                            NameTextView.text = temp
                        }
                        "address" -> {
                            temp = ProTextView.text as String
                            temp += " Student"
                            ProTextView.text = temp
                            AreaTextView.visibility = View.VISIBLE
                            temp = ""
                            temp += AreaTextView.text as String
                            temp += data.toString()
                            AreaTextView.text = temp
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun teacherData () {
        Database.child ("teacher").child (UID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val key = postSnapshot.key
                    val data = postSnapshot.value
                    var temp = ""

                    when (key) {
                        "age" -> {
                            temp = ""
                            temp += AgeTextView.text as String
                            temp += data.toString()
                            AgeTextView.text = temp
                        }
                        "email" -> {
                            temp = ""
                            temp += EmailTextView.text as String
                            temp += data.toString()
                            EmailTextView.text = temp
                        }
                        "firstName" -> {
                            temp = ""
                            temp += NameTextView.text as String
                            temp += data.toString()
                            temp += " "
                            NameTextView.text = temp
                        }
                        "lastName" -> {
                            temp = ""
                            temp += NameTextView.text as String
                            temp += data.toString()
                            NameTextView.text = temp
                        }
                        "institution" -> {
                            InsTextView.visibility = View.VISIBLE
                            temp = ProTextView.text as String
                            temp += " Teacher"
                            ProTextView.text = temp
                            temp = ""
                            temp += InsTextView.text as String
                            temp += data.toString()
                            InsTextView.text = temp
                        }
                        "undergraduate" -> {
                            UnderTextView.visibility = View.VISIBLE
                            temp = ""
                            if (data as Boolean) {
                                temp += UnderTextView.text as String
                                temp += " Yes"
                                UnderTextView.text = temp
                            } else {
                                temp += UnderTextView.text as String
                                temp += " No"
                                UnderTextView.text = temp
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun checkIfUserLoggedIn () {
        val currUser = Auth.currentUser

        if (currUser == null) {
            val switchActivityIntent = Intent(activity, Login::class.java)
            startActivity(switchActivityIntent)
        }
    }

    private fun updateProfilePic () {
        Storage.child("${UID}/profilePic/${UID}").downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(ProfilePic)
            ProgressBar.visibility =View.INVISIBLE
        }.addOnFailureListener {
            Toast.makeText(activity, "FAILED", Toast.LENGTH_SHORT).show()
            ProgressBar.visibility =View.INVISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}