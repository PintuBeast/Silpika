package com.example.silpika_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class courseActivity extends AppCompatActivity {
EditText ed1, ed2;
Spinner spinner;
Button b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        spinner=findViewById(R.id.courseName);
        ed1=findViewById(R.id.courseTitle);
        ed2=findViewById(R.id.courseText);
        b1=findViewById(R.id.btn_courseCreate);
        b2=findViewById(R.id.btn_courseCancel);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insert();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });


    }

    public void insert() {

        try {
            String course = spinner.getSelectedItem().toString();
            String title = ed1.getText().toString();
            String text = ed2.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("SILPIKA", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS courses(id INTEGER PRIMARY KEY AUTOINCREMENT,course VARCHAR,title VARCHAR,text VARCHAR)");
            String sql = "insert into courses(course,title,text) values(?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, course);
            statement.bindString(2, title);
            statement.bindString(3, text);
            statement.execute();
            Toast.makeText(this, "Record Added", Toast.LENGTH_LONG).show();

            ed1.setText("");
            ed2.setText("");


        } catch (Exception ex) {

        }

    }
}