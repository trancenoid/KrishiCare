package xhedra.krishicare;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ReplyCatcher extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Received: ", remoteMessage.getMessageId());
        String url = remoteMessage.getNotification().getBody();
        Intent intent = new Intent(getApplicationContext(), Reply.class);
        intent.putExtra("URL", url);
        getApplicationContext().startActivity(intent);
    }
}
