package com.example.schoolproject.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity {

    TextView username, email, password, phone;
    Button logout, deleteAccount, save;
    Switch accountStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.ususr);
        email = (TextView) findViewById(R.id.maill);
        password = (TextView) findViewById(R.id.pswrdd);
        phone = (TextView) findViewById(R.id.phone);

        accountStatus = (Switch) findViewById(R.id.switcch);

        logout = (Button) findViewById(R.id.logout_btn);
        deleteAccount = (Button) findViewById(R.id.delete_btn);
        save = (Button) findViewById(R.id.update_btn);

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();

        username.setText(user.getUsername());
        email.setText(user.getEmail());


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                logoutUser();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserAccount();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updateUserData();
                PopUpClass popUpClass = new PopUpClass();
                popUpClass.showPopupWindow(view);
            }
        });
    }

    private void logoutUser() {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
    }

    private void updateUserData () { }

    private void deleteUserAccount () { }
}