package com.example.azhar.myfirstandroidproject;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import static android.R.transition.fade;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_TIMEOUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Main-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                SplashActivity.this.finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
