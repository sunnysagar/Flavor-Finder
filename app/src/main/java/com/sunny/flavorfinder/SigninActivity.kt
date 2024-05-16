package com.sunny.flavorfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers.Main

class SigninActivity : AppCompatActivity() {

    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var btnGoogle: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnGoogle = findViewById(R.id.btnGoogle)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null)
        {
            navigateToSecondActivity()
        }
        else {
            googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        task.getResult(ApiException::class.java)
                        navigateToSecondActivity()
                    } catch (e: ApiException) {
                        Log.e("SignupActivity", "Google sign in failed: ${e.statusCode}")
                        Toast.makeText(applicationContext, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnGoogle.setOnClickListener {
                signIn()
            }
        }


    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

//    private fun navigateToMainActivity(account:GoogleSignInAccount){
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("name", )
//
//    }

    private fun navigateToSecondActivity(){
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }
}