package xhedra.krishicare;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Second extends AppCompatActivity implements TextToSpeech.OnInitListener,View.OnClickListener {

    private ImageButton newQuery,faq,reply;
    private Button query,freq,rep;

    private TextToSpeech engine;

    public ListView mList1;
    public ImageButton speakButton1;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        newQuery = (ImageButton)findViewById(R.id.queryText);
        faq = (ImageButton)findViewById(R.id.faqText);
        reply = (ImageButton)findViewById(R.id.replyText);

        engine = new TextToSpeech(this,this);

        query = (Button)findViewById(R.id.query);
        freq = (Button)findViewById(R.id.faq);
        rep = (Button)findViewById(R.id.reply);

        newQuery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String querystring = query.getText().toString();
                speak(querystring);

            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String faqString = freq.getText().toString();
                speak(faqString);
            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String replyString = rep.getText().toString();
                speak(replyString);
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NewQuery.class));
            }
        });

        speakButton1 = (ImageButton) findViewById(R.id.btn_speak1);
        speakButton1.setOnClickListener(this);

        voiceinputbuttons();
    }
    public void newQuery(View view){
        getApplicationContext().startActivity(new Intent(getApplicationContext(),NewQuery.class));
    }
    public void Rep(View view) {
        getApplicationContext().startActivity(new Intent(getApplicationContext(),Second.class));
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
        speakButton1 = (ImageButton) findViewById(R.id.btn_speak1);
        mList1 = (ListView) findViewById(R.id.list);
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
            mList1.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));
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
            if(matches.contains("one") || matches.contains("ek")){
                startActivity(new Intent(this,NewQuery.class));
            }else if(matches.contains("to") || matches.contains("do")){
                startActivity(new Intent(this,NewQuery.class));
            }else if(matches.contains("three") || matches.contains("teen")){
                startActivity(new Intent(this,NewQuery.class));
            }

            if (matches.contains("information")) {
                informationMenu();
            }
        }

    }
}


