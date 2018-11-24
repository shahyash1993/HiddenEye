package com.hiddeneyepro;

import android.app.Activity;
import android.content.Intent;
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
import com.hiddeneyepro.helper.Config;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    EditText usernameET, passwordET, emailET, phoneNumberET;
    //nameBox
    Button registerButton;
    TextView loginLink;
    String URL = Config.REST_URL+"/register";
    String TAG = Config.TAG+ getClass().getSimpleName()+">>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        nameBox = (EditText)findViewById(R.id.nameBox);
        emailET = (EditText)findViewById(R.id.emailET);
        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        phoneNumberET = (EditText)findViewById(R.id.phoneNumberET);

        registerButton = (Button)findViewById(R.id.registerButton);
        loginLink = (TextView)findViewById(R.id.loginLink);
        Log.e(TAG,"onCreate occurred!");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"registerButton Clicked!");
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("true")){
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Can't Register", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegisterActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("username", usernameET.getText().toString());
                        parameters.put("password", passwordET.getText().toString());
                        //parameters.put("name", nameBox.getText().toString());
                        parameters.put("email", emailET.getText().toString());
                        parameters.put("phoneNumber", phoneNumberET.getText().toString());

                        Log.e(TAG, "Sending: name, email, un, pwd, phNum:"
                                //+nameBox.getText().toString()
                                +emailET.getText().toString()
                                +usernameET.getText().toString()
                                +passwordET.getText().toString()
                                +phoneNumberET.getText().toString()
                        );

                        return parameters;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
                rQueue.add(request);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
