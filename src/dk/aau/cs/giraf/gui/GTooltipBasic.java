package dk.aau.cs.giraf.gui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GTooltipBasic extends GTooltip {

    private boolean hasImage = false;
    private int imageMargin = -1;
    private int imageLines = -1;

    private final View content;

    public GTooltipBasic(View anchorTo)
    {
        super(anchorTo);

        //Inflate the gtooltipbasic layout
        content = LayoutInflater.from(GetContext()).inflate(R.layout.gtooltipbasic_layout, null);
        SetView(content);

        SetListeners();
        SetStyle();
    }

    public GTooltipBasic(View anchorTo, String text)
    {
        this(anchorTo);

        SetText(text);
    }

    public GTooltipBasic(View anchorTo, String text, Drawable img)
    {
        this(anchorTo, text);

        SetDrawable(img);
    }

    public GTooltipBasic(View anchorTo, String text, int imgResID)
    {
        this(anchorTo, text, anchorTo.getContext().getResources().getDrawable(imgResID));
    }

    /**
     * Sets the text of the tooltip, and rescales the tooltip to fit.
     * @param text
     */
    public void SetText(String text) {
        GTextView textView = (GTextView) content.findViewById(R.id.GTooltip_Text);
        if (!hasImage)
        {
            textView.setMinWidth(0);
            textView.setText(text);
        }
        else
        {
            //Set a leading span to the text,
            //resulting in an area to place the image
            SpannableString str = new SpannableString(text);
            str.setSpan(new LeadingMarginBox(imageLines, imageMargin), 0, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.setText(str);

            //Calculate text width
            Rect rect = new Rect();
            textView.getPaint().getTextBounds(str.toString(), 0, str.length(), rect);
            int w = imageMargin+rect.width();

            textView.setMinWidth(w > 400 ? 400 : w);
        }
    }

    /**
     * Sets the image of the tooltip, and rescales the tooltip to fit. Call this with parameter null to remove image.
     * @param img
     */
    public void SetDrawable(Drawable img)
    {
        ImageView imgView = (ImageView) content.findViewById(R.id.GTooltip_Image);
        GTextView textView = (GTextView) content.findViewById(R.id.GTooltip_Text);

        if (img == null)
        {
            //Remove image
            hasImage = false;
            imageMargin = -1;
            imageLines = -1;
            imgView.setImageDrawable(null);

            //Remove leading span
            this.SetText(textView.getText().toString());
        }
        else
        {
            //Add image
            hasImage = true;
            imageMargin = img.getIntrinsicWidth() + 5;
            imageLines = (int)(img.getIntrinsicHeight() / textView.getTextSize() + 0.5);
            imgView.setImageDrawable(img);

            String text = textView.getText().toString();

            if (text == null || text == "")
            {
                //Center in parent
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                imgView.setLayoutParams(params);
            }
            else
            {
                //Add leading span
                this.SetText(text);
            }
        }
    }

    private void SetListeners()
    {
        //Register imageview for movement
        GTextView textView = (GTextView) content.findViewById(R.id.GTooltip_Text);
        textView.SetOnScrollListener(new GTextView.OnScrollListener() {
            @Override
            public void onScroll(View v) {
                ImageView imgView = (ImageView) content.findViewById(R.id.GTooltip_Image);
                imgView.setScrollY(v.getScrollY());
            }
        });
    }

    private void SetStyle()
    {
        //Create bounds for text
        GTextView textView = (GTextView) content.findViewById(R.id.GTooltip_Text);
        textView.setMaxWidth(400);
        textView.setMaxHeight(300);
    }

    /**
     * Used for creating the inwards margin to account for the image. This enables the "wrapping around" the image.
     */
    private class LeadingMarginBox implements LeadingMarginSpan.LeadingMarginSpan2
    {
        private int lines;
        private int margin;

        public LeadingMarginBox(int lines, int margin)
        {
            this.lines = lines;
            this.margin = margin;
        }

        @Override
        public int getLeadingMargin(boolean first)
        {
            return first ? margin : 0;
        }

        @Override
        public int getLeadingMarginLineCount()
        {
            return lines;
        }

        //Override, because we have to. It's unused.
        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout)
        {}
    }
}
