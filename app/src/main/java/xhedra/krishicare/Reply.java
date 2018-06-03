package xhedra.krishicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

public class Reply extends AppCompatActivity {

    private ImageView imageView;
    private VideoView videoView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        imageView = (ImageView)findViewById(R.id.imageReply);
        Intent intent = getIntent();
        if(intent.hasExtra("URL")){
            Picasso.get().load(intent.getStringExtra("URL")).into(imageView);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Second.class));
    }
}
