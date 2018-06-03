package xhedra.krishicare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;


public class Login extends AppCompatActivity implements TextToSpeech.OnInitListener{
    Button lgnIn;
    public EditText phoneNo;
    static public String phn;
    private TextToSpeech engine;
    private Button location;

    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        location = (Button)findViewById(R.id.btnlocation);
        progressDialog = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        engine = new TextToSpeech(this,this);
        phoneNo = findViewById(R.id.editText);
        lgnIn = findViewById(R.id.btnLogin);
        lgnIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String login = lgnIn.getText().toString();
                speak(login);
                phn = phoneNo.getText().toString();
                getApplicationContext().startActivity(new Intent(getApplicationContext(),Second.class));
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Finding Location...");
                progressDialog.show();
                Thread thread = new Thread(){

                    public void runThread(){

                        try {

                            sleep(3000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                };
                thread.start();
                progressDialog.setMessage("Location Found : Nariman Point,Mumbai,India");
                progressDialog.dismiss();
                location.setText("Location found");



            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}
