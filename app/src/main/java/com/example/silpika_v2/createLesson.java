package com.example.silpika_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class createLesson extends AppCompatActivity {

    TextView text1,text2;

    private Uri videoUri;
    private static final int REQUEST_CODE=101;
    private StorageReference videoRef;

    ListView lst;
    ArrayList<String> titles=new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_lesson);

        text1=findViewById(R.id.t1);
        text2=findViewById(R.id.t2);

        Intent i=getIntent();
          String courseTitle=i.getStringExtra("title").toString();
       // String text=i.getStringExtra("text").toString();

        text1.setText(courseTitle);
       // text2.setText(text);




        SQLiteDatabase db = openOrCreateDatabase("SILPIKA", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS courses(id INTEGER PRIMARY KEY AUTOINCREMENT,course VARCHAR,title VARCHAR,text VARCHAR)");
        lst=findViewById(R.id.lst2);
        Cursor c=db.rawQuery("select * from courses where course='"+ courseTitle+"'", null);
        int id=c.getColumnIndex("id");
        int title=c.getColumnIndex("title");
        int text=c.getColumnIndex("text");



        titles.clear();
        arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,titles);
        lst.setAdapter(arrayAdapter);
        final ArrayList<vb> obj1 =new ArrayList<vb>();

        if(c.moveToFirst()) {

            do{
                vb k=new vb();
                k.id=c.getString(id);
                k.title=c.getString(title);
                k.text=c.getString(text);

                obj1.add(k);

                titles.add(c.getString(title));



            }while(c.moveToNext());

            arrayAdapter.notifyDataSetChanged();
            lst.invalidateViews();


        }


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ee=titles.get(position).toString();

                vb k=obj1.get(position);
              // Intent i=new Intent(getApplicationContext(),c.class);
              //  i.putExtra("id",k.id);
              //  i.putExtra("title",k.title);
              //  i.putExtra("text",k.text);
              //  startActivity(i);
            }



        });









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
                    Toast.makeText(createLesson.this, "upload Failed"+exception.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Toast.makeText(createLesson.this, "upload complete",
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
            Toast.makeText(createLesson.this, "Nothing to upload",
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









