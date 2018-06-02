
package xhedra.krishicare;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView)findViewById(R.id.text);
        imageView = (ImageView)findViewById(R.id.image);

        Animation myAnimation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        textView.startAnimation(myAnimation);
        imageView.startAnimation(myAnimation);

        final Intent i1 = new Intent(this,MainActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i1);
                    finish();
                }
            }
        };

        timer.start();
    }
}
