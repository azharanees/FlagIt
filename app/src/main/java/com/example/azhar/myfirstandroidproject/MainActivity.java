package com.example.azhar.myfirstandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {


    Switch timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = findViewById(R.id.timerSwitch);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        Button b1 = findViewById(R.id.guessFlag);
        Button b2 = findViewById(R.id.guessCountry);
        Button b3 = findViewById(R.id.advancedLevel);
        Button b4 = findViewById(R.id.guessHints);

        b1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                v.startAnimation(animScale);
                startGuessTheFlag(v);
            }
        });

        b2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                startGuessTheCountry(v);
            }
        });   b3.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                startAdvancedLevel(v);
            }
        });
        b4.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                startGuessHints(v);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGuessHints(View v){
        Intent guessHintsIntent = new Intent(this, GuessHints.class);
        guessHintsIntent.putExtra("timer",timer.isChecked());
        startActivity(guessHintsIntent);
        //overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_out_right);

    }
    public void startGuessTheCountry(View v){
            Intent guessTheCountryIntent = new Intent(this,GuessTheCountry.class);
            guessTheCountryIntent.putExtra("timer",timer.isChecked());
            startActivity(guessTheCountryIntent);
    }
    public void startGuessTheFlag(View v){
        Intent guessTheGlagIntent = new Intent(this,GuessTheFlag.class);
        guessTheGlagIntent.putExtra("timer",timer.isChecked());
        startActivity(guessTheGlagIntent);
    }
  public void startAdvancedLevel(View v){
        Intent advancedLevel = new Intent(this,AdvancedLevel.class);
      advancedLevel.putExtra("timer",timer.isChecked());
        startActivity(advancedLevel);
    }

}
