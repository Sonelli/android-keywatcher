package com.sonelli.keywatcher;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends ActionBarActivity {

    private TextView logView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.hardware_keyevent_logger);

        this.scrollView = (ScrollView) findViewById(R.id.scrollview);
        this.logView = (TextView) findViewById(R.id.log);

        this.logView.setText(generateDeviceInfo());
        this.logView.setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View view, int i, KeyEvent event) {

               StringBuilder sb = new StringBuilder();
               sb.append(logView.getText());
               sb.append("\n*** Key Event Detected ***\n");
               sb.append("Key Code: ").append(event.getKeyCode()).append("\n");
               sb.append("Action: ").append(event.getAction()).append("\n");
               sb.append("Meta State: ").append(event.getMetaState()).append("\n");
               sb.append("Flags: ").append(event.getFlags()).append("\n");
               sb.append("Source: ").append(event.getSource()).append("\n");
               sb.append("Keyboard Type: ").append(event.getDevice().getKeyboardType()).append("\n");
               sb.append("Device ID: ").append(event.getDeviceId()).append("\n");
               logView.setText(sb.toString());

               scrollView.post(new Runnable() {
                   @Override
                   public void run() {
                       scrollView.fullScroll(View.FOCUS_DOWN);
                   }
               });

               return true;

           }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        logView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_clear:
                logView.setText(generateDeviceInfo());
                return true;

            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "KeyEvent Information");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, logView.getText());
                startActivity(Intent.createChooser(sharingIntent, "Share Using:"));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public String generateDeviceInfo(){

        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(new Date().toString()).append("\n");
        sb.append("Device Manufacturer: ").append(Build.MANUFACTURER).append("\n");
        sb.append("Device Model: ").append(Build.MODEL).append("\n");
        sb.append("Device Board: ").append(Build.BOARD).append("\n");
        sb.append("Device Bootloader: ").append(Build.BOOTLOADER).append("\n");
        sb.append("Android Version: ").append(Build.VERSION.CODENAME).append(" ").append(Build.VERSION.RELEASE).append(" (sdk:").append(Build.VERSION.SDK_INT).append(")\n");

        return sb.toString();

    }

}
