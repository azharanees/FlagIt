package com.example.azhar.myfirstandroidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class GuessHints extends AppCompatActivity {



    String country = "BRAZIL";
    private int failCounter = 0;
    private Button submit;
    private TextView label;
    private TextView correctLabel;
    boolean isTimer;
    TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_hints);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        label = findViewById(R.id.label);
        correctLabel = findViewById(R.id.correctLabel);
        submit = findViewById(R.id.submit);
        isTimer = getIntent().getExtras().getBoolean("timer");
        timer = findViewById(R.id.timer);
        //TODO : save the state and recall
        getRandomCountry();
        showBlankLetters(country);


    }


    /**
     * This method generates a random flag for each time it's called
     * @return
     */


    public String getRandomCountry(){

if(isTimer){
    runTimer();
}
        label.setText("");
        correctLabel.setText("");
        String json = null;
        try {
            InputStream is = getAssets().open("countries.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
         //   System.out.println(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject jsonObj = new JSONObject(json);
            Iterator i = jsonObj.keys();
            ArrayList cList = new ArrayList<>();
           while (i.hasNext()){
              cList.add(i.next());
           }
            int randomCountryIndex = (int)(Math.random() * cList.size() + 1);
             String cCode = cList.get(randomCountryIndex).toString();
            country =  jsonObj.getString(cCode);
            setCountryFlag(cCode);
            Log.d("COUNTRYLOG", country);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return country;
    }


    /**
     * This method sets the country flag of the imageview by downloading from the internet
     * and the downloading process takes place in the UI thread
     * @param countryCode
     */

    public void setCountryFlag(String countryCode){
        countryCode = countryCode.toLowerCase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String finalCountryCode = countryCode;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try  {
                    ImageView img = findViewById(R.id.cFlag);
                    Bitmap bmp = null;
                    try {
                        URL url = new URL("https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/"+ finalCountryCode +".png");
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });





    }




    /**
     * To show the length of the country name using dashes
     * @param country
     *
     *
     *
     */
    public void showBlankLetters(String country){
        LinearLayout layout = findViewById(R.id.layoutLetters);
        layout.removeAllViewsInLayout();
        failCounter = 0;

        for (int i = 0; i < country.length(); i++) {

            LinearLayout.LayoutParams paramsExample = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
            paramsExample.setMargins(2,0,2,0);
            TextView dash = new TextView(this);
            dash.setGravity(Gravity.CENTER);
            if(!((country.charAt(i) >= 'a' && country.charAt(i) <= 'z') || (country.charAt(i) >= 'A' && country.charAt(i) <= 'Z'))){

                dash.setText(Character.toString(country.charAt(i)));
            }else{
                dash.setText("_");
            }

            dash.setTextSize(40);
            dash.setLayoutParams(paramsExample);
            layout.addView(dash);

            Log.d("GAMELOG","Dash Added");

        }


    }
boolean fullGuessed;
    /**
     * Retrieving the letter input from the editText
     * @param v (button clicked)
     */
    public void introduceLetter(View v){



        if(submit.getText().toString().equalsIgnoreCase("next")){
            country = getRandomCountry();
            showBlankLetters(country);
            submit.setText(R.string.SUBMIT);
            runTimer();
        }else
        {

            EditText editTextletter = findViewById(R.id.editTextLetter);
            String letter = editTextletter.getText().toString();
            Log.d("GAMELOG", "The Letter entered is " + letter);
            editTextletter.setText("");

            if(letter.length()==1 && letter.matches("[a-zA-ZäöüßÄÖÜ]")){
                checkLetter(letter);

            }else {
                Toast.makeText(this,"Please Enter a Letter",Toast.LENGTH_SHORT).show();
            }
        }


    }

    int correctGuess= 0;
    /**
     * Checking if the input letter matches any letter in the county string
     * @param inputLetter
     */
    public void checkLetter(String inputLetter){

        inputLetter = inputLetter.toUpperCase();
        char charIn = inputLetter.charAt(0);
        boolean letterGuessed = false;


        for (int i = 0; i < country.length(); i++) {
            char charFromCountry = country.toUpperCase().charAt(i);
                Log.d("GAMELOG","checking for " + charFromCountry);
            if(charIn==charFromCountry){
                Log.d("GAMELOG","There was a match");
                showLetterAtIndex(i,charIn);
                letterGuessed=true;
            }else {
                Log.d("GAMELOG","There was NO match");

            }

        }
        if(letterGuessed==false) {
            letterFailed();
        }else {
            if(isGameOver()){
                correctAnswer();
            }
        }

    }

    /**
     * checks and returns a boolean if the game is over
     * @return
     */
    private boolean isGameOver() {
        LinearLayout l = findViewById(R.id.layoutLetters);


        for (int i = 0; i < l.getChildCount(); i++) {
            TextView text = (TextView) l.getChildAt(i);
            if(!text.getText().toString().equalsIgnoreCase("_")){
                fullGuessed=true;

            }else{
                fullGuessed = false;
                break;
            }
        }

        return fullGuessed;
    }

    /**
     * Handles wrong guesses
     */
    public void letterFailed(){
        failCounter++;
        if(isTimer){
            runTimer();
        }
        if(failCounter>2){
            Toast.makeText(this,"You have reached the guess limit",Toast.LENGTH_SHORT).show();
            submit.setText(R.string.NEXT);
            label.setText(R.string.WRONG);
            label.setTextColor(Color.RED);
            correctLabel.setTextColor(Color.BLUE);
            correctLabel.setText(country);
            failCounter = 0;
if(isTimer){
    countDownTimer.cancel();

}
        }else
            Toast.makeText(this,(3-failCounter)+" more guesses remaining",Toast.LENGTH_SHORT).show();




    }

    /**
     * Displays the guessed letter
     * @param position
     * @param guessedLetter
     */

    public void showLetterAtIndex(int position, char guessedLetter){

        LinearLayout layoutLetter = findViewById(R.id.layoutLetters);
        TextView letterHidden = (TextView) layoutLetter.getChildAt(position);
        letterHidden.setText(Character.toString(guessedLetter));




    }

    /**
     *
     */

    private void correctAnswer() {
       if(isTimer){
           countDownTimer.cancel();
       }

        label.setText(R.string.CORRECT);
        label.setTextColor(Color.GREEN);
        submit.setText(R.string.NEXT);
        correctGuess=0;

    }

    CountDownTimer countDownTimer;

    /**
     * Countdown timer logic is written in this method
     */
    public void runTimer() {



        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("1sec");
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d",

                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );
                timer.setText(hms);
            }

            @Override
            public void onFinish() {
                timer.setText("00:00:00");

                letterFailed();


            }
        }.start();
    }


    /**
     * Handles the android back button press event
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isTimer){
            countDownTimer.cancel();
        }
    }
}
