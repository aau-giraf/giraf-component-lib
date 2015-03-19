package dk.aau.cs.giraf.activity;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.R;
import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 17/03/15.
 */
public class GirafActivity extends FragmentActivity {

    private final float ACTION_BAR_SPACING = 5; // Spacing in the action bar
    private final float ACTION_BAR_TEXT_SIZE = 25; // Text size of the title in the action bar
    private ActionBar actionBar; // The action bar of the activity
    private RelativeLayout actionBarCustomView; // The custom action bar view
    private LeftActionBarLayout actionBarCustomViewLeft; // The left side of the action bar that can be customized
    private RightActionBarLayout actionBarCustomViewRight; // The right side of the action bar that can be customized
    private TextView actionBarTitleView; // The view to contain the title of the action bar

    // Constants to use when inserting buttons to the action bar
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    /**
     * Layout placed to the right in the action bar
     */
    private class RightActionBarLayout extends LinearLayout {

        public RightActionBarLayout(Context context) {
            super(context);
            init();
        }

        public RightActionBarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public RightActionBarLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            this.setGravity(Gravity.RIGHT);
        }

        /**
         * Adds a {@link dk.aau.cs.giraf.gui.GirafButton}
         *
         * @param girafButton a button to be added
         */
        public void addGirafButton(final GirafButton girafButton) {
            final LinearLayout.LayoutParams buttonParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonParams.rightMargin = (int) GirafScalingUtilities.convertDpToPixel(getContext(), ACTION_BAR_SPACING);
            this.addView(girafButton, buttonParams);
        }
    }

    /**
     * Layout placed to the left in the action bar
     */
    private class LeftActionBarLayout extends LinearLayout {

        public LeftActionBarLayout(Context context) {
            super(context);
        }

        public LeftActionBarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LeftActionBarLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        /**
         * Adds a {@link dk.aau.cs.giraf.gui.GirafButton}
         *
         * @param girafButton a button to be added
         */
        public void addGirafButton(final GirafButton girafButton) {
            final LinearLayout.LayoutParams buttonParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            buttonParams.leftMargin = (int) GirafScalingUtilities.convertDpToPixel(getContext(), ACTION_BAR_SPACING);
            this.addView(girafButton, buttonParams);
        }
    }

    /**
     * Creates the GirafActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch the action bar
        actionBar = this.getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false); // Hide the home action
            actionBar.setDisplayShowTitleEnabled(false); // Hide the title action
            actionBar.setCustomView(createActionBarView()); // Set the custom view to the action bar
            actionBar.setDisplayShowCustomEnabled(true); // Show the custom custom view of the action bar
        }
    }

    /**
     * * Adds an {@link dk.aau.cs.giraf.gui.GirafButton} to a side of the action bar
     *
     * @param girafButton the {@link dk.aau.cs.giraf.gui.GirafButton} to be added to the action bar
     * @param side        either LEFT or RIGHT
     */
    public void addGirafButtonToActionBar(GirafButton girafButton, int side) {

        if (actionBar == null) {
            throw new IllegalStateException("You cannot add a GirafButton to GirafActivity with \"GirafTheme.NoTitleBar\" use \"GirafTheme\" instead ");
        }

        if (side == LEFT) {
            actionBarCustomViewLeft.addGirafButton(girafButton);
        } else if (side == RIGHT) {
            actionBarCustomViewRight.addGirafButton(girafButton);
        } else {
            throw new IllegalArgumentException("You have to give LEFT or RIGHT side when adding a button to the actionbar");
        }

    }

    /**
     * Sets the title of the action bar
     *
     * @param title the title of the action bar
     */
    public void setActionBarTitle(String title) {
        actionBarTitleView.setText(title);
    }

    /**
     * Creates the view of the actionbar
     *
     * @return the custom view to be set in the action bar
     */
    private View createActionBarView() {
        // Create the actionBarLayout
        actionBarCustomView = new RelativeLayout(this);

        // Make the actionBarLayout match its parrents dimensions
        actionBarCustomView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );

        // Calculate the padding of the actionBarLayout
        final int padding = (int) GirafScalingUtilities.convertDpToPixel(this, ACTION_BAR_SPACING);

        // Set padding on actionBarLayout
        actionBarCustomView.setPadding(padding, padding, padding, padding);

        // Set the background of the actionBarLayout
        actionBarCustomView.setBackgroundResource(R.drawable.action_bar_background);

        // The backButton of the actionBarLayout
        final GirafButton backButton = new GirafButton(this, this.getResources().getDrawable(R.drawable.icon_back));

        backButton.setId(R.id.giraf_action_bar_back_button); // Set the ID of the backButton

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
        actionBarCustomView.addView(backButton, backButtonParams);

        // The titleView of the actionBarLayout
        actionBarTitleView = new TextView(this);

        // Set default title of the action bar
        String title;
        if (this.getTitle() != null) {
            title = this.getTitle().toString();
        } else if (this.getString(this.getApplicationInfo().labelRes) != null) {
            title = this.getString(this.getApplicationInfo().labelRes);
        } else {
            title = "Unknown title";
        }

        actionBarTitleView.setText(this.getTitle()); // Set the tile of the titleView
        actionBarTitleView.setTextSize(GirafScalingUtilities.convertDpToPixel(this, ACTION_BAR_TEXT_SIZE));
        actionBarTitleView.setGravity(Gravity.CENTER); // Set the text to be in the cetner of the titleView

        // The layout params for the titleView
        final RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Align the text in the center of the actionBarLayout
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        // Add the backButton to the actionBarLayout
        actionBarCustomView.addView(actionBarTitleView, titleParams);

        // Add the left customizable layout of the action bar
        actionBarCustomViewLeft = new LeftActionBarLayout(this);
        RelativeLayout.LayoutParams leftActionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        leftActionBarLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.giraf_action_bar_back_button);
        actionBarCustomView.addView(actionBarCustomViewLeft, leftActionBarLayoutParams);

        // Add the left customizable layout of the action bar
        actionBarCustomViewRight = new RightActionBarLayout(this);
        RelativeLayout.LayoutParams rightActionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rightActionBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        actionBarCustomView.addView(actionBarCustomViewRight, rightActionBarLayoutParams);

        return actionBarCustomView; // Return the actionBarLayout
    }

}
