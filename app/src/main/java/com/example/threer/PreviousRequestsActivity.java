package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.a3rs.RecycleOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreviousRequestsActivity extends AppCompatActivity {
ArrayList<RecycleOrder> recycleOrderArrayList;
RequestAdapter requestAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_requests);
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
        String email = sh.getString("email", "example@com");
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleOrderArrayList=new ArrayList<>();
        requestAdapter=new RequestAdapter(this,recycleOrderArrayList);
        recyclerView.setAdapter(requestAdapter);
        getOrderList(email);


    }

    public void getOrderList(String email){
        FirebaseDatabase.getInstance().getReference("RecycleOrders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    RecycleOrder order=dataSnapshot.getValue(RecycleOrder.class);
                    if(order.getEmail().equals(email))
                        recycleOrderArrayList.add(order);
                }
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}