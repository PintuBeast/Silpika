package com.example.silpika_v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class signOutFragment extends Fragment {

    ListView lst;
    ArrayList<String> titles=new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_out_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

  Alert();

    }




    public void Alert() {

      //  FirebaseAuth.getInstance().signOut();        // logout here

      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to Sign out ?");
        builder.setIcon(R.drawable.small_logo);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
        FirebaseAuth.getInstance().signOut();

                Intent i=new Intent(getActivity(),signIn.class);
                startActivity(i);


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
       AlertDialog alert = builder.create();
        alert.show();





       // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
      //  {
      //      @Override
      //      public void onClick(DialogInterface dialog, int which)
      //      {
                // Stuff to do

     //           FirebaseAuth.getInstance().signOut();
     //       }
     //   });
    //    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
    //    {
    //        @Override
    //        public void onClick(DialogInterface dialog, int which)
    //        {
    //            // Stuff to do
   //         }
   //     });

   //     builder.setMessage("Your_MSG");
   //     builder.setTitle("Warning..");

//        AlertDialog d = builder.create();
//        d.show();







        }


}
