package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by rasmusholmjensen on 25/03/15.
 */
public class GirafConfirmDialog extends GirafDialog {

    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String METHOD_ID_TAG = "METHOD_ID_TAG";

    /**
     * An interface to perform the confirm action for a GirafConfirmDialog
     */
    public interface Confirmation {
        /**
         * Method for swtiching between methods called when pressing the acceptance buttons
         * @param methodID identifier of the method
         */
        public void confirmDialog (int methodID);
    }

    // An instance of the confirmation interface user
    Confirmation confirmation;

    public static GirafConfirmDialog newInstance(String title, String description, int methodID) {
        GirafConfirmDialog girafConfirmDialog = new GirafConfirmDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundel
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(METHOD_ID_TAG, methodID);

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

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        final Bundle args = this.getArguments();
        setTitle(args.getString(TITLE_TAG, ""));
        setDescription(args.getString(DESCRIPTION_TAG, ""));

        GirafButton yesButton = new GirafButton(getActivity(),getResources().getDrawable(R.drawable.icon_accept),"Ja");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GirafConfirmDialog.this.dismiss();
                confirmation.confirmDialog(args.getInt(METHOD_ID_TAG));
            }
        });
        GirafButton noButton = new GirafButton(getActivity(),getResources().getDrawable(R.drawable.icon_cancel),"Nej");
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GirafConfirmDialog.this.dismiss();
            }
        });
        addButton(yesButton);
        addButton(noButton);
        return dialog;
    }
}
