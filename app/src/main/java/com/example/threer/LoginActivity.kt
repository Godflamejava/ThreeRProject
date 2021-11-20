package com.example.threer;

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.threer.MainActivity
import com.example.threer.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity: AppCompatActivity() {
    private lateinit var account: Auth0
    private lateinit var code:String
    lateinit var progressBar:ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        code  =intent.getStringExtra("code").toString()



        account = Auth0(
                "FL1RahJn508MJwChSQITSCCRorQbk0K6",
                "dev-zvjmyfrq.us.auth0.com"
        )
        if(code == "exit")
            logout()

        progressBar=findViewById<ProgressBar>(R.id.progressBar)

        val enter=findViewById<Button>(R.id.button)
        enter.setOnClickListener {
            progressBar.visibility= View.VISIBLE
            loginWithBrowser()
        }



    }
    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.

        WebAuthProvider.login(account)
                .withScheme("demo")
                .withScope("openid profile email")
                // Launch the authentication passing the callback where the results will be received
                .start(this, object : Callback<Credentials, AuthenticationException> {
                    // Called when there is an authentication failure
                    override fun onFailure(exception: AuthenticationException) {
                        // Something went wrong!
                    }

                    // Called when authentication completed successfully
                    override fun onSuccess(credentials: Credentials) {

                        val accessToken = credentials.accessToken
                        showUserProfile(accessToken)
                    }
                })
    }
    private fun showUserProfile(accessToken: String) {
        var client = AuthenticationAPIClient(account)
        client.userInfo(accessToken)
                .start(object : Callback<UserProfile, AuthenticationException> {
                    override fun onFailure(exception: AuthenticationException) {
                        // Something went wrong!
                    }

                    override fun onSuccess(profile: UserProfile) {
                        // We have the user's profile!
                        val email = profile.email.toString()
                        val name = profile.name
                        Log.i("email","g "+email)
                        sharedPreferences= this@LoginActivity.getSharedPreferences("3r",Context.MODE_PRIVATE)
                        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                        editor.putString("email",email)
                        editor.apply()
                        editor.commit()
                        progressBar.visibility= View.GONE


                        //check here if child exists in firebase
                        FirebaseDatabase.getInstance().getReference("Users").child(email.dropLast(4)).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            if(it.exists())
                            { Toast.makeText(this@LoginActivity,"Welcome Back",Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)}
                            else
                            {
                                Toast.makeText(this@LoginActivity,"Welcome",Toast.LENGTH_LONG).show()
                                val intent = Intent(this@LoginActivity, DetailActivity::class.java)
                                startActivity(intent)
                            }
                        }.addOnFailureListener{

                        }
                    }
                })
    }
    private fun logout() {
        WebAuthProvider.logout(account)
                .withScheme("demo")
                .start(this, object: Callback<Void?, AuthenticationException> {
                    override fun onSuccess(payload: Void?) {
                        Toast.makeText(this@LoginActivity,"Waiting for You Bye ",Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(error: AuthenticationException) {
                        // Something went wrong!
                    }
                })
    }

}