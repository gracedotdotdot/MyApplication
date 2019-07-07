package com.example.user.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class temperature extends AppCompatActivity {

    private ImageButton mup, mdown;
    private TextView mdesiredTempView;
    private int mdesiredTemp;
    int mcurrentTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        mup = findViewById(R.id.up);
        mdown= findViewById(R.id.down);
        mdesiredTempView = findViewById(R.id.desired_temp_num);

        View.OnClickListener btnUpOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdesiredTemp = Integer.parseInt(mdesiredTempView.getText().toString()) ;
                mdesiredTemp = mdesiredTemp+1;
                mdesiredTempView.setText(Integer.toString(mdesiredTemp));
            }
        };
        View.OnClickListener btnDownOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdesiredTemp = Integer.parseInt(mdesiredTempView.getText().toString());
                mdesiredTemp = mdesiredTemp-1;
                mdesiredTempView.setText(Integer.toString(mdesiredTemp));
            }
        };

        mup.setOnClickListener(btnUpOnClick);
        mdown.setOnClickListener(btnDownOnClick);
    }
}
