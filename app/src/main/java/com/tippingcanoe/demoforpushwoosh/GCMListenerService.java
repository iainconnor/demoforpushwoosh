package com.tippingcanoe.demoforpushwoosh;

import android.os.Bundle;
import android.util.Log;

import com.pushwoosh.internal.utils.NotificationPrefs;

public class GCMListenerService extends com.pushwoosh.GCMListenerService {
    @Override
    public void onMessageReceived ( String s, Bundle bundle ) {
        if (bundle == null) {
            return;
        }

        Log.v("Push", "Received message " + s + " " + bundle.getString("u") + " " + NotificationPrefs.getMessageId(this));

        super.onMessageReceived(s, bundle);
    }
}
