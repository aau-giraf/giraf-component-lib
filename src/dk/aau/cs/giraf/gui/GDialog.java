package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Malakahh & JLY on 3/21/14.
 */
public class GDialog extends Dialog {

    GDialog thisDialog;
    RelativeLayout shade;
    boolean shadeIsReady = false; //check whether shade is instantiated to see if backgroundCancelDialog() can be called.

    public GDialog(Context context, View content)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        thisDialog = this;
        SetView(content);
    }

    public GDialog(Context context)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        thisDialog = this;
    }

    public void SetView(View content)
    {
        View layout = LayoutInflater.from(this.getContext()).inflate(R.layout.gdialog_layout, null);
        //Add content
        RelativeLayout wrapper = (RelativeLayout) layout.findViewById(R.id.GDialog_ViewWrapper);
        wrapper.addView(content);
        //Styling
        SetSyle(layout);

        this.setContentView(layout);
    }

    private void SetSyle(View layout)
    {
        //Background of dialog
        RelativeLayout background = (RelativeLayout) layout.findViewById(R.id.GDialog_Background);

        GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {GStyler.dialogBackgroundColor, GStyler.dialogBackgroundColor});
        d.setCornerRadius(10);

        d.setStroke(5, GStyler.calculateGradientColor(GStyler.dialogBorderColor));

        background.setBackgroundDrawable(d);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           }
        });
        //Shade
        shade = (RelativeLayout) layout.findViewById(R.id.GDialog_Shade);
        shadeIsReady = true;
        shade.setBackgroundColor(GStyler.dialogShadeColor);
    }


    public void backgroundCancelsDialog(Boolean flag) throws Exception
    {
        if(shadeIsReady == true)
        {
             if(flag == true)
             {
                 shade.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               thisDialog.cancel();
           }
       });}
             else if(flag == false)
             {
                 shade.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {}
                 });
             }
        }
        else
        {
            throw new Exception("You need to instantiate the dialog before using this function.");
        }
    }
}

