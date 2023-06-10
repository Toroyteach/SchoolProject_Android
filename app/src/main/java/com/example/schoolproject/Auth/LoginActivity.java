package com.example.schoolproject.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.MainActivity;
import com.example.schoolproject.MenuActivity;
import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.utils.RequestHandler;
import com.example.schoolproject.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText usernameedittext, passwordedittext;
    Button loginbtn;
    TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        usernameedittext = (EditText) findViewById(R.id.editTextUsername);
        passwordedittext = (EditText) findViewById(R.id.editTextPassword);

        textViewRegister = (TextView) findViewById(R.id.textViewRegister);

        loginbtn = (Button) findViewById(R.id.buttonLogin);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                loginUser();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void loginUser() {
        final String username = usernameedittext.getText().toString().trim();
        final String password = passwordedittext.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(username)) {
            usernameedittext.setError("Please enter username");
            usernameedittext.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordedittext.setError("Enter a password");
            passwordedittext.requestFocus();
            return;
        }

        //if it passes all the validations

        class LoginUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        if(!obj.getString("message").equals("Login successfull")){
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("phone"),
                                userJson.getString("device_token"),
                                userJson.getString("status")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        LoginUser ru = new LoginUser();
        ru.execute();
    }
}