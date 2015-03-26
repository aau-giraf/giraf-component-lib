package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

/**
 * Created by rasmusholmjensen on 26/03/15.
 */
public class GirafNotifyDialog extends GirafDialog {


    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String METHOD_ID_TAG = "METHOD_ID_TAG";

    /**
     * An interface to perform the confirm action for a GirafNotifyDialog
     */
    public interface Notification {
        /**
         * Method for swtiching between methods called when pressing the button
         * @param methodID identifier of the method
         */
        public void noticeDialog (int methodID);
    }

    // An instance of the confirmation interface user
    Notification notification;

    public static GirafNotifyDialog newInstance(String title, String description, int methodID) {
        GirafNotifyDialog girafNotifyDialog = new GirafNotifyDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundel
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(METHOD_ID_TAG, methodID);

        // Store the bundle
        girafNotifyDialog.setArguments(args);
        return girafNotifyDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if the activity using the fragment implements the needed interface
        try {
            notification = (Notification) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Notification interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        final Bundle args = this.getArguments();
        setTitle(args.getString(TITLE_TAG, ""));
        setDescription(args.getString(DESCRIPTION_TAG, ""));

        GirafButton confirmButton = new GirafButton(getActivity(),getResources().getDrawable(R.drawable.icon_accept),"Okay");
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GirafNotifyDialog.this.dismiss();
                notification.noticeDialog(args.getInt(METHOD_ID_TAG));
            }
        });

        addButton(confirmButton);
        return dialog;
    }
}
