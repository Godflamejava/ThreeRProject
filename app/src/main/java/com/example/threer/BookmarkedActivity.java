package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookmarkedActivity extends AppCompatActivity {
ArrayList<ShopItems> shopItemsArrayList;
ShopItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);

        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
        String email = sh.getString("email", "");


        RecyclerView recyclerView =findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        shopItemsArrayList=new ArrayList<>();
        adapter=new ShopItemAdapter(this,shopItemsArrayList);
        recyclerView.setAdapter(adapter);


        FirebaseDatabase.getInstance().getReference("Bookmarked").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ShopItems items=dataSnapshot.getValue(ShopItems.class);
                    shopItemsArrayList.add(items);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}