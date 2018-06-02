package xhedra.krishicare;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,View.OnClickListener {

    Button loc_en;
    Button loc_hi;
    Button loc_mr;
    private TextToSpeech engine;

    public ListView mList;
    public ImageButton speakButton;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
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
                speak("english"); // text to speech of the text view
                setLocale("en");
            }
        });

        loc_hi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                String hindi = loc_hi.getText().toString();
                speak("hindi");
                setLocale("hi");
            }
        });

        loc_mr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                String marath = loc_mr.getText().toString();
                speak("marathi");
                setLocale("mr");
            }
        });

        speakButton = (ImageButton) findViewById(R.id.btn_speak);
        speakButton.setOnClickListener(this);
                voiceinputbuttons();




    }
    protected void setLocale(String loc){
        Locale myLocale = new Locale(loc);
        Resources res = getResources();
        DisplayMetrics myDM = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, myDM);
        Intent refresh = new Intent(this, Login.class);
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

    public void informationMenu() {
        startActivity(new Intent("android.intent.action.INFOSCREEN"));
    }

    public void voiceinputbuttons() {
        speakButton = (ImageButton) findViewById(R.id.btn_speak);
        mList = (ListView) findViewById(R.id.list);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        startVoiceRecognitionActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")
            if(matches.contains("Ek") || matches.contains("one")){
                String marath = loc_mr.getText().toString();
                speak("marathi");
                setLocale("mr");
                startActivity(new Intent(this,Login.class));
            }
            else if(matches.contains("do") || matches.contains("to")){
                String hindi = loc_hi.getText().toString();
                speak("hindi");
                setLocale("hi");
            }
            else if(matches.contains("teen") || matches.contains("three")){
                String english = loc_en.getText().toString();
                speak("english"); // text to speech of the text view
                setLocale("en");
            }
            else{
                Toast.makeText(this,"Please speak again",Toast.LENGTH_SHORT).show();
            }

            if (matches.contains("information")) {
                informationMenu();
            }
        }

    }
}

