package com.example.silpika_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class instructor_login extends AppCompatActivity {
private Uri videoUri;
private static final int REQUEST_CODE=101;
private StorageReference videoRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_main);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        videoRef = storageRef.child("/videos/" + uid + "/userIntro.3gp");

    }
    public void upload(View view) {
        if(videoUri!=null) {
            UploadTask uploadTask = videoRef.putFile(videoUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {



                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(instructor_login.this, "upload Failed"+exception.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Toast.makeText(instructor_login.this, "upload complete",
                            Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            updateProgress(taskSnapshot);
                        }
                    });

        }

        else{
            Toast.makeText(instructor_login.this, "Nothing to upload",
                    Toast.LENGTH_LONG).show();
        }


        }

       public void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {

    long filesize=taskSnapshot.getTotalByteCount();
    long uploadBytes=taskSnapshot.getBytesTransferred();
    long progress=100*uploadBytes/filesize;
           ProgressBar progressBar=(ProgressBar) findViewById(R.id.pbar);
           progressBar.setProgress( (int) progress );
       }


       public void record (View view) {
        Intent i=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(i, REQUEST_CODE);
       }


      // public void download (View view) {

       // try{
//            final File localFile= File.createTempFile("userIntro", "3gp");

  //          videoRef.getFile(localFile).addOnSuccessListener(


    //        )



    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        videoUri=data.getData();
        if(requestCode==REQUEST_CODE) {

            if (resultCode==RESULT_OK) {
                Toast.makeText(this, "Video saved to \n" +
                           videoUri, Toast.LENGTH_LONG).show();
            } else if (resultCode==RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled",
                        Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }


        }


    }


}

