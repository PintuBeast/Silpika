package com.example.silpika_v2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class createVideoLesson extends AppCompatActivity {

    private static final int PICK_VIDEO=1;
    VideoView videoView;
    Button button;
    ProgressBar progressBar;
    EditText editText;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Member member;
    UploadTask uploadTask;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_video_lesson);
        videoView=findViewById(R.id.videoview_main);
        button=findViewById(R.id.btn_upload_main);
        editText=findViewById(R.id.video_name);
        member=new Member();
        progressBar=findViewById(R.id.progressbar_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid=user.getUid();


        Intent i=getIntent();

        String level=i.getStringExtra("levelName").toString();
        String art=i.getStringExtra("artName").toString();
        //String text=i.getStringExtra("text").toString();


        storageReference= FirebaseStorage.getInstance().getReference("Video/"+art+"/"+level);
        databaseReference= FirebaseDatabase.getInstance().getReference("Video/"+art+"/"+level);

        mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideo();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_VIDEO || resultCode==RESULT_OK ||
                data !=null || data.getData() !=null) {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
        }
    }

    public void ChooseVideo(View view) {



        Intent intent =new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO);
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void ShowVideo(View view) {

        Intent i=getIntent();
        String level=i.getStringExtra("levelName").toString();
        String art=i.getStringExtra("artName").toString();

        Intent intent=new Intent(createVideoLesson.this, VideoList.class);
       // intent.putExtra("id",k.id);
        intent.putExtra("artName",art);
        intent.putExtra("levelName",level);
        //intent.putExtra("text",k.text);
        startActivity(intent);
    }

    private void UploadVideo() {
        final String videoName=editText.getText().toString();
        final String search=editText.getText().toString().toLowerCase();

        FirebaseUser user = mAuth.getCurrentUser();
       String uid=user.getUid();

        if (videoUri !=null || !TextUtils.isEmpty(videoName)) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference= storageReference.child(System.currentTimeMillis()+"."+getExtension(videoUri));
            uploadTask=reference.putFile(videoUri);

            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {

                        throw task.getException();
                    }
                    return reference.getDownloadUrl();

                }
            })         .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {

                        Uri downloadUrl = task.getResult();
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(createVideoLesson.this,"Data Saved", Toast.LENGTH_LONG).show();

                        member.setName(videoName);
                        member.setVideoUrl(downloadUrl.toString());
                        member.setSearch(search);
                        member.setUid(uid);
                        String i=databaseReference.push().getKey();
                        databaseReference.child(i).setValue(member);

                    }  else
                        Toast.makeText(createVideoLesson.this,"Failed", Toast.LENGTH_LONG).show();


                }
            });


        } else
            Toast.makeText(this,"All fields are required", Toast.LENGTH_LONG).show();

    }
}