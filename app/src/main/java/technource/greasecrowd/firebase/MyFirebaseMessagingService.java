package technource.greasecrowd.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.activities.DiscussionActivity;
import technource.greasecrowd.activities.JobDetailsActivity;
import technource.greasecrowd.activities.StaticScreen;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.helper;
import technource.greasecrowd.model.LoginDetail_DBO;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    public static String Ntype = "";
    public static String msg = "";
    public static boolean isActivityRunning;
    String message;
    LoginDetail_DBO loginDetail_dbo;
    MyPreference myPreference;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        myPreference = new MyPreference(MyFirebaseMessagingService.this);
        AppLog.Log(TAG, "From: " + remoteMessage.toString());
        AppLog.Log(TAG, "Data: " + remoteMessage.getData());
        AppLog.Log(TAG, "onMessageReceived");

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT", object.toString());
        loginDetail_dbo = HelperMethods
                .getUserDetailsSharedPreferences(MyFirebaseMessagingService.this);

        JSONObject json = new JSONObject(remoteMessage.getData());
        try {

            String jsonData = "";
            if (json.has("data")) {
                jsonData = json.getString("data").toString();
                AppLog.Log(TAG, "Data: " + json.getString("data").toString());
            } else {
                jsonData = json.toString();
            }
            JSONObject jsonObject = new JSONObject(jsonData);
            String type = jsonObject.getString("type");
            if (type.equalsIgnoreCase("deact_acc")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    LogOut();
                    Intent intent = new Intent(MyFirebaseMessagingService.this, StaticScreen.class);
                    intent.putExtra("flag", "1");
                    intent.putExtra("message", jsonObject.getString("body"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Constants.INTENT_FLAGS);
                    startActivity(intent);
                } else {
                    generateAccountDeactivateNotification(jsonObject);
                }
            } else if (type.equalsIgnoreCase("del_acc")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    LogOut();
                    Intent intent = new Intent(MyFirebaseMessagingService.this, StaticScreen.class);
                    intent.putExtra("flag", "1");
                    intent.putExtra("message", jsonObject.getString("body"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Constants.INTENT_FLAGS);
                    startActivity(intent);
                } else {
                    generateAccountDeactivateNotification(jsonObject);
                }
            } else if (type.equalsIgnoreCase("logout")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    LogOut();
                    Intent intent = new Intent(MyFirebaseMessagingService.this, StaticScreen.class);
                    intent.putExtra("flag", "1");
                    intent.putExtra("message", jsonObject.getString("body"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Constants.INTENT_FLAGS);
                    startActivity(intent);
                } else {
                    generateAccountLogouteNotification(jsonObject);
                }
            } else if (type.equalsIgnoreCase("NJ")) {
                generateNewJobPostNotification(jsonObject);
            } else if (type.equalsIgnoreCase("PM")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    JSONObject jobj = jsonObject.getJSONObject("data");
                    if (myPreference.getBooleanReponse(Constants.NotificationTags.CHAT_NOTI)) {
                        Intent intent1 = new Intent("OnMessageRecieved");
                        intent1.putExtra("flag", "1");
                        intent1.putExtra("name", jobj.getString("name"));
                        intent1.putExtra("text", jobj.getString("message_text"));
                        intent1.putExtra("img", jobj.getString("img"));
                        intent1.putExtra("object_type", jobj.getString("object_type"));
                        intent1.putExtra("object_id", jobj.getString("object_id"));
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                    } else {
                        generateChatMEssage(jsonObject, "1");
                    }
                } else {
                    generateChatMEssage(jsonObject, "1");
                }
            } else if (type.equalsIgnoreCase("DM")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    JSONObject jobj = jsonObject.getJSONObject("data");
                    if (myPreference.getBooleanReponse(Constants.NotificationTags.CHAT_NOTI_PUBLIC)) {
                        Intent intent1 = new Intent("OnMessageRecievedPublic");
                        intent1.putExtra("flag", "0");
                        intent1.putExtra("name", jobj.getString("name"));
                        intent1.putExtra("text", jobj.getString("message_text"));
                        intent1.putExtra("img", jobj.getString("img"));
                        intent1.putExtra("object_type", jobj.getString("object_type"));
                        intent1.putExtra("object_id", jobj.getString("object_id"));
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                    } else {
                        generateChatMEssage(jsonObject, "0");
                    }
                } else {
                    generateChatMEssage(jsonObject, "0");
                }
            } else if (type.equalsIgnoreCase("RTP")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    Toast.makeText(this, "RTP notification found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "RTP notification found", Toast.LENGTH_SHORT).show();
                }
            } else if (type.equalsIgnoreCase("WD")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    Toast.makeText(this, "WD notification found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "WD notification found", Toast.LENGTH_SHORT).show();
                }
            } else if (type.equalsIgnoreCase("BP")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    generateBidPostNotification(jsonObject);
                } else {
                    generateBidPostNotification(jsonObject);
                }
            } else if (type.equalsIgnoreCase("BU")) {
                if (!helper.isAppIsInBackground(MyFirebaseMessagingService.this)) {
                    generateBidPostNotification(jsonObject);
                } else {
                    generateBidPostNotification(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void generateChatMEssage(JSONObject jsonObject, String flag) {
        JSONObject jobj = null;
        AppLog.Log(TAG, "generateAccountDeactivateNotification");
        Intent intent = new Intent(getApplicationContext(), DiscussionActivity.class);
        try {
            jobj = jsonObject.getJSONObject("data");
            message = jsonObject.getString(Constants.NotificationTags.BODY);
            intent.putExtra("flag", flag);
            intent.putExtra("job_id", jobj.getString("job_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, HelperMethods.randomInteger(), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.applogo))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
//                .setColor(getColor(R.color.background_dark))
                .setWhen(System.currentTimeMillis());

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(HelperMethods.randomInteger(), builder.build());
    }

    private void LogOut() {
        MyPreference myPreference = new MyPreference(MyFirebaseMessagingService.this);

        myPreference.removeBooleanReponse();
        myPreference.removeIntegerReponse();
        myPreference.removeStringReponse();
        HelperMethods.deleteUserDetailsSharedPreferences(MyFirebaseMessagingService.this);
    }

    private void generateAccountDeactivateNotification(JSONObject jsonObject) {
        AppLog.Log(TAG, "generateAccountDeactivateNotification");
        Intent intent = new Intent(getApplicationContext(), StaticScreen.class);
        try {
            message = jsonObject.getString(Constants.NotificationTags.BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("flag", "1");
        intent.putExtra("message", message);
        intent.setFlags(Constants.INTENT_FLAGS);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, HelperMethods.randomInteger(), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.applogo))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(getColor(R.color.background_dark))
                .setWhen(System.currentTimeMillis());

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(HelperMethods.randomInteger(), builder.build());
        //LogOut();
    }

    private void generateAccountLogouteNotification(JSONObject jsonObject) {
        AppLog.Log(TAG, "generateAccountDeactivateNotification");
        Intent intent = new Intent(getApplicationContext(), StaticScreen.class);

        try {
            message = jsonObject.getString(Constants.NotificationTags.BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("flag", "1");
        intent.putExtra("message", message);
        intent.setFlags(Constants.INTENT_FLAGS);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, HelperMethods.randomInteger(), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.applogo))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
//                .setColor(getColor(R.color.background_dark))
                .setWhen(System.currentTimeMillis());

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(HelperMethods.randomInteger(), builder.build());
        LogOut();
    }

    private void generateBidPostNotification(JSONObject jsonObject) {
        AppLog.Log(TAG, "generateNewJobPostNotification");

        Intent intent = new Intent(getApplicationContext(), JobDetailsActivity.class);

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            message = jsonObject.getString(Constants.NotificationTags.BODY);
            intent.putExtra("job_id", data.getString("job_info"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        intent.putExtra("message", message);
        intent.setFlags(Constants.INTENT_FLAGS);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, HelperMethods.randomInteger(), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.applogo))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(HelperMethods.randomInteger(), builder.build());

    }

    private void generateNewJobPostNotification(JSONObject jsonObject) {
        AppLog.Log(TAG, "generateNewJobPostNotification");
        Intent intent = new Intent(getApplicationContext(), StaticScreen.class);

        try {
            message = jsonObject.getString(Constants.NotificationTags.BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.putExtra("flag", "1");
        intent.putExtra("message", message);
        intent.setFlags(Constants.INTENT_FLAGS);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, HelperMethods.randomInteger(), intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.applogo))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(HelperMethods.randomInteger(), builder.build());

    }

}