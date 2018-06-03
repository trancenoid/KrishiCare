package xhedra.krishicare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewQuery extends AppCompatActivity implements TextToSpeech.OnInitListener,View.OnClickListener {

    public ListView mList2;
    public ImageButton speakButton2;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    private FloatingActionButton record;
    private TextView label;
    private ImageView imgUpld;
    private MediaRecorder mRecorder;

    private ImageButton stopRecording;

    private String mFileName = null;

    private static final String LOG_TAG = "Record_log";

    private StorageReference storage;

    private ProgressDialog progressDialog;
    private final int PICK_IMAGE_REQUEST = 71;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String mCurrentImagePath;
    Uri filePath;

    private TextView image, video, text;
    private Button upload;
    private TextToSpeech engine;

    private EditText textQuery;
    private TextView videoQuery, imageQuery;

    private DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query);

        stopRecording = (ImageButton)findViewById(R.id.stopRec);

        engine = new TextToSpeech(this, this);

        databaseReference = FirebaseDatabase.getInstance().getReference("query");

        //upload = findViewById(R.id.btn_speak2);
        image = findViewById(R.id.imageQuery);
        video = findViewById(R.id.videoQuery);
        text = findViewById(R.id.textQuery);
        textQuery = (EditText) findViewById(R.id.editText2);

        mList2 = (ListView)findViewById(R.id.list3);

        speakButton2 = (ImageButton) findViewById(R.id.btn_speak2);
        speakButton2.setOnClickListener(this);

        voiceinputbuttons();

        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
                uploadAudio();
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 29);
        }

        progressDialog = new ProgressDialog(this);
        imgUpld = findViewById(R.id.imgUpld);
        storage = FirebaseStorage.getInstance().getReference();

        record = findViewById(R.id.record);
        label = findViewById(R.id.audioQuery);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/record.3gp";

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                   startRecording();
                    label.setText("Started Recording....");

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    stopRecording();
                    label.setText("Recording stopped");

                }

                return false;
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String textS = text.getText().toString();
                String query = textQuery.toString();
                speak(textS);
                //addQuery();
                StorageReference filepath = storage.child(Login.phn).child("text.txt");

                filepath.putBytes(query.getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        label.setText("Uploading Finished");
                    }
                });
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String videotext = video.getText().toString();
                speak(videotext);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String imageText = image.getText().toString();
                speak(imageText);
            }
        });

        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String audioText = label.getText().toString();
                speak(audioText);
            }
        });
        imgUpld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

       // upload.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
             //   uploadImage();
           // }
        //});

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Camera allowed", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 29) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "MIC allowed", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadAudio();
    }

    private void uploadAudio() {

        progressDialog.setMessage("Uploading Audio...");
        progressDialog.show();

        StorageReference filepath = storage.child(Login.phn).child("recording.3gp");
        Uri uri = Uri.fromFile(new File(mFileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                label.setText("Uploading Finished");
            }
        });


    }

    private File createNewFile() throws IOException {
        String header = "IMG";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String prefix = header + timeStamp;
        String suffix = ".jpg";
        File image = File.createTempFile(prefix, suffix, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        mCurrentImagePath = image.getPath();
        return image;
    }

    private void uploadImage() {
        StorageReference storageReference = storage.child(Login.phn).child("IMG.jpg");
        storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewQuery.this, "Success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewQuery.this, "Could not upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //calculating progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                progressDialog.show();
            }
        });
    }
    private void uploadVideo() {
        StorageReference storageReference = storage.child(Login.phn).child("VID.mp4");
        storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewQuery.this, "Success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewQuery.this, "Could not upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //calculating progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                progressDialog.show();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri PhotoUri = FileProvider.getUriForFile(NewQuery.this, "xhedra.krishicare.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, PhotoUri);
                startActivityForResult(intent, 1);


            }
        }
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.getDefault());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String s) {
        engine.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null){
            filePath = Uri.fromFile(new File(mCurrentImagePath));
            Picasso.get().load(filePath).into(imgUpld);
        }
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList2.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));
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
                uploadImage();
                Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show();
            }else if(matches.contains("do") || matches.contains("to")){
                uploadImage();
                Toast.makeText(this,"Video Uploaded",Toast.LENGTH_SHORT).show();
            }else if(matches.contains("three") || matches.contains("teen")){
                String textS = text.getText().toString();
                String query = textQuery.toString();
                speak(textS);
                //addQuery();
                StorageReference filepath = storage.child("Text").child("text.txt");

                filepath.putBytes(query.getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        label.setText("Uploading Finished");
                    }
                });
            }else if(matches.contains("four") || matches.contains("chaar")){
                startRecording();
            }else{
                Toast.makeText(this,"Please speak again",Toast.LENGTH_SHORT).show();
            }

            if (matches.contains("information")) {
                informationMenu();
            }
        }
    }

    private void chooseVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (videoFile != null) {
                Uri VideoUri = FileProvider.getUriForFile(NewQuery.this, "xhedra.krishicare.fileprovider", videoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, VideoUri);
                startActivityForResult(intent, 1);


            }
        }
    }

    public void informationMenu() {
        startActivity(new Intent("android.intent.action.INFOSCREEN"));
    }

    public void voiceinputbuttons() {
        speakButton2 = (ImageButton) findViewById(R.id.btn_speak2);
        mList2 = (ListView) findViewById(R.id.list3);
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Second.class));
    }

}
