package com.example.reflex_projec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(startGame());
        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.black_logo);

    }

    private  View.OnClickListener startGame(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splashIntent = new Intent(MainActivity.this,GameActivity.class);
                startActivity(splashIntent);
            }
        };
    }


}