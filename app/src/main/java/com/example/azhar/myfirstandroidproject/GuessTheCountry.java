package com.example.azhar.myfirstandroidproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuessTheCountry extends AppCompatActivity {
    String country;
    Spinner countrySpinner;
    Button submit;
    ImageView img;
    ArrayList<String> countryNames = new ArrayList<>();
    TextView timer;
    boolean isTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_country);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        countrySpinner = findViewById(R.id.countrySpinner);
        submit = findViewById(R.id.submit);
        label = findViewById(R.id.answerLabel);
        correctContryLabel  = findViewById(R.id.correctCLabel);
         img = findViewById(R.id.cFlag);

        timer = findViewById(R.id.timer);
        isTimer = getIntent().getExtras().getBoolean("timer");

        if(savedInstanceState==null){
            country =  getRandomCountry();
        }
        System.out.println("country Code oncreate = " + finalCountryCode);






    }


    /**
     * This returns a random country String by fetching it from the json file
     * @return
     */
    public String getRandomCountry(){
        if(isTimer){
            runTimer();
        }

        submit.setText(R.string.SUBMIT);

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
            countryNames = new ArrayList<>();
            while (i.hasNext()){
                cList.add(i.next());
                String code = cList.get(cList.size()-1).toString();
                countryNames.add(jsonObj.getString(code));
            }
            Collections.sort(countryNames);

            populateSpinner(countryNames);

            int randomCountryIndex = (int)(Math.random() * cList.size());
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
     * This method populates the spinner element(view) with the existing countries from a list
     * @param countryNames
     */

    private void populateSpinner(List countryNames) {
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countryNames);
        countrySpinner.setAdapter(countryAdapter);
        setSelectedCountry();
    }
    String selectedCountry;
    public void setSelectedCountry() {
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedCountry = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * This method checks whether the answer is correct
     * when submit is pressed
     * @param v
     */
    public void checkAnswer(View v) {
        if(isTimer){
            countDownTimer.cancel();
        }
        if(submit.getText().toString().equalsIgnoreCase("next")){
            correctContryLabel.setText("");
            label.setText("");
            getRandomCountry();

        }else {
            if(selectedCountry.equalsIgnoreCase(country)){
                correctAnswer();
            }else{
                wrongAnswer();
            }

        }


    }



    TextView label;
    TextView correctContryLabel;
    private void wrongAnswer() {
        correctContryLabel.setText(country);
        correctContryLabel.setTextColor(Color.BLUE);
        label.setText(R.string.WRONG);
        label.setTextColor(Color.RED);
        submit.setText(R.string.NEXT);
    }

    int score;
    private void correctAnswer() {
        label.setText(R.string.CORRECT);
        label.setTextColor(Color.GREEN);
        submit.setText(R.string.NEXT);
        score++;
    }


    String finalCountryCode;



    public void setCountryFlag(String countryCode) {
        countryCode = countryCode.toLowerCase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        finalCountryCode = countryCode;



        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    ImageView img = findViewById(R.id.cFlag);
                    Bitmap bmp = null;
                    try {
                        URL url = new URL("https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/" + finalCountryCode + ".png");
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
     * This method overides the on save instance state where it explicitly saves
     * the ui data which was created before so when an even like change of orientation occurs
     * it can recreate the state where it was
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap b1 = ((BitmapDrawable) img.getDrawable()).getBitmap();
        outState.putParcelable("flag", b1);
        outState.putString("cCode",finalCountryCode);
        outState.putString("country",country);
        outState.putString("selectedCountry",selectedCountry);
        outState.putStringArrayList("countryList",countryNames);
        outState.putString("correctLabel",correctContryLabel.getText().toString());
        outState.putString("label",label.getText().toString());
        outState.putInt("labelColor",correctContryLabel.getCurrentTextColor());
        outState.putString("submit",submit.getText().toString());
        System.out.println("country Code onsave = " + finalCountryCode);

    }

    @Override
    /**
     * This method works with the onSaveInstanceState() method where the saved data will be restored
     * we can explicitly control which part of the activity that we should restore
     */
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        finalCountryCode = savedInstanceState.getString("cCode");
        img.setImageBitmap((Bitmap) savedInstanceState.getParcelable("flag"));
        country = (savedInstanceState.getString("country"));
        selectedCountry = savedInstanceState.getString("selectedCountry");
        countryNames = savedInstanceState.getStringArrayList("countryList");
        label.setText(savedInstanceState.getString("label"));
        correctContryLabel.setText(savedInstanceState.getString("correctL   bel"));
        submit.setText(savedInstanceState.getString("submit"));
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countryNames);
        countrySpinner.setAdapter(countryAdapter);
        System.out.println("country Code onrestore = " + finalCountryCode);
    }

    CountDownTimer countDownTimer;
    public void runTimer(){



        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("1sec");
                long millis= millisUntilFinished;
                String hms= String.format("%02d:%02d:%02d",

                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                );
                timer.setText(hms);
            }

            @Override
            public void onFinish() {
                timer.setText("00:00:00");
                if(submit.getText().toString().equalsIgnoreCase("submit")){
                    submit.callOnClick();
                }

            }
        }.start();
    }


}
