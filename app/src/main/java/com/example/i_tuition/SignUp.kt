package com.example.i_tuition

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class SignUp : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var FirstPanalTextView : TextView
    private lateinit var TeacherRadio : RadioButton
    private lateinit var StudentRadio : RadioButton
    private lateinit var SecondPanalTextView : TextView
    private lateinit var FirstNameEditText : EditText
    private lateinit var LastNameEditText : EditText
    private lateinit var AgeEditText : EditText
    private lateinit var ThirdTeacherTextView1 : TextView
    private lateinit var InstitutionSpin : Spinner
    private lateinit var ThirdTeacherTextView2 : TextView
    private lateinit var UnderRadio : RadioButton
    private lateinit var NotUnderRadio : RadioButton
    private lateinit var ThirdStudentTextView1 : TextView
    private lateinit var AreaEditText : EditText
    private lateinit var ThirdStudentTextView2 : TextView
    private lateinit var ForthPanalTextView : TextView
    private lateinit var EmailEditText : EditText
    private lateinit var FinalPanalTextView1 : TextView
    private lateinit var PasswordEditText : EditText
    private lateinit var ConfirmPasswordEditText : EditText
    private lateinit var NextBtn : Button
    private lateinit var ProfilePic : ImageView
    private lateinit var UploadBtn : Button

    private lateinit var Auth : FirebaseAuth
    private lateinit var Database : DatabaseReference
    private lateinit var Storage : StorageReference
    private lateinit var FirstName : String
    private lateinit var LastName : String
    private lateinit var Age : String
    private lateinit var Area : String
    private lateinit var Email : String
    private lateinit var Password : String
    private var isTeacher : Boolean = false
    private var isUnder : Boolean = false
    private var fileUploaded : Boolean = false
    private lateinit var Institution : String
    private lateinit var ImageUri: Uri

    private val SpinArr = arrayOf("AUST", "BUET", "DU", "NSU", "BRAC")

    private var State : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init ()
    }

    override fun onStart() {
        super.onStart()

        checkIfTheUserLoggedIn ()
        updateUi ()
        checkForNext ()
    }

    private fun init () {
        Auth = Firebase.auth
        Database = Firebase.database.reference
        Storage = Firebase.storage.reference

        ProfilePic = findViewById(R.id.sign_up_pic)
        UploadBtn = findViewById(R.id.sign_up_upload)
        FirstPanalTextView = findViewById(R.id.first_panal_textview)
        TeacherRadio = findViewById(R.id.first_panal_teacher_radio)
        StudentRadio = findViewById(R.id.first_panal_student_radio)
        SecondPanalTextView = findViewById(R.id.second_panal_textview)
        FirstNameEditText = findViewById(R.id.sign_up_fname)
        LastNameEditText = findViewById(R.id.sign_up_lname)
        AgeEditText = findViewById(R.id.sign_up_age)
        ThirdTeacherTextView1 = findViewById(R.id.third_teacher_panal_textview_1)
        InstitutionSpin = findViewById(R.id.spin)
        ThirdTeacherTextView2 = findViewById(R.id.third_teacher_panal_textview_2)
        UnderRadio = findViewById(R.id.under_radio)
        NotUnderRadio = findViewById(R.id.not_under_lol_radio)
        ThirdStudentTextView1 = findViewById(R.id.third_student_panal_textview_1)
        AreaEditText = findViewById(R.id.sign_up_area)
        ThirdStudentTextView2 = findViewById(R.id.third_student_panal_textview_2)
        ForthPanalTextView = findViewById(R.id.fourth_panal_textview)
        EmailEditText = findViewById(R.id.sign_up_email)
        FinalPanalTextView1 = findViewById(R.id.final_panal_textview_1)
        PasswordEditText = findViewById(R.id.sign_up_pass)
        ConfirmPasswordEditText = findViewById(R.id.sign_up_pass_re)
        NextBtn = findViewById(R.id.sign_up_next_btn)
        InstitutionSpin.onItemSelectedListener = this

        val Adapter = ArrayAdapter (this, android.R.layout.simple_spinner_item, SpinArr)

        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        InstitutionSpin.adapter = Adapter
    }

    private fun checkIfTheUserLoggedIn () {
        val user = Auth.currentUser

        if (user != null) {
            val switchActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(switchActivityIntent)
        }
    }

    private fun updateUi () {
        TeacherRadio.setOnClickListener {
            if (StudentRadio.isChecked) {
                StudentRadio.isChecked = false
            }
        }

        StudentRadio.setOnClickListener {
            if (TeacherRadio.isChecked) {
                TeacherRadio.isChecked = false
            }
        }

        UploadBtn.setOnClickListener {
            val gallery = Intent()
            gallery.action = Intent.ACTION_PICK
            gallery.type = "image/"
            startActivityForResult(gallery, 100)
        }

        UnderRadio.setOnClickListener {
            if (NotUnderRadio.isChecked) {
                NotUnderRadio.isChecked = false
            }
        }

        NotUnderRadio.setOnClickListener {
            if (UnderRadio.isChecked) {
                UnderRadio.isChecked = false
            }
        }
    }

    private fun checkForNext () {
        NextBtn.setOnClickListener {
            when (State) {
                1 -> phrase1 ()
                2 -> phrase1half ()
                3 -> phrase2 ()
                4 -> phrase3 ()
                5 -> phrase4 ()
                6 -> phrase5 ()
            }
        }
    }

    private fun phrase1 () {
        if (!TeacherRadio.isChecked && !StudentRadio.isChecked) {
            Toast.makeText(baseContext, "Please select an option.",
                Toast.LENGTH_SHORT).show()

            return;
        }

        FirstPanalTextView.visibility = View.GONE
        TeacherRadio.visibility = View.GONE
        StudentRadio.visibility = View.GONE
        ProfilePic.visibility = View.VISIBLE
        UploadBtn.visibility = View.VISIBLE

        State += 1
    }

    private fun phrase1half () {
        if (!fileUploaded) {
            Toast.makeText(
                this,
                "Please Upload an Image.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        ProfilePic.visibility = View.GONE
        UploadBtn.visibility = View.GONE
        SecondPanalTextView.visibility = View.VISIBLE
        FirstNameEditText.visibility = View.VISIBLE
        LastNameEditText.visibility = View.VISIBLE
        AgeEditText.visibility = View.VISIBLE

        State += 1
    }

    private fun phrase2 () {
        if (FirstNameEditText.text.toString() == "" ||
            LastNameEditText.text.toString() == "" ||
            AgeEditText.text.toString() == ""
        ) {
            Toast.makeText(baseContext, "Please fill up all necessary information.",
                Toast.LENGTH_SHORT).show()

            return
        }

        SecondPanalTextView.visibility = View.GONE
        FirstNameEditText.visibility = View.GONE
        LastNameEditText.visibility = View.GONE
        AgeEditText.visibility = View.GONE

        if (TeacherRadio.isChecked) {
            ThirdTeacherTextView1.visibility = View.VISIBLE
            InstitutionSpin.visibility = View.VISIBLE
            ThirdTeacherTextView2.visibility = View.VISIBLE
            UnderRadio.visibility = View.VISIBLE
            NotUnderRadio.visibility = View.VISIBLE
        } else {
            ThirdStudentTextView1.visibility = View.VISIBLE
            AreaEditText.visibility = View.VISIBLE
            ThirdStudentTextView2.visibility = View.VISIBLE
        }

        State += 1
    }

    private fun phrase3 () {
        if (TeacherRadio.isChecked) {
            if (!UnderRadio.isChecked && !NotUnderRadio.isChecked) {
                Toast.makeText(baseContext, "Please select an option.",
                    Toast.LENGTH_SHORT).show()

                return;
            }

            ThirdTeacherTextView1.visibility = View.GONE
            InstitutionSpin.visibility = View.GONE
            ThirdTeacherTextView2.visibility = View.GONE
            UnderRadio.visibility = View.GONE
            NotUnderRadio.visibility = View.GONE
        } else {
            if (AreaEditText.text.toString() == "") {
                Toast.makeText(baseContext, "Please fill up all necessary information.",
                    Toast.LENGTH_SHORT).show()

                return
            }

            ThirdStudentTextView1.visibility = View.GONE
            AreaEditText.visibility = View.GONE
            ThirdStudentTextView2.visibility = View.GONE
        }

        ForthPanalTextView.visibility = View.VISIBLE
        EmailEditText.visibility = View.VISIBLE

        State += 1
    }

    private fun phrase4 () {
        if (EmailEditText.text.toString() == "") {
            Toast.makeText(baseContext, "Please fill up all necessary information.",
                Toast.LENGTH_SHORT).show()

            return
        }

        if (TeacherRadio.isChecked) {
            var endOfStr = InstitutionSpin.selectedItem.toString().lowercase() + ".edu"

            if (EmailEditText.text.toString().indexOf(endOfStr) == -1) {
                Toast.makeText(baseContext, "Enter Your Institution Email.",
                    Toast.LENGTH_SHORT).show()

                return
            }
        }

        ForthPanalTextView.visibility = View.GONE
        EmailEditText.visibility = View.GONE
        FinalPanalTextView1.visibility = View.VISIBLE
        PasswordEditText.visibility = View.VISIBLE
        ConfirmPasswordEditText.visibility = View.VISIBLE

        State += 1
    }

    private fun phrase5 () {
        if (PasswordEditText.text.toString() != ConfirmPasswordEditText.text.toString()) {
            Toast.makeText(baseContext, "Password does not match.",
                Toast.LENGTH_SHORT).show()

            return
        }

        if (PasswordEditText.text.toString() == "" ||
            ConfirmPasswordEditText.text.toString() == "") {

            Toast.makeText(baseContext, "Please fill up all necessary information.",
                Toast.LENGTH_SHORT).show()

            return
        }

        Auth.createUserWithEmailAndPassword(EmailEditText.text.toString(), PasswordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = Auth.currentUser

                    if (currentUser != null) {
                        val uid = currentUser.uid

                        update ()

                        Storage.child("$uid/profilePic/$uid").putFile(ImageUri)

                        if (isTeacher) {
                            Database.child("teacher").child (uid).setValue(
                                TeacherClass (FirstName, LastName, Age, Institution, isUnder, Email)
                            )
                        } else {
                            Database.child("student").child (uid).setValue(
                                StudentClass (FirstName, LastName, Age, Area, Email)
                            )
                        }

                        val switchActivityIntent = Intent(this, MainActivity::class.java)
                        startActivity(switchActivityIntent)
                    }
                } else {
                    try {
                        throw task.exception!!
                    } catch (e : FirebaseAuthWeakPasswordException) {
                        Toast.makeText(baseContext, "Weak Password.",
                            Toast.LENGTH_SHORT).show()
                    } catch (e : FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(baseContext, "Invalid Email.",
                            Toast.LENGTH_SHORT).show()

                        State -= 1

                        ForthPanalTextView.visibility = View.VISIBLE
                        EmailEditText.visibility = View.VISIBLE
                        FinalPanalTextView1.visibility = View.GONE
                        PasswordEditText.visibility = View.GONE
                        ConfirmPasswordEditText.visibility = View.GONE
                    } catch (e : FirebaseAuthUserCollisionException) {
                        Toast.makeText(baseContext, "User already exists.",
                            Toast.LENGTH_SHORT).show()

                        State -= 1

                        ForthPanalTextView.visibility = View.VISIBLE
                        EmailEditText.visibility = View.VISIBLE
                        FinalPanalTextView1.visibility = View.GONE
                        PasswordEditText.visibility = View.GONE
                        ConfirmPasswordEditText.visibility = View.GONE
                    }
                }
            }
    }

    private fun update () {
        FirstName = FirstNameEditText.text.toString()
        LastName = LastNameEditText.text.toString()
        Age = AgeEditText.text.toString()
        Area = AreaEditText.text.toString()
        Email = EmailEditText.text.toString()
        Password = PasswordEditText.text.toString()
        isTeacher = TeacherRadio.isChecked
        isUnder = UnderRadio.isChecked
        Institution = InstitutionSpin.selectedItem.toString()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Institution = SpinArr[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            fileUploaded = true
            ImageUri = data?.data!!
            ProfilePic.setImageURI(ImageUri)
            ProfilePic.setBackgroundResource(R.drawable.border_img)
        }
    }
}
