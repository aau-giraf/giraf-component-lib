package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Malakahh on 3/25/14.
 */
public class GDialogAlert extends GDialog {

    private final GDialogAlert mDialog;
    private final View.OnClickListener _task;

    public GDialogAlert(Context context, int drawable, String headline, String text, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(drawable, headline, text);
        setListener();
    }

    public GDialogAlert(Context context, String headline, String text, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(-1, headline, text);
        setListener();
    }

    public GDialogAlert(Context context, int drawable, String headline, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(drawable, headline, "");
        setListener();
    }

    public GDialogAlert(Context context, String headline, View.OnClickListener task)
    {
        super(context);
        mDialog = this;
        _task = task;

        setContent(-1, headline, "");
        setListener();
    }

    private void setContent(int img, String headline, String description)
    {
        View layout = LayoutInflater.from(this.getContext()).inflate(R.layout.gdialogalert_layout, null);

        //Set the icon
        if (img >= 0)
        {
            ImageView imgView = (ImageView) layout.findViewById(R.id.GDialogAlert_image);
            imgView.setBackgroundResource(img);
        }

        //Set the descriptive text
        TextView txt = (TextView) layout.findViewById(R.id.GDialogAlert_description);
        txt.setText(description);

        //Set the header text
        TextView htxt = (TextView) layout.findViewById(R.id.GDialogAlert_headline);
        htxt.setText(headline);

        this.SetView(layout);
    }

    private void setListener()
    {
        this.findViewById(R.id.GDialogAlert_okBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (_task != null)
                    _task.onClick(v);

                mDialog.dismiss();
            }
        });
    }
}
