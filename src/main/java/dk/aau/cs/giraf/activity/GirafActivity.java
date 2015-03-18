package dk.aau.cs.giraf.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.R;
import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 17/03/15.
 */
public class GirafActivity extends FragmentActivity {

    private final int ACTION_BAR_PADDING = 5;
    private final int ACTION_BAR_TEXT_SIZE = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayShowHomeEnabled(false); // Hide the home action
        actionBar.setDisplayShowTitleEnabled(false); // Hide the title action
        actionBar.setCustomView(createActionBarView()); // Set the custom view to the action bar
        actionBar.setDisplayShowCustomEnabled(true); // Show the custom custom view of the action bar
    }

    /**
     * Creates the view of the actionbar
     *
     * @return the custom view to be set in the action bar
     */
    private View createActionBarView() {
        // Create the actionBarLayout
        final RelativeLayout actionBarLayout = new RelativeLayout(this);

        // Make the actionBarLayout match its parrents dimensions
        actionBarLayout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );

        // Calculate the padding of the actionBarLayout
        final int padding = (int) GirafScalingUtilities.convertDpToPixel(this, ACTION_BAR_PADDING);

        // Set padding on actionBarLayout
        actionBarLayout.setPadding(padding, padding, padding, padding);

        // Set the background of the actionBarLayout
        actionBarLayout.setBackgroundResource(R.drawable.action_bar_background);

        // The backButton of the actionBarLayout
        final GirafButton backButton = new GirafButton(this, this.getResources().getDrawable(R.drawable.icon_back));

        // Finish the activity when clicking the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GirafActivity.this.finish();
            }
        });

        // The layout params for the backButton pararms
        final RelativeLayout.LayoutParams backButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Align the button to the left of the parrent
        backButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        // Add the backButton to the actionBarLayout
        actionBarLayout.addView(backButton, backButtonParams);

        // The titleView of the actionBarLayout
        final TextView titleView = new TextView(this);

        String title;

        if(false) { // Check for custom titleView

        }
        else if(this.getTitle() != null) {
            title = this.getTitle().toString();
        } else if(this.getString(this.getApplicationInfo().labelRes) != null) {
            title = this.getString(this.getApplicationInfo().labelRes);
        } else {
            title = "Unknown title";
        }


        titleView.setText(this.getTitle()); // Set the tile of the titleView
        titleView.setTextSize(GirafScalingUtilities.convertDpToPixel(this, ACTION_BAR_TEXT_SIZE));
        titleView.setGravity(Gravity.CENTER); // Set the text to be in the cetner of the titleView

        // The layout params for the titleView
        final RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Align the text in the center of the actionBarLayout
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);


        // Add the backButton to the actionBarLayout
        actionBarLayout.addView(titleView, titleParams);

        return actionBarLayout; // Return the actionBarLayout
    }

}
