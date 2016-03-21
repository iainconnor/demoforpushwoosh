package com.tippingcanoe.demoforpushwoosh;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pushwoosh.internal.PushManagerImpl;
import com.pushwoosh.internal.utils.NotificationPrefs;
import com.pushwoosh.notification.PushData;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMListenerService extends com.pushwoosh.GCMListenerService {
    @Override
    public void onMessageReceived ( String s, Bundle bundle ) {
        if (bundle == null) {
            return;
        }

        Log.v("Push", "Received message " + s + " " + bundle.getString("u") + " " + NotificationPrefs.getMessageId(this));

        Notification notification = null;
        PushData pushData = null;
        try {
            pushData = new PushData(this, bundle);

            //create notification builder
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

            //set title of notification
            notificationBuilder.setContentTitle(pushData.getHeader());

            //set content of notification
            notificationBuilder.setContentText(pushData.getMessage());

            //set small icon (usually app icon)
            notificationBuilder.setSmallIcon(pushData.getSmallIcon());

            //set ticket text
            notificationBuilder.setTicker(pushData.getTicker());

            //display notification now
            notificationBuilder.setWhen(System.currentTimeMillis());


            if (pushData.getBigPicture() != null)
            {
                //set big image if available
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(pushData.getBigPicture()).setSummaryText((pushData.getMessage())));
            }
            else
            {
                //otherwise it's big text style
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(pushData.getMessage()));
            }

            //support icon background color
            if (pushData.getIconBackgroundColor() != null)
            {
                notificationBuilder.setColor(pushData.getIconBackgroundColor());
            }

            //support custom icon
            if (null != pushData.getLargeIcon())
            {
                notificationBuilder.setLargeIcon(pushData.getLargeIcon());
            }

            //build the notification
            notification = notificationBuilder.build();


        } catch (PushData.BadPushBundleException e) {
            e.printStackTrace();
        }

        JSONObject pushBundleJson = PushManagerImpl.bundleToJSON(bundle);

        if ( notification != null && pushData != null && pushBundleJson != null && pushBundleJson.has("userdata") ) {

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            try {
                synchronized (this) {
                    JSONObject userdata = pushBundleJson.getJSONObject("userdata");
                    int messageId = getMessageId(this);
                    Log.v("Iain", messageId + " " + userdata.toString());
                    manager.notify(userdata.getString("push_message_id"), messageId, notification);
                    setMessageId(this, messageId + 1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setMessageId(Context context, int messageId) {
        SharedPreferences prefs = context.getSharedPreferences("foobar", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("dm_messageid", messageId);
        editor.commit();
    }

    public static int getMessageId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("foobar", 0);
        int value = prefs.getInt("dm_messageid", 1001);
        return value;
    }
}
