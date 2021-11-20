package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a3rs.DetailClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
        String email = sh.getString("email", "");

        TextView name = findViewById(R.id.name);
        TextView ttemail=findViewById(R.id.email);
        TextView coins=findViewById(R.id.coins);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }




        FirebaseDatabase.getInstance().getReference("Users").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DetailClass detailClass=snapshot.getValue(DetailClass.class);
                name.setText(detailClass.getName());
                ttemail.setText(detailClass.getEmail());
                coins.setText(detailClass.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_recycle);
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }



    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_recycle:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new RecycleFragment()).commit();
                    return true;
                case R.id.navigation_reuse:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new Reuse()).commit();

                    return true;
                case R.id.navigation_reduce:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,new Reduce()).commit();
                    return true;
            }
            return false;
        }
    };
}