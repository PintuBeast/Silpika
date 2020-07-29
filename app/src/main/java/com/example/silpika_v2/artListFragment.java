package com.example.silpika_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class artListFragment extends Fragment {

    ListView lst;
    ArrayList<String> titles=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    FloatingActionButton btnCreate;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.art_list_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       btnCreate=view.findViewById(R.id.create);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),createCourse.class);
                startActivity(i);

            }
        });






        //SQLiteDatabase db = getActivity().openOrCreateDatabase("SILPIKA", Context.MODE_PRIVATE, null);
        lst=view.findViewById(R.id.lst1);
        //Cursor c=db.rawQuery("select * from courses where course='Bharatnatyam'", null);
        //int id=c.getColumnIndex("id");
        //int title=c.getColumnIndex("title");
        //int text=c.getColumnIndex("text");

titles.clear();
arrayAdapter=new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,titles);
lst.setAdapter(arrayAdapter);
final ArrayList<vb> obj1 =new ArrayList<vb>();
//if(c.moveToFirst()) {

  //do{
        //  Art 1

      art k=new art();
      vb k1=new vb();
        k.id="0";
        k.artName ="Kathak";
        k.courseCount="5";
        k.imageUri=" ";

        k1.id="0";
        k1.artName="Kathak";


        obj1.add(k1);

        titles.add(k.artName);


        //Art 2
        vb k1a=new vb();
        k.id="1";
        k.artName="Kuchipudi";
        k.courseCount="5";
        k.imageUri=" ";



        k1a.id="1";
        k1a.artName="Kuchipudi";


        obj1.add(k1a);
        titles.add( k.artName);

        //Art 3

        k.id="2";
        k.artName="BharatNatyam";
        k.courseCount="5";
        k.imageUri=" ";

        vb k1b=new vb();
        k1b.id="2";
        k1b.artName="BharatNatyam";
        obj1.add(k1b);

        titles.add( k.artName);

        //Art4

        k.id="3";
        k.artName="Odissi";
        k.courseCount="5";
        k.imageUri=" ";

        vb k1c=new vb();
        k1c.id="3";
        k1c.artName="Odissi";
        obj1.add(k1c);

        titles.add( k.artName);

        //Art5

        k.id="4";
        k.artName="Kathakali";
        k.courseCount="5";
        k.imageUri=" ";

        vb k1d=new vb();
        k1d.id="4";
        k1d.artName="Kathakali";
        obj1.add(k1d);

        titles.add( k.artName);

        //Art6

        k.id="5";
        k.artName="Sattriya";
        k.courseCount="5";
        k.imageUri=" ";

        vb k1e=new vb();
        k1e.id="5";
        k1e.artName="Sattriya";
        obj1.add(k1e);

        titles.add( k.artName);



        //Art7

        k.id="6";
        k.artName="Manipuri";
        k.courseCount="5";
        k.imageUri=" ";

        vb k1f=new vb();
        k1f.id="6";
        k1f.artName="Manipuri";
        obj1.add(k1f);

        titles.add( k.artName);

        //Art8

        k.id="7";
        k.artName="Mohiniyattam";
        k.courseCount="5";
        k.imageUri=" ";

        vb k1g=new vb();
        k1g.id="7";
        k1g.artName="Mohiniyattam";

        obj1.add(k1g);
        titles.add( k.artName);



        //}while(c.moveToNext());
        arrayAdapter.notifyDataSetChanged();
lst.invalidateViews();


//}


lst.setOnItemClickListener(new AdapterView.OnItemClickListener(){
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String ee=titles.get(position).toString();

       vb k2=obj1.get(position);
       Intent i=new Intent(getActivity().getApplicationContext(),CourseList.class);
      i.putExtra("id",k2.id);
        i.putExtra("title",k2.artName);
       // i.putExtra("text",k1.text);
        startActivity(i);
    }

});


    }

}
