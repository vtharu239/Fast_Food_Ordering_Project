package com.example.project_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.project_food.R;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(6000);
                    startActivity(new Intent(SplashActivity.this,NavigationActivity.class));
                    finish();
                }catch(Exception e){

                }
            }
        };thread.start();
    }
}