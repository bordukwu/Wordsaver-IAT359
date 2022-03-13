package com.raphael.wordsaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    //Launcher Activity using google sign in
    private lateinit var signInButton: SignInButton
    private lateinit var googleSignInClient:GoogleSignInClient
    private val TAG = "SignInActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signInButton = findViewById(R.id.signInButton)

        // official documentation for google firebase Authentication

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso) // this will give info about signed in IDS

        auth = Firebase.auth
        signInButton.setOnClickListener{
            signIn()
        }
    }

    // automatically log in user if user already exists

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn()
    {
        // sign in intent to show all available ids

        val signInIntent = googleSignInClient.signInIntent
        // start Activity For Result
        getResult.launch(signInIntent)
    }
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(it.resultCode== RESULT_OK)
        {
            // this will get data for signIn
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        // get the id's and data in background using ID caroutine
        GlobalScope.launch(Dispatchers.IO)
        {
            // Now we need auth to get user
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            // now update UI using main coroutine

            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null)
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}