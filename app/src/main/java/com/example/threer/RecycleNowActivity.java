package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a3rs.DetailClass;
import com.example.a3rs.RecycleOrder;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class RecycleNowActivity extends AppCompatActivity {
    String time="",date="";
    public static Button timepicker;
    public static Button datepicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_now);
        EditText etName = findViewById(R.id.etName);
        EditText etAddress = findViewById(R.id.etAddress);
        EditText etZipcode = findViewById(R.id.etZipcode);
        EditText etCity = findViewById(R.id.etCity);
        EditText etMobileNumber = findViewById(R.id.etMobileNumber);
         timepicker=findViewById(R.id.time);
         datepicker=findViewById(R.id.date);
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
                timepicker.setText("Time "+time);
            }
        });
datepicker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        showDatePickerDialog(view);

        datepicker.setText("Date "+ date);


    }
});


        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
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
        HashSet<String> recycleItemType=new HashSet<>();
        ChipGroup chipGroup = findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip=findViewById(checkedId);
                recycleItemType.add(chip.getText().toString());
            }
        });
        ArrayList<String> orderlist
                = new ArrayList<>(recycleItemType);

        Button placeOrder=findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date.equals("")||time.equals("")){
                    Toast.makeText(RecycleNowActivity.this, "Please choose a date for pick up", Toast.LENGTH_SHORT).show();
                }
                else {
                    date=datepicker.getText().toString();
                    time=timepicker.getText().toString();
                    RecycleOrder order = new RecycleOrder(email, etName.getText().toString(), etAddress.getText().toString(), etCity.getText().toString(), etZipcode.getText().toString(), etMobileNumber.getText().toString(), orderlist, date, time);
                    FirebaseDatabase.getInstance().getReference("RecycleOrders").child(String.valueOf(System.currentTimeMillis())).setValue(order);
                    Toast.makeText(RecycleNowActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etAddress.setText("");
                    etCity.setText("");
                    etMobileNumber.setText("");
                    etZipcode.setText("");
                    datepicker.setText("CHOOSE DATE");
                    timepicker.setText("CHOOSE TIME");

                }
            }
        });
        Button previous_request=findViewById(R.id.previous_requests);
        previous_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecycleNowActivity.this,PreviousRequestsActivity.class);
                startActivity(intent);
            }
        });
        ImageView back =findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        newFragment.onDetach();
        time=TimePickerFragment.hours+":"+TimePickerFragment.min;
        int i= v.getId();
        Button b= findViewById(i);
        b.setText(time);

    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        date=DatePickerFragment.days+"/"+DatePickerFragment.months+"/"+DatePickerFragment.years;
        int i= v.getId();
        Button b= findViewById(i);
        b.setText(date);
    }
}