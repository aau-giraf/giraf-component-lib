package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GTooltip extends RelativeLayout {

    private String text = "";

    public GTooltip(Context context, String text)
    {
        super(context);
        this.text = text;

        this.Init();
    }

    public GTooltip(Context context, int resID)
    {
        this(context, context.getString(resID));
    }

    private void Init()
    {
        /*
        TextView tv = (TextView) findViewById(R.id.GTooltip_Text);
        tv.setText(this.text);
        */
    }

    public void Show()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.gtooltip_layout, null);
    }

    /*
    public GTooltip(Context context, AttributeSet attrs, )
    {}

    public GTooltip(Context context, AttributeSet attrs, int defStyleAttr)
    {}
    */


}
