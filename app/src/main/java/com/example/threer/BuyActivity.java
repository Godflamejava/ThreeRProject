package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.example.a3rs.DetailClass;
import com.example.a3rs.ReuseItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class BuyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
        String name1 = sh.getString("itemname", "");
        String image1 = sh.getString("itemimage", "");
        String price1 = sh.getString("itemprice", "");


        ImageView image=findViewById(R.id.image);
        TextView name=findViewById(R.id.name);
        TextView price=findViewById(R.id.price);
        EditText etName = findViewById(R.id.etName);
        EditText etAddress = findViewById(R.id.etAddress);
        EditText etZipcode = findViewById(R.id.etZipcode);
        EditText etCity = findViewById(R.id.etCity);
        EditText etMobileNumber = findViewById(R.id.etMobileNumber);
        Button pay = findViewById(R.id.pay);


        name.setText(name1);

                        Picasso.get().load(image1).into(image);
                        price.setText(price1+" coins");
                        pay.setText("Pay "+price1 +" coins");





        String email = sh.getString("email", "example@com");

        FirebaseDatabase.getInstance().getReference("Users").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DetailClass detail=snapshot.getValue(DetailClass.class);
                etName.setText(detail.getName());
                etAddress.setText(detail.getAddress());
                etCity.setText(detail.getCity());
                etMobileNumber.setText(detail.getPhone());
                etZipcode.setText(detail.getZipcode());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          new SweetAlertDialog(BuyActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You wanna use "+price1+ " coins")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                     .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseDatabase.getInstance().getReference("Users").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        DetailClass detailClass=snapshot.getValue(DetailClass.class);
                                        Integer coin=Integer.parseInt(detailClass.getPrice());
                                        if(Integer.parseInt(detailClass.getPrice())<Integer.parseInt(price1))
                                        {
                                           sDialog.setTitleText("Sorry!")
                                                .setContentText("You Don't have enough coins")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                        }
                                        else
                                        {

                                            sDialog.setTitleText("Ordered")
                                                    .setContentText("Your Item has been Ordered")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();

                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                            ItemOrder itemOrder=new ItemOrder(email,etName.getText().toString(),etAddress.getText().toString(),etCity.getText().toString(),etZipcode.getText().toString(),etMobileNumber.getText().toString(),name1,price1);
FirebaseDatabase.getInstance().getReference("Order").child(email.substring(0, email.length() - 4)).setValue(itemOrder);
String newcoins=String.valueOf(Integer.parseInt(detailClass.getPrice())-Integer.parseInt(price1));
                                            FirebaseDatabase.getInstance().getReference("Users").child(email.substring(0, email.length() - 4)).child("price").setValue(newcoins);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .show();





            }
        });


        }
}