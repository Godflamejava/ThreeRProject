package com.example.threer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.threer.ml.ModelRecycled;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.content.ContentValues.TAG;

public class DetectingActivity extends AppCompatActivity {
    ImageView image;
    Interpreter interpreter;
    TextView category;
   TextView accuracy;
    Bitmap img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detecting);
        Button upload = findViewById(R.id.upload);
         image=findViewById(R.id.image);
      category =findViewById(R.id.category);
        accuracy=findViewById(R.id.accuracy);

        image.setVisibility(View.INVISIBLE);
        category.setVisibility(View.INVISIBLE);
        accuracy.setVisibility(View.INVISIBLE);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();


            }
        });

        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("newmodel", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                    @Override
                    public void onSuccess(CustomModel model) {
                        // Download complete. Depending on your app, you could enable the ML
                        // feature, or switch from the local model to the remote model, etc.

                        // The CustomModel object contains the local path of the model file,
                        // which you can use to instantiate a TensorFlow Lite interpreter.
                        File modelFile = model.getFile();
                        if (modelFile != null) {

                            interpreter = new Interpreter(modelFile);
                            Toast.makeText(DetectingActivity.this, "done", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DetectingActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Log.i("lovely","ritik"+data);
            if(requestCode==2){
            Uri uri = data.getData();
                mlFunction(uri,2);
                  image.setImageURI(uri);
                image.setVisibility(View.VISIBLE);
            }
else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                img=photo;
                mlFunction( null,1);

                image.setImageBitmap(photo);
                image.setVisibility(View.VISIBLE);
            } } }



public void mlFunction(Uri uri,int k){
    try {
        if(k==2)
        img=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);

        img=Bitmap.createScaledBitmap(img,250,250,true);

        try {
            ModelRecycled model = ModelRecycled.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 250, 250, 3}, DataType.FLOAT32);
            TensorImage tensorImage =new TensorImage(DataType.FLOAT32);
            tensorImage.load(img);
            ByteBuffer byteBuffer=tensorImage.getBuffer();
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelRecycled.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Releases model resources if no longer used.
            model.close();
            category.setVisibility(View.VISIBLE);
            accuracy.setVisibility(View.VISIBLE);
            Log.i("lovely",""+outputFeature0.getFloatArray().length);
//            category.setText(outputFeature0.getFloatArray()[0]+"");
//            accuracy.setText(outputFeature0.getFloatArray()[1]+"");

            float[]confidence =outputFeature0.getFloatArray();
            int maxPos=0;
            float maxconfidence=0;
            for(int i=0;i<confidence.length;i++){
if(confidence[i]>maxconfidence)
{maxconfidence=confidence[i];
maxPos=1;}
            }
            String[] categories ={"Cardboard", "Glass", "Metal", "Paper", "Plastic", "Trash"};

            if(maxPos==5) {
                category.setText("Non recyclable");
                accuracy.setText(""+maxconfidence);
            }
          else{
            category.setText(categories[maxPos]);
            accuracy.setText(""+maxconfidence);
          }



        } catch (IOException e) {
            // TODO Handle the exception
        }







    } catch (IOException e) {
        e.printStackTrace();
    }

}


}