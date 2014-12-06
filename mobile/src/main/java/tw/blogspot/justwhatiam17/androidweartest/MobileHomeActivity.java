package tw.blogspot.justwhatiam17.androidweartest;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MobileHomeActivity extends Activity {

    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static final String TAG = "tag";
    private static final String EXTRA_EVENT_ID = "Event ID";
    private static int notificationID = 0;
    private static String GROUP_KEY_EMAILS = "group_key_emails";
    private static int summaryCount = 0;
    private static NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    private static String COUNT_KEY = "COUNT_KEY";
    private static int serviceCount;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_home);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mobile_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void WannaClicked(View view) {

// Build intent for notification content
        Intent viewIntent = new Intent(this, MobileHomeActivity.class);
        viewIntent.putExtra(EXTRA_EVENT_ID, "100000");
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Notification")
                        .setContentText("Content")
                        .setContentIntent(viewPendingIntent);

        NotificationCreate(notificationBuilder);
//./adb -d forward tcp:5601 tcp:5601
    }

    public void GmapClicked(View view) {

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Oplink"));
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_ic_googleplayservices)
                        .setContentTitle("Gmap Title")
                        .setContentText("Gmap Content")
                        .setContentIntent(mapPendingIntent)
                        .addAction(R.drawable.common_full_open_on_phone, "Map", mapPendingIntent);

        NotificationCreate(notificationBuilder);
    }

    public void WearableOnlyClicked(View view) {
        // Create an intent for the reply action
        Intent actionIntent = new Intent(this, MyNoteActivity.class);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

// Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.common_signin_btn_icon_dark,
                        "Wearable", actionPendingIntent)
                        .build();
//        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
//        bigStyle.bigText("test");
// Build the notification and add the action via WearableExtender
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.powered_by_google_light)
                        .setContentTitle("Wearable Only")
                        .setContentText("Content")
                        .extend(new WearableExtender().addAction(action));

        NotificationCreate(builder);
    }

    public void VoiceInputClicked(View view) {
        String replyLabel = getResources().getString(R.string.reply_label);
        String[] replyChoices = getResources().getStringArray(R.array.reply_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .setChoices(replyChoices)
                .build();

        Intent replyIntent = new Intent(this, Receiver.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(this, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

// Create the reply action and add the remote input
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_plusone_medium_off_client,
                        "Voice", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

// Build the notification and add the action via WearableExtender
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_full_open_on_phone)
                        .setContentTitle("Voice Input")
                        .setContentText("Content")
                        .extend(new WearableExtender().addAction(action));

        NotificationCreate(builder);
    }

    public void AddPageClicked(View view) {

        Intent viewIntent = new Intent(this, MobileHomeActivity.class);
        viewIntent.putExtra(EXTRA_EVENT_ID, "100000");
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);


        // Create builder for the main notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.powered_by_google_light)
                        .setContentTitle("Page 1")
                        .setContentText("Short message")
                        .setContentIntent(viewPendingIntent);

// Create a big text style for the second page
        NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
        secondPageStyle.setBigContentTitle("Page 2")
                .bigText("A lot of text...");

// Create second page notification
        Notification secondPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(secondPageStyle)
                        .build();

// Add second page with wearable extender and extend the main notification
        // Add second page with wearable extender and extend the main notification
        Notification twoPageNotification =
                new WearableExtender()
                        .addPage(secondPageNotification)
                        .extend(notificationBuilder)
                        .build();

// Issue the notification
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(++notificationID, twoPageNotification);
    }

    public void GroupNotificationClicked(View view) {
        summaryCount++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("New mail from " + "User " + summaryCount)
                .setContentText("Subject" + summaryCount)
                .setSmallIcon(R.drawable.ic_plusone_small_off_client)
                .setGroup(GROUP_KEY_EMAILS)
                .setGroupSummary(true)
                .setStyle(
                        inboxStyle
                                .addLine("New mail from " + "User " + summaryCount)
                                .setBigContentTitle(summaryCount + " Messages")
                                .setSummaryText("xxx@gmail.com")
                );
        notificationID--;


        NotificationCreate(builder);
    }

    public void googleServiceClicked(View view) {

        if (mGoogleApiClient == null)
            return;

        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                final List<Node> nodes = result.getNodes();
                if (nodes != null) {
                    for (int i=0; i<nodes.size(); i++) {
                        final Node node = nodes.get(i);

                        // You can just send a message
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/MESSAGE", null);



                        // or you may want to also check check for a result:
                        // final PendingResult<SendMessageResult> pendingSendMessageResult = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/MESSAGE", null);
                        // pendingSendMessageResult.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        //      public void onResult(SendMessageResult sendMessageResult) {
                        //          if (sendMessageResult.getStatus().getStatusCode()==WearableStatusCodes.SUCCESS) {
                        //              // do something is successed
                        //          }
                        //      }
                        // });
                    }
                }
            }
        });

        return;
//        GoogleApiClient mGoogleAppiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                    @Override
//                    public void onConnected(Bundle connectionHint) {
//                        Log.d(TAG, "onConnected: " + connectionHint);
//                        // Now you can use the data layer API
//                    }
//                    @Override
//                    public void onConnectionSuspended(int cause) {
//                        Log.d(TAG, "onConnectionSuspended: " + cause);
//                    }
//                })
//                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(ConnectionResult result) {
//                        Log.d(TAG, "onConnectionFailed: " + result);
//                    }
//                })
//
//                .addApi(Wearable.API)
//                .build();
//
//        mGoogleAppiClient.connect();
//
//
    }

    public void SendDataClicked(View view) {
        if(mGoogleApiClient == null)
            return;

        PutDataMapRequest dataMap = PutDataMapRequest.create("/COUNT");
        dataMap.getDataMap().putInt(COUNT_KEY, ++serviceCount);
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.d(TAG, "count updated:" + serviceCount);
            }
        });

    }


    private void NotificationCreate(NotificationCompat.Builder builder) {
        notificationID++;

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationID, builder.build());
    }



    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.v("s", spokenText);
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //http://stackoverflow.com/questions/25956982/android-wear-doesnt-return-voice-data
    private void getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {
            Log.v("123", remoteInput.getCharSequence(EXTRA_VOICE_REPLY).toString());
        }

        //return null;
    }

}
