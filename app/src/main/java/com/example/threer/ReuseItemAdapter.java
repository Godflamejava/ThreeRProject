package com.example.threer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3rs.ReuseItem;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ReuseItemAdapter extends RecyclerView.Adapter<ReuseItemAdapter.ViewHolder> {
    ArrayList<ReuseItem> reuseItemArrayList;
    Context context;

    public ReuseItemAdapter(Context context,ArrayList<ReuseItem> reuseItemArrayList){
        this.context=context;
        this.reuseItemArrayList=reuseItemArrayList;
    }
    @NonNull
    @Override
    public ReuseItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reuse_item_layout, parent, false);
        return new ReuseItemAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ReuseItemAdapter.ViewHolder holder, int position) {
         ReuseItem item=reuseItemArrayList.get(position);
holder.itemName.setText(item.getName());
holder.itemDescription.setText(item.getDescription());
Picasso.get().load(item.getImage()).into(holder.itemImage);
holder.price.setText("Price:"+item.getPrice()+" coins");

holder.buy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context,BuyActivity.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences("3r",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
        myEdit.putString("itemname", item.getName());
        myEdit.putString("itemimage",item.getImage());
        myEdit.putString("itemprice",item.getPrice());

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
        myEdit.apply();

        intent.putExtra("name",item.getName());
        context.startActivity(intent);
    }
});
holder.connect.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Uri u = Uri.parse("tel:" + item.getMobileNumber());
        Intent i = new Intent(Intent.ACTION_DIAL, u);
       context.startActivity(i);

    }
});

    }

    @Override
    public int getItemCount() {
        return reuseItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName,itemDescription,price;
        ImageView itemImage,connect;
        Button buy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.itemName);
            price=itemView.findViewById(R.id.price);
            itemDescription=itemView.findViewById(R.id.itemDescription);
            itemImage=itemView.findViewById(R.id.itemImage);
            connect=itemView.findViewById(R.id.connect);
            buy=itemView.findViewById(R.id.buy);
        }
    }
}
