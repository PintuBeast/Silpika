package com.example.silpika_v2;

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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lesson);

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
                Intent i=new Intent(CourseList.this,createVideoLesson.class);
                i.putExtra("id",k.id);
                i.putExtra("title",k.title);
                i.putExtra("text",k.text);
                startActivity(i);
            }



        });



    }


}









