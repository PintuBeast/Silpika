package com.example.silpika_v2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

public class viewStatsFragment extends Fragment {

    ListView lst;
    ArrayList<String> titles=new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_stats_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SQLiteDatabase db = getActivity().openOrCreateDatabase("SILPIKA", Context.MODE_PRIVATE, null);
        lst=view.findViewById(R.id.lst1);
        Cursor c=db.rawQuery("select * from courses where course='Kuchipudi'", null);
        int id=c.getColumnIndex("id");
        int title=c.getColumnIndex("title");
        int text=c.getColumnIndex("text");

titles.clear();
arrayAdapter=new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,titles);
lst.setAdapter(arrayAdapter);
final ArrayList<vb> vbb =new ArrayList<vb>();
if(c.moveToFirst()) {

  do{
      vb k=new vb();
      k.id=c.getString(id);
      k.title=c.getString(title);
      k.text=c.getString(text);

      vbb.add(k);

      titles.add(c.getString(title));



  }while(c.moveToNext());

  arrayAdapter.notifyDataSetChanged();
lst.invalidateViews();


}


lst.setOnItemClickListener(new AdapterView.OnItemClickListener(){
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ee=titles.get(position).toString();

        vb k=vbb.get(position);
       // Intent i=new Intent(getActivity().getApplicationContext(),createLesson.class);
      //  i.putExtra("id",k.id);
      //  i.putExtra("title",k.title);
      //  i.putExtra("text",k.text);
      //  startActivity(i);
    }



});




    }
}
