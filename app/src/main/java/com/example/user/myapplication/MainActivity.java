package com.example.user.myapplication;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
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


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "com.example.user.myapplication.LoginActivity";
    private static final String URL_FOR_LOGIN = "http://192.168.0.9/android_login_example/login.php";
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    Button btnlogin;
    Button btnLinkSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInputEmail = findViewById(R.id.login_input_email);
        loginInputPassword = findViewById(R.id.login_input_password);
        btnlogin = findViewById(R.id.btn_login);
        btnLinkSignup = findViewById(R.id.btn_link_signup);
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(loginInputEmail.getText().toString(),
                        loginInputPassword.getText().toString());
//                if(loginInputEmail.getText().toString().equals("admin")){
//                    if(loginInputPassword.getText().toString().equals("admin")){
//                        Intent intent = new Intent(
//                                MainActivity.this,
//                                UserActivity.class);
//                        startActivity(intent);
//                    }
//                }
            }
        });

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

            }
        });
    }

    private void loginUser( final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();

        Uri.Builder builder = Uri.parse(URL_FOR_LOGIN).buildUpon();
        builder.appendQueryParameter("email", email);
        builder.appendQueryParameter("password", password);
        String loginUrl=builder.build().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                loginUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("login response", "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        // Launch User activity
                        Intent intent = new Intent(
                                MainActivity.this,
                                UserActivity.class);
                        intent.putExtra("username", user);
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
                Log.e("login error", "Login Error: " + error.getMessage());
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
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

