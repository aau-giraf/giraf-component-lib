package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

/**
 * Created on 25/03/15.
 * <p></p>
 * @deprecated Old dialog use {@link dk.aau.cs.giraf.gui.GirafPopupDialog} instead.
 */
public class GirafConfirmDialog extends GirafDialog {

    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String METHOD_ID_TAG = "METHOD_ID_TAG";
    private static final String ACCEPT_TEXT = "ACCEPT_TEXT";
    private static final String DENY_TEXT = "DENY_TEXT";
    private static final String ACCEPT_ICON_RESOURCE = "ACCEPT_ICON_RESOURCE";
    private static final String DENY_ICON_RESOURCE = "DENY_ICON_RESOURCE";

    /**
     * An interface to perform the confirm action for a GirafConfirmDialog
     */
    public interface Confirmation {
        /**
         * Method for switching between methods called when pressing the acceptance buttons
         * @param dialogIdentifier identifier of the dialog
         */
        public void confirmDialog (int dialogIdentifier);
    }

    // An instance of the confirmation interface user
    Confirmation confirmation;

    /**
     * Creates a new instance of GirafConfirmDialog
     * @param title title of the dialog
     * @param description description of the dialog
     * @param methodID identifier of the method
     * @return a GirafConfirmDialog
     */
    public static GirafConfirmDialog newInstance(String title, String description, int methodID) {
        return GirafConfirmDialog.newInstance(title, description, methodID, null, -1, null, -1);
    }

    /**
     * Creates a new instance of GirafConfirmDialog
     * @param title title of the dialog
     * @param description description of the dialog
     * @param methodID identifier of the method
     * @param acceptText text for the acceptance button: null will use default string
     * @param acceptIconResource icon resource for the acceptance button: -1 will use default icon
     * @param denyText text fot the deny button: null will use default string
     * @param denyIconResource icon resource for the deny button: -1 will use default
     * @return a GirafConfirmDialog
     */
    public static GirafConfirmDialog newInstance(String title, String description, int methodID, String acceptText, int acceptIconResource, String denyText, int denyIconResource) {
        GirafConfirmDialog girafConfirmDialog = new GirafConfirmDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(METHOD_ID_TAG, methodID);
        args.putString(ACCEPT_TEXT, acceptText);
        args.putString(DENY_TEXT, denyText);
        args.putInt(ACCEPT_ICON_RESOURCE, acceptIconResource);
        args.putInt(DENY_ICON_RESOURCE, denyIconResource);

        // Store the bundle
        girafConfirmDialog.setArguments(args);
        return girafConfirmDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if the activity using the fragment implements the needed interface
        try {
            confirmation = (Confirmation) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Confirmation interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Fetch the dialog
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Fetch the arguments
        final Bundle args = this.getArguments();

        // Set the arguments
        setTitle(args.getString(TITLE_TAG, ""));
        setDescription(args.getString(DESCRIPTION_TAG, ""));

        // Create the yesButton
        String acceptText = args.getString(ACCEPT_TEXT, null);
        if(acceptText == null) {
            acceptText = this.getActivity().getResources().getString(R.string.giraf_confirm_dialog_yes_button_text);
        }

        int acceptIconResource = args.getInt(ACCEPT_ICON_RESOURCE,-1);

        if(acceptIconResource == -1) {
            acceptIconResource = R.drawable.icon_accept;
        }

        GirafButton yesButton = new GirafButton(getActivity(),getResources().getDrawable(acceptIconResource),acceptText);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GirafConfirmDialog.this.dismiss();
                confirmation.confirmDialog(args.getInt(METHOD_ID_TAG));
            }
        });

        // Create the noButton
        String denyText = args.getString(DENY_TEXT, null);
        if(denyText == null) {
            denyText = this.getActivity().getResources().getString(R.string.giraf_confirm_dialog_no_button_text);
        }

        int denyIconResource = args.getInt(DENY_ICON_RESOURCE,-1);

        if(denyIconResource == -1) {
            denyIconResource = R.drawable.icon_cancel;
        }

        GirafButton noButton = new GirafButton(getActivity(),getResources().getDrawable(denyIconResource),denyText);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GirafConfirmDialog.this.dismiss();
            }
        });

        // Add the buttons to the dialog
        addButton(noButton);
        addButton(yesButton);

        return dialog;
    }
}
