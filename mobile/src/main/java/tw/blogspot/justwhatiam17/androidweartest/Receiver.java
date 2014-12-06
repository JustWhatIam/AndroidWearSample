package tw.blogspot.justwhatiam17.androidweartest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by justwhatiam on 2014/10/28.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WEAR", "Context: " + context.toString());
        Log.d("WEAR", "Intent: " + intent.toString());
        Log.d("WEAR", "Action" + intent.getAction());
        if(intent.getAction().equals(MobileHomeActivity.EXTRA_VOICE_REPLY)){
//            String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
//            Log.d("WEAR", "Message from intent: " + message);
//            Toast.makeText(context, "Message from intent: " + message, Toast.LENGTH_LONG).show();
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            CharSequence reply = remoteInput.getCharSequence(MobileHomeActivity.EXTRA_VOICE_REPLY);
            Log.d("WEAR", "User replies: " + reply);
            Toast.makeText(context, "User replies: " + reply, Toast.LENGTH_LONG).show();
        }
    }
}

