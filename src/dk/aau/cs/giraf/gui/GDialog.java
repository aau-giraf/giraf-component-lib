package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GDialog extends Dialog {
	
	private final GDialog mDialog;
    private final View.OnClickListener _task;
	
	public GDialog(Context context, int drawable, String headline, String text, android.view.View.OnClickListener task) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setContent(drawable, headline, text);

        mDialog = this;
        _task = task;

        setListeners();
	}

    public GDialog(Context context, String headline, String text, View.OnClickListener task)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setContent(-1, headline, text);

        mDialog = this;
        _task = task;

        setListeners();
    }

    public GDialog(Context context, int drawable, String headline, View.OnClickListener task)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setContent(drawable, headline, "");

        mDialog = this;
        _task = task;

        setListeners();
    }

    public GDialog(Context context, String headline, View.OnClickListener task)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setContent(-1, headline, "");

        mDialog = this;
        _task = task;

        setListeners();
    }

    private void setListeners()
    {
        this.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDialog.cancel();
            }
        });

        this.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void setContent(int thumb, String headline, String description) {
        this.setContentView(R.layout.gdialog_layout);

        //Set the icon
        if (thumb >= 0)
        {
            ImageView thumb_view = (ImageView) this.findViewById(R.id.content_thumb);
            thumb_view.setBackgroundResource(thumb);
        }

        //set the descriptive text
        TextView description_txt = (TextView) this.findViewById(R.id.dialog_description);
        description_txt.setText(description);
        this.setStyle();

        //Set the header text
        TextView headline_txt = (TextView) this.findViewById(R.id.dialog_headline);
        headline_txt.setText(headline);
    }

    /*
     * Description: Sets the style of a dialog box. The dialog view is retrieved from the ID in XML.
     * The style is set using a new drawable (gradient) which replaces the old dialog window. This allows for
     * dynamic setting of colors, borders and what not.
     */
    public void setStyle()
    {

        //default colors
        int[] colors = new int[2];
        colors = GStyler.getColors(GStyler.dialogBoxBaseColor);


        //Set the properties for the new drawable
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gd.setCornerRadius(10);
        gd.setStroke(2, GStyler.calculateGradientColor(colors[0], .46f), 0f, 0f);

        //Get the view from the XML definition
        View v = findViewById(R.id.GDialog);
        v.setBackgroundDrawable(gd);


    }

}
