package com.example.silpika_v2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class InstructorMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructor_main);
        Toolbar toolbar = findViewById(R.id.toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //mAppBarConfiguration = new AppBarConfiguration.Builder(
        //        R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
        //        R.id.nav_tools, R.id.nav_share, R.id.nav_send)
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //inserted code start
        navController.addOnDestinationChangedListener(new
           NavController.OnDestinationChangedListener() {
               @Override
               public void onDestinationChanged(@NonNull NavController controller,
                   @NonNull
                    NavDestination destination, @Nullable Bundle arguments) {
// fragment =null;

                   if (destination.getId() == R.id.nav_home) {
                       //Toast.makeText(MainActivity.this, "home", Toast.LENGTH_LONG).show();
                   }
                   if (destination.getId() == R.id.nav_gallery) {
                     //  Toast.makeText(MainActivity.this, "nav_gallery",
                       //        Toast.LENGTH_LONG).show();
                   }
                   if (destination.getId() == R.id.nav_slideshow) {
                     //  Toast.makeText(MainActivity.this, "nav_slideshow",
                     //          Toast.LENGTH_LONG).show();
                   }
                  // if (destination.getId() == R.id.nav_tools) {
                     //  fragment=new instructor_login();
                     //  Toast.makeText(MainActivity.this, "nav_tools",
                       //        Toast.LENGTH_LONG).show();
                  // }
                  // if (destination.getId() == R.id.nav_share) {
                       //Toast.makeText(MainActivity.this, "nav_share",Toast.LENGTH_LONG).show();
                 //  }
                 //  if (destination.getId() == R.id.nav_send) {
                     //  Toast.makeText(MainActivity.this, "nav_send",
                      //         Toast.LENGTH_LONG).show();
                 //  }

                 //   if(fragment !=null) {
                   //     FragmentManager fragmentManager =getSupportFragmentManager();
                     //   FragmentTransaction ft= fragmentManager.beginTransaction();

                       // ft.replace(R.id.area,fragment);
                       // ft.commit();

                  //  }

               }
           });

        //inserted code end
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
