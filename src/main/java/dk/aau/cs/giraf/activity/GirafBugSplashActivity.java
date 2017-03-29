package dk.aau.cs.giraf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.*;
import dk.aau.cs.giraf.gui.R;



/**
 * Created on 30/04/2015.
 * Modified on 29/03/2017.
 */
public class GirafBugSplashActivity extends GirafActivity {

    public static final String EXCEPTION_TAG = "EXCEPTION_TAG";
    public static final String TRACKING_ID_TAG = "TRACKING_ID_TAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.giraf_bug_splash_activity);

        Exception ex = (Exception) getIntent().getExtras().getSerializable(EXCEPTION_TAG);
        String trackingId = getIntent().getExtras().getString(TRACKING_ID_TAG);
        // Find the messages
        String message = ex.getMessage();
        String stacktrace = getStackTraceArray(ex.getStackTrace());

        // Find and set the exception message
        TextView exceptionMessageView = (TextView) findViewById(R.id.exception);
        exceptionMessageView.setText(message);

        // FInd and set the stacktrace message
        TextView stacktraceView = (TextView) findViewById(R.id.stacktrace);
        stacktraceView.setText(stacktrace);

        //Sends the exception to google analytics
        Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(trackingId);
        tracker.send(MapBuilder.createException(new StandardExceptionParser(this,null)
            .getDescription(Thread.currentThread().getName(),ex),true)
            .build());

    }


    private String getStackTraceArray(StackTraceElement[] stackTraceElements) {
        String[] stackTraceLines = new String[stackTraceElements.length];

        int i = 0;
        for (StackTraceElement se : stackTraceElements) {
            stackTraceLines[i++] = se.toString();
        }

        StringBuilder builder = new StringBuilder();
        for (String s : stackTraceLines) {
            builder.append(s);
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public void onBackPressed() {
        // When the back-button is pressed, stop this activity
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
