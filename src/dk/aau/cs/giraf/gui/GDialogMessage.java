package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Malakahh on 3/24/14.
 */

public class GDialogMessage extends GDialog
{

    private final GDialogMessage mDialog;
    private final View.OnClickListener _task;

    public GDialogMessage(Context context, int drawable, String headline, String text, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(drawable, headline, text);
        setListeners();
    }

    public GDialogMessage(Context context, String headline, String text, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(-1, headline, text);
        setListeners();
    }

    public GDialogMessage(Context context, int drawable, String headline, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(drawable, headline, "");
        setListeners();
    }

    public GDialogMessage(Context context, String headline, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(-1, headline, "");
        setListeners();
    }

    private void setListeners()
    {
        this.findViewById(R.id.GDialogMessage_cancelBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDialog.cancel();
            }
        });

        this.findViewById(R.id.GDialogMessage_okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_task != null)
                    _task.onClick(v);

                mDialog.dismiss();
            }
        });
    }

    /**
     * description: sets the contents of a dialog box
     * @param thumb - The icon shown in the dialog box
     * @param headline - the headline text
     * @param description - further text describing the context of the dialogbox
     */

    public void setContent(int thumb, String headline, String description)
    {
        View layout = LayoutInflater.from(this.getContext()).inflate(R.layout.gdialogmessage_layout, null);

        //Set the icon
        if (thumb >= 0)
        {
            ImageView thumb_view = (ImageView) layout.findViewById(R.id.GDialogMessage_image);
            thumb_view.setBackgroundResource(thumb);
        }

        //set the descriptive text
        TextView description_txt = (TextView) layout.findViewById(R.id.GDialogMessage_description);
        description_txt.setText(description);

        //Set the header text
        TextView headline_txt = (TextView) layout.findViewById(R.id.GDialogMessage_headline);
        headline_txt.setText(headline);

        this.SetView(layout);
    }
}
