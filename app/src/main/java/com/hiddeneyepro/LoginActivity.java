package com.hiddeneyepro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {    EditText usernameBox, passwordBox;
    Button loginButton;
    TextView registerLink;
    String URL = "http://192.168.112.2:9080/HiddenEye/rest/DBConnection/login";

    String SHARED_PREF_NAME = "HiddenEyePref";
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, 0);
        editor = pref.edit();


        usernameBox = (EditText)findViewById(R.id.usernameBox);
        passwordBox = (EditText)findViewById(R.id.passwordBox);
        loginButton = (Button)findViewById(R.id.loginButton);
        registerLink = (TextView)findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Admin Privileges
                /*if(usernameBox.getText().toString().equalsIgnoreCase("adm")
                        && passwordBox.getText().toString().equalsIgnoreCase("adm")){

                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    return;
                }*/

                //For users
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        String[] seperatedResponse = s.split("\\|", -1);
                        Log.e(">>","the seperatedResponse: "+ Arrays.toString(seperatedResponse));

                        String login_id, name, email, phone_number;

                        if(seperatedResponse[0].equals("success")){
                            login_id = seperatedResponse[1];
                            name= seperatedResponse[2];
                            email= seperatedResponse[3];
                            phone_number= seperatedResponse[4];

                            //create a folder - HiddenEye
                            FileIO.createFolder("HiddenEye");

                            //save login_id, name, email, phone_number to SharedPref;
                            saveToSharedPref(login_id, name, email, phone_number);

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect Login Details", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this, "Some volleyError occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("username", usernameBox.getText().toString());
                        parameters.put("password", passwordBox.getText().toString());
                        return parameters;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                rQueue.add(request);
            }
        });

        //If user is Registering
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    //Saves User details to the SharedPreferences
    private void saveToSharedPref(String login_id, String name, String email, String phone_number) {
        editor.putString("login_id", login_id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("phone_number", phone_number);

        editor.commit(); // commit changes
    }//end of saveToSharedPref

    //Remove removeSharedPref
    public void removeSharedPref(){
        editor.clear();
    }//end removeSharedPref

}//end LoginActivity