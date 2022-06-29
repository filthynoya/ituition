package com.example.i_tuition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.i_tuition.Fragment.AddFragment
import com.example.i_tuition.Fragment.HomeFragment
import com.example.i_tuition.Fragment.NotificationFragment
import com.example.i_tuition.Fragment.ProfileFragment
import com.example.i_tuition.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.iammert.library.readablebottombar.ReadableBottomBar

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    private lateinit var Auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init ()

        binding=ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        replaceFragment (HomeFragment ())

        binding.readableBottomBar.setOnItemSelectListener(object : ReadableBottomBar.ItemSelectListener{
            override fun onItemSelected(i : Int ) {
                when (i)
                {
                    0-> replaceFragment(HomeFragment())  //Toast.makeText(this@MainActivity, "home!", Toast.LENGTH_SHORT).show()
                    1-> replaceFragment(AddFragment()) //Toast.makeText(this@MainActivity, "add!", Toast.LENGTH_SHORT).show()
                    2-> replaceFragment(NotificationFragment()) //Toast.makeText(this@MainActivity, "notif!", Toast.LENGTH_SHORT).show()
                    3-> replaceFragment(ProfileFragment()) //Toast.makeText(this@MainActivity, "profile!", Toast.LENGTH_SHORT).show()
                }
            }
        })

       // private fun replaceFragment (fragment: Fragment) {

          //  val transaction = supportFragmentManager.beginTransaction()
          //  transaction.replace(R.id.container,fragment)
          //  transaction.commit()
       // }
    }

    override fun onStart() {
        super.onStart()

    }

    fun replaceFragment (fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    private fun init () {
        Auth = Firebase.auth
    }
}