package dk.aau.cs.giraf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import dk.aau.cs.giraf.gui.R;

/**
 * Created on 30/04/2015.
 */
public class GirafBugSplashActivity extends GirafActivity {

    public static final String EXCEPTION_MESSAGE_TAG = "EXCEPTION_MESSAGE_TAG";
    public static final String EXCEPTION_STACKTRACE_TAG = "EXCEPTION_STACKTRACE_TAG";

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
    }

    @Override
    public void onBackPressed() {
        // When the back-button is pressed, stop this activity and launch the systems default launcher
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
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
