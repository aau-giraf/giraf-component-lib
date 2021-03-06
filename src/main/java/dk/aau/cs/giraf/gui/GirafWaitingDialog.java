package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by rasmusholmjensen on 24/04/15.
 */
public class GirafWaitingDialog extends GirafDialog {

    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String METHOD_ID_TAG = "METHOD_ID_TAG";

    public static GirafWaitingDialog newInstance(String title, String description) {
        GirafWaitingDialog girafWaitingDialog = new GirafWaitingDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundel
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);

        // Store the bundle
        girafWaitingDialog.setArguments(args);
        return girafWaitingDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        final Bundle args = this.getArguments();
        setTitle(args.getString(TITLE_TAG, ""));
        setDescription(args.getString(DESCRIPTION_TAG, ""));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        RelativeLayout progressBar = (RelativeLayout) inflater.inflate(R.layout.giraf_waiting_dialog_custom_view, null);
        setCustomView(progressBar);

        // Make the dialog unable to be cancelled
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        return dialog;
    }
}
