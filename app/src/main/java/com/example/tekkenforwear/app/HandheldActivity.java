package com.example.tekkenforwear.app;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
//import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class HandheldActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener{

    private GoogleApiClient mGoogleApiClient;
    private final String TAG = "handheld";
    // private String mNode;
    private TextView value;
    private MainView mMainView;
    private Sound se_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_handheld);
        se_player = new Sound(this);
        mMainView = new MainView(this);
        setContentView(mMainView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


//        value = (TextView)findViewById(R.id.valueLabel);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)

                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed : " + connectionResult.toString());
                    }
                })
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");

    }
    @Override
    public void onMessageReceived(MessageEvent event) {

        final String message = event.getPath();
        Log.d(TAG, message);

        if (message.equals("PUNCH")){
            mMainView.punch();
            se_player.play(1);
        }else if(message.equals("UPPER")) {
            mMainView.upper();
            se_player.play(2);
        }else if(message.equals("HOOK")){
            mMainView.hook();
            se_player.play(3);
        }


//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                value.setText(message);
//            }
//        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_handheld, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
