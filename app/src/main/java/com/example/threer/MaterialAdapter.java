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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    ArrayList<Material> materialArrayList;
    Context context;
    public MaterialAdapter(Context context,ArrayList<Material> materialArrayList ){
        this.materialArrayList=materialArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.material_list, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Material material=materialArrayList.get(position);
        Picasso.get().load(material.image).into(holder.image);
        holder.name.setText(material.name.toUpperCase());
        holder.price.setText("Worth: "+material.price+" coins/kg");
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MaterialDisplayActivity.class);
//                intent.putExtra("name",material.name);
//                intent.putExtra("description",material.description);
//                intent.putExtra("image",material.image);
//                intent.putExtra("price",material.price);
                SharedPreferences sharedPreferences = context.getSharedPreferences("3r",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("name", material.name);
                myEdit.putString("description",material.description );
                myEdit.putString("image",material.image );
                myEdit.putString("price",material.price);
                myEdit.apply();

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return materialArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image,detail;
        TextView name,price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            detail=itemView.findViewById(R.id.detail);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);

        }
    }
}
