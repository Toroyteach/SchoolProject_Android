package com.example.schoolproject.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.utils.URLs;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    Button updateProfileBtn;
    Button cancelProfileBtn;
    Button updatePasswordBtn;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    EditText phoneEditText;
    EditText emailEditText;
    SwitchMaterial status;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        setTitle("Update Profile Details");
        progressBar = (ProgressBar) findViewById(R.id.progressBarUpdate);

        updateProfileBtn = (Button) findViewById(R.id.buttonSubmit);
        updatePasswordBtn = (Button) findViewById(R.id.buttonUpdatePassword);
        cancelProfileBtn = (Button) findViewById(R.id.buttonCancel);

        usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        confirmPasswordEditText = (EditText) findViewById(R.id.editTextConfirmPassword);
        phoneEditText = (EditText) findViewById(R.id.editTextPhone);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        getUserProfileData();

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                submitDetails();
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                submitPasswordDetails();
            }
        });

        cancelProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getUserProfileData() {

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();

        usernameEditText.setText( user.getUsername());
        phoneEditText.setText( user.getPhone());
        emailEditText.setText( user.getEmail());

    }

    public void submitDetails() {

        boolean isSuccess = false;
        String username = usernameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (username.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //Add a regular expressions to check the number format
        if (phone.length() < 12) {
            // add a regula expressoin check
            Toast.makeText(this, "Phone Number must be 12 Digits 254...", Toast.LENGTH_SHORT).show();
            return;
        }

        // If all checks pass, concatenate the response and display a toast message
        postDataUsingVolley(username, phone, email);
    }

    public void submitPasswordDetails() {

        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // If all checks pass, concatenate the response and display a toast message
        postPasswordDataUsingVolley(password);
    }

    private void postDataUsingVolley(String username, String phone, String email) {
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        int id = user.getId();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_USER_DATA + "?id=" + id, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                progressBar.setVisibility(View.GONE);

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String message = respObj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


                    JSONObject userObject = respObj.getJSONObject("user");
                    String username = userObject.getString("username");
                    String deviceToken = userObject.getString("device_token");
                    String phone = userObject.getString("phone");
                    String email = userObject.getString("email");
                    String status = userObject.getString("status");

                    SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
                    User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();

                    user.setPhone(phone);
                    user.setEmail(email);
                    user.setUsername(username);

                    sharedPrefManager.updateUserProfile(user);


                    getUserProfileData();

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("username", username);
                params.put("email", email);
                params.put("phone", phone);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

    private void postPasswordDataUsingVolley(String password) {
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        User user =  SharedPrefManager.getInstance(getApplicationContext()).getUser();
        int id = user.getId();

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_UPDATE_USER_PASSWORD, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                progressBar.setVisibility(View.GONE);

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String message = respObj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    finish();
                    //Call the method to refresh the list of custom alerts

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id", String.valueOf(id));
                params.put("password", password);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

}