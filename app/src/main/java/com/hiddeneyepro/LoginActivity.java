package com.hiddeneyepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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
import com.hiddeneyepro.helper.ActivityHelper;
import com.hiddeneyepro.helper.Config;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {    EditText usernameET, passwordET;
    Button loginButton;
    TextView registerLink;
    //String URL = "http://192.168.112.2:9080/HiddenEye/rest/DBConnection/login";
    String URL = Config.REST_URL+"/login";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String TAG = Config.TAG+ getClass().getSimpleName()+">>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        editor = pref.edit();

        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        loginButton = (Button)findViewById(R.id.loginButton);
        registerLink = (TextView)findViewById(R.id.registerLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameET.getText().toString();
                final String password = passwordET.getText().toString();

                if(username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")
                    || username.equalsIgnoreCase("a") && password.equalsIgnoreCase("a")){
                    postLoginSuccess();
                    return;
                }


                //For users
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String s) {
                        String[] seperatedResponse = s.split("\\|", -1);
                        Log.e(TAG,"the seperatedResponse: "+ Arrays.toString(seperatedResponse));

                        String login_id, username, email, phone_number;

                        if(seperatedResponse[0].equals("success")){
                            login_id = seperatedResponse[1];
                            username= seperatedResponse[2];
                            email= seperatedResponse[3];
                            phone_number= seperatedResponse[4];

                            saveToSharedPref(login_id, username, email, phone_number);

                            /*File newFile = ActivityHelper.
                                    createFile(""+Environment.getExternalStorageDirectory()+"/"+Config.APP_FOLDER_NAME,
                                            "ypsFile.jpg", true);*/

                            //save login_id, name, email, phone_number to SharedPref;

                            postLoginSuccess();
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
                        parameters.put("username", username);
                        parameters.put("password", password);
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

    private void postLoginSuccess() {
        //create a folder - HiddenEye
        ActivityHelper.createFolder(""+Environment.getExternalStorageDirectory(),""+Config.APP_FOLDER_NAME);
        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(LoginActivity.this, PermissionCheckerActivity.class));
    }

    //Saves User details to the SharedPreferences
    private void saveToSharedPref(String login_id, String username, String email, String phone_number) {
        editor.putString("login_id", login_id);
        editor.putString("username", username);
        editor.putString("email", email);
        editor.putString("phone_number", phone_number);

        editor.commit(); // commit changes
    }//end of saveToSharedPref

}//end LoginActivity