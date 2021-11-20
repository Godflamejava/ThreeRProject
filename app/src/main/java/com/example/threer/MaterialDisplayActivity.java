package com.example.threer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MaterialDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_display);
        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);

        String name = sh.getString("name", "");
        String description = sh.getString("description", "");
        String image= sh.getString("image", "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg");
        String price = sh.getString("price", "");



        TextView materialName=findViewById(R.id.materialName);
        TextView materialDescription=findViewById(R.id.materialDescription);
        ImageView materialImage=findViewById(R.id.materialImage);
        TextView materialPrice=findViewById(R.id.materialPrice);

        materialName.setText(name.toUpperCase());
        materialDescription.setText(description);
        Picasso.get().load(image).into(materialImage);
        materialPrice.setText("Worth "+price+" coins/kg");

    }
}