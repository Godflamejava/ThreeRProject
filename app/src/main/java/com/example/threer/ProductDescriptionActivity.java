package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
        String itemname = sh.getString("itemname", "");
        String itemimage = sh.getString("itemimage", "");
        String itemprice = sh.getString("itemprice", "");
        String itembrand = sh.getString("itembrand", "");
        String itemrecycled=sh.getString("itemrecycled","");
        String itemdescription = sh.getString("itemdescription", "");
        String shopename=sh.getString("shopename","");
        final String[] itembookmark = {sh.getString("itembookmark", "")};


        ImageView ivProduct=findViewById(R.id.ivProduct);
        ImageView ivSaveProduct=findViewById(R.id.ivSaveProduct);
        TextView tvProductBrand=findViewById(R.id.tvProductBrand);
        TextView tvProductName=findViewById(R.id.tvProductName);
        TextView tvProductPrice=findViewById(R.id.tvProductPrice);
        Button btnBuyProduct=findViewById(R.id.btnBuyProduct);
        TextView tvDescription=findViewById(R.id.tvDescription);


        Picasso.get().load(itemimage).into(ivProduct);
        tvProductName.setText(itemname);
        tvProductBrand.setText(itembrand);
        tvProductPrice.setText(itemprice+" coins");
        tvDescription.setText(itemdescription);


        if(itembookmark[0].equals("true")) {

            ivSaveProduct.setImageResource(R.drawable.bookmarkfilled);
        }
        else {
            ivSaveProduct.setImageResource(R.drawable.bookmarkoutline);
        }

        ivSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
                String email = sh.getString("email", "");


                if(itembookmark[0].equals("true"))
                {
                    Log.i("babe",""+itembookmark[0]);
                    itembookmark[0] ="false";
                    ivSaveProduct.setImageResource(R.drawable.bookmarkoutline);
                    Log.i("babe",""+itembookmark[0]);

                    FirebaseDatabase.getInstance().getReference("Bookmarked").child(email.substring(0, email.length() - 4)).child(shopename).removeValue();
                }
                else{
                    Log.i("babe",""+itembookmark[0]);

                    itembookmark[0] ="true";
                   ShopItems items=new ShopItems(itemname ,itembrand,itemprice,itemdescription,itemimage,shopename,itemrecycled,"false");

                    ivSaveProduct.setImageResource(R.drawable.bookmarkfilled);
                    FirebaseDatabase.getInstance().getReference("Bookmarked").child(email.substring(0, email.length() - 4)).child(shopename).setValue(items);
                    Log.i("babe",""+itembookmark[0]);

                }

            }
        });
        btnBuyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductDescriptionActivity.this,BuyActivity.class);
                startActivity(intent);
            }
        });




    }
}