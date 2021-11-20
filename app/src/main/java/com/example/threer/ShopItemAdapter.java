package com.example.threer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
    ArrayList<ShopItems> shopItemsArrayList;
    Context context;
    public ShopItemAdapter(Context context,ArrayList<ShopItems> shopItemsArrayList){
        this.context=context;
        this.shopItemsArrayList=shopItemsArrayList;
    }

    @NonNull
    @Override
    public ShopItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.shopitemlayout, parent, false);
        return new ShopItemAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemAdapter.ViewHolder holder, int position) {
ShopItems shopItem = shopItemsArrayList.get(position);
holder.tvBrandName.setText(shopItem.getItemBrand());
holder.tvPrice.setText(shopItem.getPrice()+" coins");
holder.tvProductName.setText(shopItem.getItemNmae());
Picasso.get().load(shopItem.getImage()).into(holder.ivProduct);
if(shopItem.getRecycled().equals("true"))
    holder.recycled.setVisibility(View.VISIBLE);
else
    holder.recycled.setVisibility(View.INVISIBLE);


        SharedPreferences sh = context.getSharedPreferences("3r", MODE_PRIVATE);
        String email = sh.getString("email", "");
        holder.ivBookmark.setImageResource(R.drawable.bookmarkoutline);


        FirebaseDatabase.getInstance().getReference("Bookmarked").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ShopItems items=dataSnapshot.getValue(ShopItems.class);
                    if(items.getShopname().equals(shopItem.getShopname())) {
                        holder.ivBookmark.setImageResource(R.drawable.bookmarkfilled);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




     holder.card.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(context,ProductDescriptionActivity.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("3r",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("itemname",shopItem.getItemNmae());
        myEdit.putString("itemimage",shopItem.getImage());
        myEdit.putString("itemprice",shopItem.getPrice());
        myEdit.putString("itembrand",shopItem.getItemBrand());
        myEdit.putString("itemdescription",shopItem.getItemDescription());
        myEdit.putString("shopename",shopItem.getShopname());

        myEdit.putString("itemrecycled",shopItem.getRecycled());


        myEdit.putString("itembookmark","false");




        FirebaseDatabase.getInstance().getReference("Bookmarked").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ShopItems items=dataSnapshot.getValue(ShopItems.class);
                    if(items.getShopname().equals(shopItem.getShopname())) {
                        holder.ivBookmark.setImageResource(R.drawable.bookmarkfilled);
                        myEdit.putString("itembookmark","true");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        myEdit.apply();

        context.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {
        return shopItemsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct,ivBookmark,recycled;
        CardView card;
        TextView tvBrandName,tvProductName,tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBookmark=itemView.findViewById(R.id.ivBookmark);
            ivProduct=itemView.findViewById(R.id.ivProduct);
            card=itemView.findViewById(R.id.card);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvBrandName=itemView.findViewById(R.id.tvBrandName);
            recycled=itemView.findViewById(R.id.recycled);

        }
    }
}
