package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class GTooltip {

    private final int x = 0;
    private final int y = 1;

    private final View layout;
    private final View anchorTo;
    private final Context context;

    private Paint arrow;
    private Path arrowPath;

    private boolean isShown = true;
    private Point tooltipDistanceVector = new Point(10,10);

    private int height = -1;
    private int width = -1;

    public GTooltip(View anchorTo)
    {
        this.anchorTo = anchorTo;
        this.context = anchorTo.getContext();
        layout = LayoutInflater.from(context).inflate(R.layout.gtooltip_layout, null);
        ((ViewGroup)anchorTo.getRootView()).addView(layout);

        //Get frame sizes and do smart anchoring every draw
        layout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                RelativeLayout frame = (RelativeLayout) layout.findViewById(R.id.GTooltip_Frame);
                SetSize(frame.getWidth(), frame.getHeight());
                SmartAnchor();
                return true;
            }
        });

        this.Hide();
    }

    public GTooltip(View anchorTo, View content)
    {
        this(anchorTo);
        SetView(content);
    }

    public void SetView(View content)
    {
        RelativeLayout contentHolder = (RelativeLayout) layout.findViewById(R.id.GTooltip_ContentHolder);
        contentHolder.addView(content);

        SetStyle();
        SetListeners();
    }

    public boolean IsShown()
    {
        return isShown;
    }

    public void Show()
    {
        if (!this.IsShown())
        {
            layout.setVisibility(View.VISIBLE);
            this.isShown = true;
        }
    }

    public void Hide()
    {
        if (this.IsShown())
        {
            layout.setVisibility(View.GONE);
            this.isShown = false;
        }
    }

    public Context GetContext()
    {
        return this.context;
    }

    public void SetDistanceToAnchor(Point p)
    {
        this.tooltipDistanceVector = p;
    }

    private void SetListeners()
    {
        //Set out of tooltip click
        RelativeLayout outOfTooltip = (RelativeLayout) layout.findViewById(R.id.GTooltip_OutOfTooltip);
        outOfTooltip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hide();
            }
        });
    }

    private void SetSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    private void SmartAnchor()
    {
        RelativeLayout frame = (RelativeLayout) layout.findViewById(R.id.GTooltip_Frame);

        //Get region of anchor
        ScreenRegion region = GetAnchorScreenRegion();

        //Place tooltip
        Point framePoint = CalculateFramePoint(frame, region);
        frame.setX(framePoint.x);
        frame.setY(framePoint.y);

        //Place arrow in proper corner
        PlaceArrow(region);
    }

    private void PlaceArrow(ScreenRegion region)
    {
        //Create arrow if it doesn't exist
        if (arrow == null)
        {
            PointSet PSArrow = new PointSet();
            PointSet PSStrokeRemover = new PointSet();

            GetPoints(region, PSArrow, PSStrokeRemover);

            SetArrow(PSArrow);

            RemoveInsideStroke(PSStrokeRemover);
        }
    }

    private void RemoveInsideStroke(PointSet PSStrokeRemover)
    {
        View contentHolder = layout.findViewById(R.id.GTooltip_ContentHolder);
        Drawable d = contentHolder.getBackground();

        Bitmap bm = Bitmap.createBitmap(contentHolder.getWidth(), contentHolder.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas contentCanvas = new Canvas(bm);

        d.setBounds(0,0, contentHolder.getWidth(), contentHolder.getHeight());
        d.draw(contentCanvas);

        //White triangle to override inside stroke
        Paint strokeRemover = new Paint();
        strokeRemover.setColor(Color.WHITE);
        strokeRemover.setStrokeWidth(5);
        strokeRemover.setStyle(Paint.Style.FILL);
        strokeRemover.setAntiAlias(true);

        //Path to follow
        Path strokeRemoverPath = new Path();
        strokeRemoverPath.setFillType(Path.FillType.EVEN_ODD);
        strokeRemoverPath.moveTo(PSStrokeRemover.A.x, PSStrokeRemover.A.y);
        strokeRemoverPath.lineTo(PSStrokeRemover.B.x, PSStrokeRemover.B.y);
        strokeRemoverPath.lineTo(PSStrokeRemover.D.x, PSStrokeRemover.D.y);
        strokeRemoverPath.lineTo(PSStrokeRemover.C.x, PSStrokeRemover.C.y);
        strokeRemoverPath.lineTo(PSStrokeRemover.A.x, PSStrokeRemover.A.y);
        strokeRemoverPath.close();

        contentCanvas.drawPath(strokeRemoverPath, strokeRemover);
        contentHolder.setBackgroundDrawable(new BitmapDrawable(bm));
    }

    private void SetArrow(PointSet PSArrow)
    {
        Bitmap bitmap = Bitmap.createBitmap(layout.findViewById(R.id.GTooltip_Frame).getWidth(), layout.findViewById(R.id.GTooltip_Frame).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //The arrow
        arrow = new Paint();
        arrow.setColor(GStyler.tooltipBaseColor);
        arrow.setStyle(Paint.Style.FILL);
        arrow.setAntiAlias(true);

        //The stroke
        Paint stroker = new Paint();
        stroker.setColor(GStyler.calculateGradientColor(GStyler.tooltipBaseColor));
        stroker.setStrokeWidth(5);
        stroker.setStyle(Paint.Style.STROKE);
        stroker.setAntiAlias(true);

        //Path to draw
        arrowPath = new Path();
        arrowPath.setFillType(Path.FillType.EVEN_ODD);
        arrowPath.moveTo(PSArrow.A.x,PSArrow.A.y);
        arrowPath.lineTo(PSArrow.B.x, PSArrow.B.y);
        arrowPath.lineTo(PSArrow.C.x, PSArrow.C.y);
        arrowPath.lineTo(PSArrow.A.x, PSArrow.A.y);
        arrowPath.close();

        //Draw things
        canvas.drawPath(arrowPath, arrow);
        canvas.drawPath(arrowPath, stroker);

        //Apply
        layout.findViewById(R.id.GTooltip_Frame).setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    private void GetPoints(ScreenRegion region, PointSet arrow, PointSet strokeRemover)
    {
        RelativeLayout frame = (RelativeLayout) layout.findViewById(R.id.GTooltip_Frame);
        RelativeLayout contentHolder = (RelativeLayout) layout.findViewById(R.id.GTooltip_ContentHolder);

        int fw = frame.getWidth();
        int fh = frame.getHeight();
        int chw = contentHolder.getWidth();
        int chh = contentHolder.getHeight();

        //Set points
        if (region == ScreenRegion.TOPLEFT)
        {
            arrow.A.set(3, 3);
            arrow.B.set(20, 60);
            arrow.C.set(60, 20);

            strokeRemover.A.set(-15, -15);
            strokeRemover.B.set(5, 45);
            strokeRemover.C.set(45, 5);
            strokeRemover.D.set(7, 7);
        }
        else if (region == ScreenRegion.TOP)
        {
            arrow.A.set(fw / 2, 3);
            arrow.B.set(fw / 2 - 20, 25);
            arrow.C.set(fw / 2 + 20, 25);

            strokeRemover.A.set(chw / 2, -13);
            strokeRemover.B.set(chw / 2 - 20, 8);
            strokeRemover.C.set(chw / 2 + 20, 8);
            strokeRemover.D.set(strokeRemover.A.x, strokeRemover.B.y);
        }
        else if (region == ScreenRegion.TOPRIGHT)
        {
            arrow.A.set(fw - 3, 3);
            arrow.B.set(fw - 20, 60);
            arrow.C.set(fw - 60, 20);

            strokeRemover.A.set(chw + 15, -15);
            strokeRemover.B.set(chw - 5, 45);
            strokeRemover.C.set(chw - 45, 5);
            strokeRemover.D.set(chw - 7, 7);
        }
        else if (region == ScreenRegion.BOTTOMLEFT)
        {
            arrow.A.set(3, fh - 3);
            arrow.B.set(20, fh - 60);
            arrow.C.set(60, fh - 20);

            strokeRemover.A.set(-15, chh + 15);
            strokeRemover.B.set(5, chh - 45);
            strokeRemover.C.set(45, chh - 5);
            strokeRemover.D.set(7, chh - 7);
        }
        else if (region == ScreenRegion.BOTTOM)
        {
            arrow.A.set(fw / 2, fh - 3);
            arrow.B.set(fw / 2 - 20, fh - 25);
            arrow.C.set(fw / 2 + 20, fh - 25);

            strokeRemover.A.set(chw / 2, chh + 13);
            strokeRemover.B.set(chw / 2 - 20, chh - 8);
            strokeRemover.C.set(chw / 2 + 20, chh - 8);
            strokeRemover.D.set(strokeRemover.A.x, strokeRemover.B.y);
        }
        else
        {
            arrow.A.set(fw - 3, fh - 3);
            arrow.B.set(fw - 20, fh - 60);
            arrow.C.set(fw - 60, fh - 20);

            strokeRemover.A.set(chw + 15, chh + 15);
            strokeRemover.B.set(chw - 5, chh - 45);
            strokeRemover.C.set(chw - 45, chh - 5);
            strokeRemover.D.set(chw - 7, chh - 7);
        }
    }

    /**
     * Calculates the point of a frame given the ScreenRegion
     * @param region - The region of the frame to be the anchor
     * @return - Calculated region point of the frame
     */
    private Point CalculateFramePoint(RelativeLayout frame, ScreenRegion region)
    {
        Point p = new Point();
        int[] realAnchor = new int[2];
        anchorTo.getLocationOnScreen(realAnchor);

        if (region == ScreenRegion.TOPLEFT)
        {
            //Calculate the TOPLEFT point of frame at a distance relative to anchorTo
            Point anchorBOTTOMRIGHT = new Point();
            anchorBOTTOMRIGHT.x = realAnchor[x] + anchorTo.getWidth();
            anchorBOTTOMRIGHT.y = realAnchor[y] + anchorTo.getHeight();
            p.set(anchorBOTTOMRIGHT.x + tooltipDistanceVector.x, anchorBOTTOMRIGHT.y + tooltipDistanceVector.y);
        }
        else if (region == ScreenRegion.TOPRIGHT)
        {
            //Calculate the TOPRIGHT point of frame at a distance relative to anchorTo
            Point anchorBOTTOMLEFT = new Point();
            anchorBOTTOMLEFT.x = realAnchor[x];
            anchorBOTTOMLEFT.y = realAnchor[y] + anchorTo.getHeight();
            p.set(anchorBOTTOMLEFT.x - tooltipDistanceVector.x, anchorBOTTOMLEFT.y + tooltipDistanceVector.y);
            p.x = p.x - this.width;
        }
        else if (region == ScreenRegion.BOTTOMRIGHT)
        {
            //Calculate the BOTTOMRIGHT point of a frame at a distance relative to anchorTo
            Point anchorTOPLEFT = new Point();
            anchorTOPLEFT.x = realAnchor[x];
            anchorTOPLEFT.y = realAnchor[y];
            p.set(anchorTOPLEFT.x - tooltipDistanceVector.x, anchorTOPLEFT.y - tooltipDistanceVector.y);
            p.x = p.x - this.width;
            p.y = p.y - this.height;
        }
        else if (region == ScreenRegion.BOTTOMLEFT)
        {
            //Calculate the BOTTOMLEFT point of a frame at a distance relative to anchorTo
            Point anchorTOPRIGHT = new Point();
            anchorTOPRIGHT.x = realAnchor[x] + anchorTo.getWidth();
            anchorTOPRIGHT.y = realAnchor[y];
            p.set(anchorTOPRIGHT.x + tooltipDistanceVector.x, anchorTOPRIGHT.y - tooltipDistanceVector.y);
            p.y = p.y - this.height;
        }
        else if (region == ScreenRegion.TOP)
        {
            //Calculate the TOP point of a frame at a distance relative to anchorTo
            Point anchorTOP = new Point();
            anchorTOP.x = realAnchor[x] + anchorTo.getWidth() / 2;
            anchorTOP.y = realAnchor[y] + anchorTo.getHeight();
            p.set(anchorTOP.x, anchorTOP.y + tooltipDistanceVector.y);
            p.x = p.x - this.width / 2;
        }
        else
        {
            //Calculate the BOTTOM point of a frame at a distance relative to anchorTo
            Point anchorBOTTOM = new Point();
            anchorBOTTOM.x = realAnchor[x] + anchorTo.getWidth() / 2;
            anchorBOTTOM.y = realAnchor[y];
            p.set(anchorBOTTOM.x, anchorBOTTOM.y - tooltipDistanceVector.y);
            p.x = p.x - this.width / 2;
            p.y = p.y - this.height;
        }

        return p;
    }

    /**
     * Gets the ScreenRegion of anchorTo
     * @return - ScreenRegion.
     */
    private ScreenRegion GetAnchorScreenRegion()
    {
        //Get size of screen
        WindowManager wm = (WindowManager) GetContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        //Calculate vertically centered reference points
        Point ref1 = new Point(screenSize.x / 3, screenSize.y / 2);
        Point ref2 = new Point((screenSize.x / 3) * 2, screenSize.y / 2);

        //Get anchor center
        Point p = new Point((int)(this.anchorTo.getX() + 0.5), (int)(this.anchorTo.getY() + 0.5));

        //Calculate screen region
        if (p.x < ref1.x && p.y < ref1.y)
            return ScreenRegion.TOPLEFT;
        else if (p.x < ref2.x && p.y < ref1.y)
            return ScreenRegion.TOP;
        else if (p.x > ref2.x && p.y < ref1.y)
            return ScreenRegion.TOPRIGHT;
        else if (p.x < ref1.x && p.y > ref1.y)
            return ScreenRegion.BOTTOMLEFT;
        else if (p.x < ref2.x && p.y > ref1.y)
            return ScreenRegion.BOTTOM;
        else
            return ScreenRegion.BOTTOMRIGHT;
    }

    private void SetStyle()
    {
        //Background of content holder
        RelativeLayout contentHolder = (RelativeLayout) layout.findViewById(R.id.GTooltip_ContentHolder);
        GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {GStyler.tooltipBaseColor, GStyler.tooltipBaseColor});
        d.setStroke(5, GStyler.calculateGradientColor(GStyler.tooltipBaseColor));
        d.setCornerRadius(10);
        contentHolder.setBackgroundDrawable(d);
    }

    private enum ScreenRegion {TOP, BOTTOM, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT}

    private class PointSet
    {
        public Point A = new Point();
        public Point B = new Point();
        public Point C = new Point();
        public Point D = new Point();
    }
}
