package xhedra.krishicare;

import android.app.ProgressDialog;
import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Location allowed", Toast.LENGTH_LONG).show();
            }

        }
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
