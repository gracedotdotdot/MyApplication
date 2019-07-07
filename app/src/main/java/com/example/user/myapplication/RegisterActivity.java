package com.example.user.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //private static final String TAG = "com.example.user.myapplication.RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "http://192.168.0.9/android_login_example/register.php";
    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputAge;
    Button btnSignUp;
    Button btnLinkLogin;
    private RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName =  findViewById(R.id.signup_input_name);
        signupInputEmail = findViewById(R.id.signup_input_email);
        signupInputPassword = findViewById(R.id.signup_input_password);
        signupInputAge = findViewById(R.id.signup_input_age);

        btnSignUp =  findViewById(R.id.btn_signup);
        btnLinkLogin = findViewById(R.id.btn_link_login);

        genderRadioGroup = findViewById(R.id.gender_radio_group);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void submitForm() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        if(selectedId == R.id.female_radio_btn)
            gender = "Female";
        else
            gender = "Male";

        registerUser(signupInputName.getText().toString(),
                signupInputEmail.getText().toString(),
                signupInputPassword.getText().toString(),
                gender,
                signupInputAge.getText().toString());
    }

    private void registerUser(final String name,  final String email, final String password,
                              final String gender, final String dob) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        Uri.Builder builder = Uri.parse(URL_FOR_REGISTRATION).buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("name", name);
        builder.appendQueryParameter("password", password);
        builder.appendQueryParameter("gender", gender);
        builder.appendQueryParameter("age", dob);
        String regUrl=builder.build().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                regUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("reg response", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject("{"+response+"}");
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Hi " + user +", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                if(error instanceof TimeoutError){
                    Log.e("error", "TimeOutError");
                    error.printStackTrace();
                }
                else if (error instanceof AuthFailureError) {
                    Log.e("error", "AuthFailureError"); error.printStackTrace();
                } else if (error instanceof ServerError) {
                    Log.e("error", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("error", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("error", "ParseError");
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Log.e(" no error", "send params");
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                params.put("age", dob);
                Log.e("request send data", params.toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {
                Log.e("Json Error Response", error.toString());
                error.printStackTrace();
            }
        });

        // Adding request to request queue
        //CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_FOR_REGISTRATION, params, this.createRequestSuccessListener(), this.createRequestErrorListener());
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}