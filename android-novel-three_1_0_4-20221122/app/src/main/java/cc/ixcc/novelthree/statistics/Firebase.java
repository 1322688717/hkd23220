package cc.ixcc.novelthree.statistics;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class Firebase {

    private static Firebase instance;

    public static Firebase GetInstance() {
        if (instance == null) {
            instance = new Firebase();
        }
        return instance;
    }

    public String TAG = "Firebase";

    public void InitFirebase() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);

//                        AdjustUtil.GetInstance().setPushToken(token);
                    }
                });
    }

}
