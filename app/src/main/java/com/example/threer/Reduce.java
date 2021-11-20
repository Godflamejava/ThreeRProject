package com.example.threer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reduce#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reduce extends Fragment {
    ArrayList<ShopItems> shopItemsArrayList;
    ShopItemAdapter adapter;
    ProgressBar pgbar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reduce() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Reduce newInstance(String param1, String param2) {
        Reduce fragment = new Reduce();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank2, container, false);
        RecyclerView recyclerView =view.findViewById(R.id.recycleView);
        pgbar =view.findViewById(R.id.pgbar);
        pgbar.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        shopItemsArrayList=new ArrayList<>();
        adapter=new ShopItemAdapter(getContext(),shopItemsArrayList);
        recyclerView.setAdapter(adapter);
        Button bookmarked= view.findViewById(R.id.bookmarked);
        bookmarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BookmarkedActivity.class);
                startActivity(intent);
            }
        });
        getItemList();
        return view;

    }
   public void getItemList(){
       FirebaseDatabase.getInstance().getReference("ShopItems").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot:snapshot.getChildren())
               {
                   Log.i("love",""+dataSnapshot);
                   ShopItems items = dataSnapshot.getValue(ShopItems.class);
                   shopItemsArrayList.add(items);
               }
               adapter.notifyDataSetChanged();
               pgbar.setVisibility(View.GONE);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }

}