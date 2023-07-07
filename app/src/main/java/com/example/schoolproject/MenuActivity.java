package com.example.schoolproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.schoolproject.Auth.User;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.charts.ChartActivity;
import com.example.schoolproject.ui.cropsDistribution.CropsDistributionActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolproject.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    private TextView txtusername, txtuseremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_news, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_notification, R.id.nav_feedback)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getUserProfileData();

    }

    private void getUserProfileData() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        txtuseremail = (TextView) headerView.findViewById(R.id.textView_useremail);
        txtusername = (TextView) headerView.findViewById(R.id.textview_username);
        txtuseremail.setText( user.getEmail());
        txtusername.setText( user.getUsername());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.nav_manage_farm);
        MenuItem menuItem2 = menu.findItem(R.id.nav_dashboard);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the click event
                int itemId = item.getItemId();

                // Check the item id to determine which activity to open
                if (itemId == R.id.nav_manage_farm) {
                    // Open the desired activity
                    Intent intent = new Intent(MenuActivity.this, CropsDistributionActivity.class);
                    startActivity(intent);
                    return true;
                }

                // Return false if the item id doesn't match any specific action
                return false;
            }
        });

        menuItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the click event
                int itemId = item.getItemId();

                if (itemId == R.id.nav_dashboard) {
                    // Open the desired activity
                    Intent intent = new Intent(MenuActivity.this, ChartActivity.class);
                    startActivity(intent);
                    return true;
                }

                // Return false if the item id doesn't match any specific action
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_manage) {
//            // Handle click on "Open Activity" menu item
//            System.out.println("here");
//            Intent intent = new Intent(this, CropsDistributionActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}