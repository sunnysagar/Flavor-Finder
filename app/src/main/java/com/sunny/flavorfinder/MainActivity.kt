package com.sunny.flavorfinder

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sunny.flavorfinder.fragment.FavouriteFragment
import com.sunny.flavorfinder.fragment.HomeDashboard

class MainActivity : AppCompatActivity() {


    private lateinit var gsc: GoogleSignInClient
    lateinit var bottomNav : BottomNavigationView
    lateinit var manager :RequestManager

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        manager = RequestManager(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)


        loadFragment(HomeDashboard())

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)!!
        val selectedColor = ContextCompat.getColor(this, R.color.orange)
        val unselectedColor = ContextCompat.getColor(this, R.color.black)

        bottomNav.itemIconTintList = ColorStateList.valueOf(unselectedColor)
        bottomNav.itemTextColor = ColorStateList.valueOf(unselectedColor)

        val menu = bottomNav.menu
        val menuItem = menu.findItem(R.id.menu_home)

            bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    loadFragment(HomeDashboard())
                    menuItem.iconTintList = ColorStateList.valueOf(selectedColor)
                    bottomNav.itemTextColor = ColorStateList.valueOf(selectedColor)
                    true
                }

                R.id.menu_fav -> {
                    loadFragment(FavouriteFragment())
                    menuItem.iconTintList = ColorStateList.valueOf(selectedColor)
                    bottomNav.itemTextColor = ColorStateList.valueOf(selectedColor)
                    true
                }
                R.id.menu_logout->{
                    signOut()
                    Toast.makeText(this,"Thank You", Toast.LENGTH_SHORT).show()
                    menuItem.iconTintList = ColorStateList.valueOf(selectedColor)
                    bottomNav.itemTextColor = ColorStateList.valueOf(selectedColor)
                    true
                }

                else -> {
                    loadFragment(HomeDashboard())
                    true
                }
            }
        }

    }

    fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

    private fun signOut() {
        gsc.signOut().addOnCompleteListener {
            finish()
            startActivity(Intent(this, SigninActivity::class.java))
        }
    }


}