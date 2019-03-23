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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class GuessTheFlag extends AppCompatActivity {


    String country;
    ImageButton f1;
    ImageButton f2;
    ImageButton f3;
    TextView countryNameLabel;
    TextView answer;
    boolean isTimer;
    TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_flag);


        next = findViewById(R.id.next);
        countryNameLabel = findViewById(R.id.countryName);
        answer = findViewById(R.id.answer);
        f1  = findViewById(R.id.flag1);
        f2  = findViewById(R.id.flag2);
        f3  = findViewById(R.id.flag3);
        isTimer = getIntent().getExtras().getBoolean("timer");
        timer = findViewById(R.id.timer);

        onNext(f1);



    }

ImageView correctImage;
    public void setCountryFlag(String countryCode, String countryCode2, String countryCode3) {
        countryCode = countryCode.toLowerCase();
        countryCode2 = countryCode2.toLowerCase();
        countryCode3 = countryCode3.toLowerCase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String finalCountryCode = countryCode;
        final String finalCountryCode2 = countryCode2;
        final String finalCountryCode3 = countryCode3;
        final ArrayList<Integer> flags = new ArrayList<>();
        flags.add(R.id.flag1);
        flags.add(R.id.flag2);
        flags.add(R.id.flag3);
        final int randomIndex = (int)(Math.random() * flags.size());
        System.out.println(flags);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    final ImageView img = findViewById(flags.get(randomIndex));
                    correctImage = img;
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            correctAnswer();
                            img.setBackgroundColor(Color.GREEN);


                        }
                    });
                    flags.remove(randomIndex);
                    int random2 = (int)(Math.random() * flags.size());
                    final ImageView img2 = findViewById(flags.get(random2));
                    img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wrongAnswer();

                            img.setBackgroundColor(Color.BLUE);

                        }
                    });
                    flags.remove(random2);
                    int random3 = (int)(Math.random() * flags.size());
                   final ImageView img3 = findViewById(flags.get(random3));
                    img3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wrongAnswer();
                            img.setBackgroundColor(Color.BLUE);


                        }
                    });
                    flags.remove(random3);

                    Bitmap bmp = null;
                    Bitmap bmp2 = null;
                    Bitmap bmp3 = null;
                    try {
                        URL url = new URL("https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/" + finalCountryCode + ".png");
                        URL url2 = new URL("https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/" + finalCountryCode2 + ".png");
                        URL url3 = new URL("https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/" + finalCountryCode3 + ".png");
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        bmp2 = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
                        bmp3 = BitmapFactory.decodeStream(url3.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img.setImageBitmap(bmp);
                    img2.setImageBitmap(bmp2);
                    img3.setImageBitmap(bmp3);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    Button next;

    public void onNext(View v){
        countryNameLabel.setText(getRandomCountry());
        f1.setEnabled(true);
        f2.setEnabled(true);
        f3.setEnabled(true);
        f1.setBackgroundResource(android.R.drawable.btn_default);
        f2.setBackgroundResource(android.R.drawable.btn_default);
        f3.setBackgroundResource(android.R.drawable.btn_default);
        answer.setText("");
        if(isTimer==true){
            runTimer();
        }

    }

    public String getRandomCountry(){
        next.setText(R.string.NEXT);
        next.setEnabled(false);

        String json = null;
        try {
            InputStream is = getAssets().open("countries.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject jsonObj = new JSONObject(json);
            Iterator i = jsonObj.keys();

            ArrayList cList = new ArrayList<>();
            final List countryNames = new ArrayList<>();

            while (i.hasNext()){
                cList.add(i.next());
                String code = cList.get(cList.size()-1).toString();
                countryNames.add(jsonObj.getString(code));
            }
            Collections.sort(countryNames);



            int randomCountryIndex = (int)(Math.random() * cList.size());
            int randomCountryIndex2 = (int)(Math.random() * cList.size());
            int randomCountryIndex3= (int)(Math.random() * cList.size() );
            String cCode = cList.get(randomCountryIndex).toString();
            String cCode2 = cList.get(randomCountryIndex2).toString();
            String cCode3 = cList.get(randomCountryIndex3).toString();
            country =  jsonObj.getString(cCode);
            setCountryFlag(cCode,cCode2,cCode3);
            Log.d("COUNTRYLOG", country);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return country;
    }






    private void wrongAnswer() {
        answer.setText(R.string.WRONG);
        answer.setTextColor(Color.RED);
        f2.setEnabled(false);
        f1.setEnabled(false);
        f3.setEnabled(false);
        next.setEnabled(true);
        if(isTimer){
            countDownTimer.cancel();
        }



    }


    private void correctAnswer() {

        answer.setText(R.string.CORRECT);
        answer.setTextColor(Color.GREEN);
        f2.setEnabled(false);
        f1.setEnabled(false);
        f3.setEnabled(false);
        next.setEnabled(true);
        if(isTimer){
            countDownTimer.cancel();
        }

    }

    CountDownTimer countDownTimer;
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
            wrongAnswer();
                correctImage.setBackgroundColor(Color.BLUE);



            }
        }.start();
    }

}
