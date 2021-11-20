package com.example.threer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class Login2Activity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN =101 ;
    private TextView register,forgotPassword;
private EditText editTextEmail,editTextPassword,choice;
private Button signIn,googleSignIn,done,googleLogIn;
    GoogleSignInClient mGoogleSignInClient;
private FirebaseAuth mAuth;
private ProgressBar progressBar;
LinearLayout input,make;
Boolean work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // User is signed in
            Intent i = new Intent(Login2Activity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            setContentView(R.layout.activity_main);

input=(LinearLayout)findViewById(R.id.input);
make=(LinearLayout)findViewById(R.id.make);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn=(Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);
        googleLogIn=(Button)findViewById(R.id.googleloginin);
        googleLogIn.setOnClickListener(this);
googleSignIn=(Button) findViewById(R.id.googlesignin);
googleSignIn.setOnClickListener(this);

choice=findViewById(R.id.choice);
done=(Button)findViewById(R.id.done);
        editTextEmail=(EditText) findViewById(R.id.email);
        editTextPassword=(EditText) findViewById(R.id.password);
done.setOnClickListener(this);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        mAuth= FirebaseAuth.getInstance();
        forgotPassword=(TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
if(account!=null)
{
    done.setVisibility(View.GONE);
    googleLogIn.setVisibility(View.VISIBLE);
}

        }

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
            startActivity(new Intent(this,DetailActivity.class));
            break;
            case R.id.signIn:
                userLogin();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this,LoginActivity.class));
                break;

            case R.id.googlesignin:
                input.setVisibility(View.INVISIBLE);
                make.setVisibility(View.VISIBLE);
                break;
            case R.id.googleloginin:
                signIn();
                break;
            case R.id.done:
                input.setVisibility(View.VISIBLE);
                make.setVisibility(View.INVISIBLE);
                signIn();
                break;
        }

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.i("mary","ok");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if(account!=null)
                Log.i("mary",account.getIdToken());
            Toast.makeText(Login2Activity.this,"signed in", Toast.LENGTH_LONG).show();

firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(Login2Activity.this,"signing in failed", Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            input.setVisibility(View.VISIBLE);
                            make.setVisibility(View.INVISIBLE);

                            User user1 = new User();

                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(user.getUid())) {

                                    } else {
                                        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).setValue(user1);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            startActivity(new Intent(Login2Activity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login2Activity.this,"signing in failed", Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }


    private void userLogin() {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Password should be atleast 6 characters");
            editTextPassword.requestFocus();
            return;
        }
progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                // indirect
startActivity(new Intent(Login2Activity.this, MainActivity.class));
                  progressBar.setVisibility(View.GONE);
              }
              else{
                  Toast.makeText(Login2Activity.this,"Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                  progressBar.setVisibility(View.GONE);

              }


            }
        });

    }


}