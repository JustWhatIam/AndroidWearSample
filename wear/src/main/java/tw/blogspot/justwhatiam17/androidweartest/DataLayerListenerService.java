package tw.blogspot.justwhatiam17.androidweartest;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by justwhatiam on 2014/10/29.
 */
public class DataLayerListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if("/MESSAGE".equals(messageEvent.getPath())) {

            Intent intent = new Intent(this, MyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // launch some Activity or do anything you like
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            DataItem dataItem = event.getDataItem();
            if ("/COUNT".equals(dataItem.getUri().getPath())) {
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                int count = dataMap.getInt("COUNT_KEY");

                Intent intent = new Intent(Intent.ACTION_DEFAULT);
//                if(message != null)
                intent.putExtra("SEND_COUNT", count);
                this.sendBroadcast(intent);
            }
        }
    }

}
