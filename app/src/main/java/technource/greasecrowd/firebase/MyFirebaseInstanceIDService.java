package technource.greasecrowd.firebase;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import technource.greasecrowd.helper.AppLog;

// Created by Dharvik on 6/25/2016.

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

  private static final String TAG = "MyFirebaseIIDService";

  @Override
  public void onTokenRefresh() {

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    AppLog.Log(TAG, "Refreshed token: " + refreshedToken);

    // TODO: Implement this method to send any registration to your app's servers.
    sendRegistrationToServer(refreshedToken);
  }

  private void sendRegistrationToServer(String token) {

  }
}