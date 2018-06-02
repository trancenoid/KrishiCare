package xhedra.krishicare;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    Button loc_en;
    Button loc_hi;
    Button loc_mr;
    private TextToSpeech engine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        engine = new TextToSpeech(this,this);

        loc_mr = findViewById(R.id.mr);
        loc_hi = findViewById(R.id.hi);
        loc_en = findViewById(R.id.en);


        loc_en.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String english = loc_en.getText().toString();
                speak(english); // text to speech of the text view
                setLocale("en");
            }
        });

        loc_hi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String hindi = loc_hi.getText().toString();
                speak(hindi);
                setLocale("hi");
            }
        });

        loc_mr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String marath = loc_mr.getText().toString();
                speak(marath);
                setLocale("mr");
            }
        });

    }
    protected void setLocale(String loc){
        Locale myLocale = new Locale(loc);
        Resources res = getResources();
        DisplayMetrics myDM = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, myDM);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();


    }

    @Override
    public void onInit(int i) {

        if(i == TextToSpeech.SUCCESS){
            engine.setLanguage(Locale.getDefault());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String s){
        engine.speak(s,TextToSpeech.QUEUE_FLUSH,null,null);
    }
}

