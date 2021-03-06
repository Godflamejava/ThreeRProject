package com.example.threer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.a3rs.DetailClass;
import com.example.a3rs.ReuseItem;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.UUID;

public class AddReuseItem extends AppCompatActivity {
int PICK_IMAGE=0;
Uri imageUrl;
ImageView image;
ImageView cancel;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reuse_item);
        SharedPreferences sh = getSharedPreferences("3r", MODE_PRIVATE);
        String email = sh.getString("email", "");
        Button added_Items=findViewById(R.id.added_Items);
        ProgressBar pgbar =findViewById(R.id.pgbar);
        EditText itemName=findViewById(R.id.itemName);
        EditText itemDescription=findViewById(R.id.itemDescription);
        EditText itemMobileNumber=findViewById(R.id.itemMobileNumber);
        EditText itemPrice=findViewById(R.id.itemPrice);
        Button upload=findViewById(R.id.upload);
         image=findViewById(R.id.image);
        cancel=findViewById(R.id.cancel);
        Button add_Item=findViewById(R.id.add_Item);


        FirebaseDatabase.getInstance().getReference("Users").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DetailClass detailClass=snapshot.getValue(DetailClass.class);
                itemMobileNumber.setText(detailClass.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        added_Items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddReuseItem.this,AddedItemsActivity.class);
                FirebaseDatabase.getInstance().getReference("Users").child(email.substring(0, email.length() - 4)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DetailClass detailClass=snapshot.getValue(DetailClass.class);
                        String  name=detailClass.getPhone();
                      intent.putExtra("phone",name);
                      startActivity(intent);

                        }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });

        add_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgbar.setVisibility(View.VISIBLE);
                if(imageUrl!=null&&!itemName.getText().toString().equals("")&&!itemDescription.getText().toString().equals(null)&&!itemPrice.getText().toString().equals( null)&&!itemMobileNumber.getText().toString().equals(null))
                {
                        String uri="https://i.pinimg.com/originals/44/a3/3a/44a33a79654203b308704285704027fa.jpg";


//                    image.setDrawingCacheEnabled(true);
//                    image.buildDrawingCache();
//                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
//
//                   String uri= BitMapToString( bitmap);

                    ReuseItem item=new ReuseItem(itemName.getText().toString(),itemDescription.getText().toString(),itemPrice.getText().toString(),itemMobileNumber.getText().toString(),uri,"applied");
                    FirebaseDatabase.getInstance().getReference("RuseItems").child(String.valueOf(System.currentTimeMillis())).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pgbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddReuseItem.this, "Congrats it's uploaded", Toast.LENGTH_SHORT).show();

                        }
                    });


//                    final StorageReference filePath = FirebaseStorage.getInstance().getReference("images")
//                        .child(System.currentTimeMillis() + "." + "pdf");

//                    StorageTask uploadtask = filePath.putFile(imageUrl);
//                    uploadtask.continueWithTask(new Continuation() {
//                        @Override
//                        public Object then(@NonNull Task task) throws Exception {
//                            if (!task.isSuccessful()){
//
//                                throw task.getException();
//                            }
//
//                            return filePath.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            Uri downloadUri = task.getResult();
//                         String url = downloadUri.toString();
//                         ReuseItem item=new ReuseItem(itemName.getText().toString(),itemDescription.getText().toString(),itemPrice.getText().toString(),itemMobileNumber.getText().toString(),url,"applied");
//                            FirebaseDatabase.getInstance().getReference("RuseItems").child(email.substring(0, email.length() - 4)).setValue(item);
//
//                        }
//                    });
//


//                    image.setDrawingCacheEnabled(true);
//                    image.buildDrawingCache();
//                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] data = baos.toByteArray();
//
//
//                    UploadTask uploadTask = filePath.putBytes(data);
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            Log.i("ritik"," failed");
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Log.i("ritik","finally done");
//                        }
//                    });

                }
                else
                    Toast.makeText(AddReuseItem.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            }
        });




    cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        image.setVisibility(View.GONE);
        cancel.setVisibility(View.INVISIBLE);
        imageUrl=null;
    }
});

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUrl=data.getData();
            image.setImageURI(imageUrl);
            image.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            //TODO: action
        }
    }



    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("markit",temp);
        return temp;
    }


}



