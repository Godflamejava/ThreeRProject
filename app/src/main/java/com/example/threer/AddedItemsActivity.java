package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.a3rs.ReuseItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddedItemsActivity extends AppCompatActivity {
    ArrayList<ReuseItem> reuseItemArrayList;
    ReuseItemAdapter adapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_items);
        Intent intent = new Intent();
        name=intent.getStringExtra("phone");

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reuseItemArrayList=new ArrayList<>();
        adapter=new ReuseItemAdapter(this,reuseItemArrayList);
        recyclerView.setAdapter(adapter);
        getitemList();
    }

    public void getitemList(){
        FirebaseDatabase.getInstance().getReference("RuseItems").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ReuseItem item=dataSnapshot.getValue(ReuseItem.class);
                    Log.i("hello2",""+item);
                    if(item.getStatus().equals("applied")&&item.getMobileNumber().equals(name))
                        reuseItemArrayList.add(item);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}