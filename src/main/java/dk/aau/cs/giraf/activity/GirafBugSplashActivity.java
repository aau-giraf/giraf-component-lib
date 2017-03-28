package dk.aau.cs.giraf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.analytics.tracking.android.EasyTracker;

import dk.aau.cs.giraf.gui.R;

/**
 * Created on 30/04/2015.
 */
public class GirafBugSplashActivity extends GirafActivity {

    public static final String EXCEPTION_MESSAGE_TAG = "EXCEPTION_MESSAGE_TAG";
    public static final String EXCEPTION_STACKTRACE_TAG = "EXCEPTION_STACKTRACE_TAG";
    private EasyTracker easytracker = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.giraf_bug_splash_activity);

        // Find the messages
        String message = getIntent().getExtras().getString(EXCEPTION_MESSAGE_TAG);
        String stacktrace = getIntent().getExtras().getString(EXCEPTION_STACKTRACE_TAG);

        // Find and set the exception message
        TextView exceptionMessageView = (TextView) findViewById(R.id.exception);
        exceptionMessageView.setText(message);

        // FInd and set the stacktrace message
        TextView stacktraceView = (TextView) findViewById(R.id.stacktrace);
        stacktraceView.setText(stacktrace);

        easytracker = new EasyTracker.getInstance(this);
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
    public onStart(){
        super.onStart();
        easytracker.getInstance(this).activityStart(this);
    }

    @Override
    public onStop(){
        super.onStop();
        easytracker.getInstance(this).activityStop(this);
    }
    @Override
    protected void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
