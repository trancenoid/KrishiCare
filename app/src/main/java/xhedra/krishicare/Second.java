package xhedra.krishicare;

import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;

public class Second extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageButton newQuery,faq,reply;
    private Button query,freq,rep;

    private TextToSpeech engine;

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



}
