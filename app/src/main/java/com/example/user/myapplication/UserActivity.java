package com.example.user.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class UserActivity extends AppCompatActivity {

    private ImageButton mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mlist = findViewById(R.id.btnlist);
        mlist.setOnClickListener(mlistOnClick);
    }
    private View.OnClickListener mlistOnClick = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent(UserActivity.this, temperature.class);
            startActivity(intent);
        }
    };

    //ParseInstallation.getCurrentInstallation().saveInBackground();


}