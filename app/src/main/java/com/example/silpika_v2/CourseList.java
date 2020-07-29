package com.example.silpika_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CourseList extends AppCompatActivity {

    TextView text1,text2;

    private Uri videoUri;
    private static final int REQUEST_CODE=101;
    private StorageReference videoRef;

    ListView lst;
    ArrayList<String> titles=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
   private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_course);

        text1=findViewById(R.id.t1);
        text2=findViewById(R.id.t2);


        Intent i=getIntent();
          String artTitle=i.getStringExtra("title").toString();
       // String text=i.getStringExtra("text").toString();

        text1.setText(artTitle);
       // text2.setText(text);






        //SQLiteDatabase db = getActivity().openOrCreateDatabase("SILPIKA", Context.MODE_PRIVATE, null);
        lst=findViewById(R.id.lst2);
        //Cursor c=db.rawQuery("select * from courses where course='Bharatnatyam'", null);
        //int id=c.getColumnIndex("id");
        //int title=c.getColumnIndex("title");
        //int text=c.getColumnIndex("text");

        titles.clear();
        arrayAdapter=new ArrayAdapter(CourseList.this,R.layout.support_simple_spinner_dropdown_item,titles);
        lst.setAdapter(arrayAdapter);
        final ArrayList<vb> obj1 =new ArrayList<vb>();
//if(c.moveToFirst()) {

        //do{
        //  Art 1


        vb k1=new vb();

        k1.id="0";
        k1.artName=artTitle;
        k1.levelName="Beginer";
        obj1.add(k1);
        titles.add(k1.levelName);


        //Art 2
        vb k1a=new vb();

        k1a.id="0";
        k1a.artName=artTitle;
        k1a.levelName="Intermediate";
        obj1.add(k1a);
        titles.add(k1a.levelName);

        //Art 3


        vb k1b=new vb();

        k1b.id="0";
        k1b.artName=artTitle;
        k1b.levelName="Advanced";
        obj1.add(k1b);
        titles.add(k1b.levelName);



        //}while(c.moveToNext());
        arrayAdapter.notifyDataSetChanged();
        lst.invalidateViews();


//}



        lst.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ee=titles.get(position).toString();
                vb k=obj1.get(position);

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();


             //   Intent i=new Intent(CourseList.this,createVideoLesson.class);

                currentUser.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() { // 1
                    @Override
                    public void onSuccess(GetTokenResult result) {


                       if (result.getClaims().get("admin") != null) {
                            boolean isAdmin = (boolean) result.getClaims().get("admin"); // 2
                            if (isAdmin) { // 3
                                // Show moderator UI
                             Intent i1=new Intent(CourseList.this,createVideoLesson.class);
                i1.putExtra("id",k.id);
                i1.putExtra("artName",k.artName);
                i1.putExtra("levelName",k.levelName);
                i1.putExtra("text",k.text);
                startActivity(i1);
                           } else {
                                // Show regular user UI.
                                Intent i2=new Intent(CourseList.this,VideoList.class);
                                i2.putExtra("id",k.id);
                                i2.putExtra("artName",k.artName);
                                i2.putExtra("levelName",k.levelName);
                                i2.putExtra("text",k.text);
                                startActivity(i2);
                            }
                        }
                        else { // Show regular user UI.
                             Intent i3=new Intent(CourseList.this,VideoList.class);
                           i3.putExtra("id",k.id);
                           i3.putExtra("artName",k.artName);
                           i3.putExtra("levelName",k.levelName);
                           i3.putExtra("text",k.text);
                           startActivity(i3);
                        }
                   }
                    });



            }



        });



    }


}









