package com.example.uploadimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.uploadimage.databinding.ActivityMainBinding;
//import com.google.firebase.storage.FirebaseStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Uri imageUri=null;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog dialog;
    String file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("IMAGES");

        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");


        binding.selectButton.setOnClickListener(view1 -> selectImage());

        binding.uploadButton.setOnClickListener(view1-> uploadImage() );


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss",Locale.CANADA);
        Long value = System.currentTimeMillis();
        Date now = new Date();
        file_name = simpleDateFormat.format(value);
        Log.d("CURRENT_DATE",""+file_name);

    }

    private void uploadImage() {


        try {
            if(imageUri!=null)
            {
                dialog.show();
                dialog.setMessage("Image is uploading");

//                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

                storageReference.child(file_name).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"UPLOADED!",Toast.LENGTH_LONG).show();
                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });
            }
            else
            {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,777);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 777 && data.getData()!=null)
        {
            imageUri = data.getData();
            binding.uploadImageView.setImageURI(imageUri);
        }


    }
}