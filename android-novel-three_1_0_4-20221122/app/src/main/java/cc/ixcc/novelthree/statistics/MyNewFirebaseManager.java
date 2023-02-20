package cc.ixcc.novelthree.statistics;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyNewFirebaseManager extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

//        AdjustUtil.GetInstance().setPushToken(s);
        // the rest of the code that makes use of the token goes in this method as well
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().containsKey("af-uinstall-tracking")){
            return;
        } else {
            //handleNotification(remoteMessage);
        }
    }
}