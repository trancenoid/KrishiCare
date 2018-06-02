package xhedra.krishicare;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class ReplyToken extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("token log", FirebaseInstanceId.getInstance().getToken());
    }
}
