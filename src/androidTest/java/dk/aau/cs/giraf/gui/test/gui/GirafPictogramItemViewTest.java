package dk.aau.cs.giraf.gui.test.gui;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.test.ApplicationTestCase;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

import dk.aau.cs.giraf.dblib.models.BasicImageModel;
import dk.aau.cs.giraf.dblib.models.Pictogram;
import dk.aau.cs.giraf.gui.GirafPictogramItemView;
import dk.aau.cs.giraf.gui.R;

/**
 * Created on 29/04/2015.
 */
public class GirafPictogramItemViewTest extends ApplicationTestCase<Application> {

    private BasicImageModel imageModel;
    private GirafPictogramItemView view;

    private final int loadTimeout = 200;

    public GirafPictogramItemViewTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        imageModel = new Pictogram();
    }

    public void testSimpleConstructor() {
        view = new GirafPictogramItemView(getContext(), (AttributeSet) null);
    }

    public void testSimpleConstructorNullPictogram() {
        view = new GirafPictogramItemView(getContext(), imageModel);

        Assert.assertNotNull(view);
    }

    public void testSimpleConstructorActualPictogram() throws InterruptedException {
        // Instantiate variables used in test
        imageModel.setImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_copy));
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);

        // Give the UI-thread some time to load the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram was actually set.
        // Note that it is 'impossible' to check if the icon is the correct icon without comparing it visually
        Assert.assertNotNull(iconImageView);
        Assert.assertNotNull(iconImageView.getDrawable());
    }

    public void testSimpleConstructorWithTitle() {
        // Instantiate variables used in test
        final String title = "title";
        view = new GirafPictogramItemView(getContext(), imageModel, title);

        // Find the views in the inflated layout
        TextView textView = (TextView) view.findViewById(R.id.pictogram_title);

        // Test if the title was actually set.
        Assert.assertNotNull(textView);
        Assert.assertEquals(title, textView.getText());
    }

    public void testConstructorWithFallbackAndActualPictogram() throws InterruptedException {
        // Instantiate variables used in test
        Drawable fallback = getContext().getResources().getDrawable(R.drawable.icon_accept);
        imageModel.setImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_copy));
        view = new GirafPictogramItemView(getContext(), imageModel, fallback);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);

        // Give the UI-thread some time to load the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram was actually set.
        // Note that it is 'impossible' to check if the icon is the correct icon without comparing it visually
        Assert.assertNotNull(iconImageView);
        Assert.assertNotNull(iconImageView.getDrawable());
    }

    public void testConstructorWithFallbackAndEmptyPictogram() throws InterruptedException {
        // Instantiate variables used in test
        Drawable fallback = getContext().getResources().getDrawable(R.drawable.icon_accept);
        view = new GirafPictogramItemView(getContext(), imageModel, fallback);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);

        // Give the UI-thread some time to load the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram was actually set.
        // Note that it is 'impossible' to check if the icon is the correct icon without comparing it visually
        Assert.assertNotNull(iconImageView);
        Assert.assertNotNull(iconImageView.getDrawable());
    }

    public void testConstructorWithFallbackAndTitle() throws InterruptedException {
        // Instantiate variables used in test
        final String title = "title";
        Drawable fallback = getContext().getResources().getDrawable(R.drawable.icon_accept);
        view = new GirafPictogramItemView(getContext(), imageModel, fallback, title);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);
        TextView textView = (TextView) view.findViewById(R.id.pictogram_title);

        // Give the UI-thread some time to load the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram and title was actually set.
        // Note that it is 'impossible' to check if the icon is the correct icon without comparing it visually
        Assert.assertNotNull(iconImageView);
        Assert.assertNotNull(iconImageView.getDrawable());
        Assert.assertEquals(title, textView.getText());
    }

    public void testSetCheckedTrue() {
        // Instantiate variables used in test
        final boolean checked = true;
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Set the view to be checked
        view.setChecked(checked);

        // Test if the view was actually checked
        Assert.assertEquals(checked, view.isChecked());
    }

    public void testSetCheckedTrueThenCheckedFalse() {
        // Instantiate variables used in test
        final boolean checked = true;
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Set the view to be checked
        view.setChecked(checked);

        // Toggle the checked state (un-check)
        view.toggle();

        // Test if the view was actually unchecked again
        Assert.assertEquals(!checked, view.isChecked());
    }

    public void testCheckedDefaultFalse() {
        // Instantiate variables used in test
        final boolean checked = false;
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Test if the view is checked
        Assert.assertEquals(checked, view.isChecked());
    }

    public void testCheckedStateChangeStyle() {
        // Instantiate variables used in test
        final boolean checked = true;
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Find the original color of the background
        Drawable originalBackground = view.getBackground();

        // Set the view to be checked
        view.setChecked(checked);

        Drawable newBackground = view.getBackground();

        // Test if the two backgrounds are different
        Assert.assertNotSame(originalBackground, newBackground);
    }

    public void testResetPictogram() throws InterruptedException {
        // Instantiate variables used in test
        imageModel.setImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_copy));
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);

        // Give the UI-thread some time to load the pictogram
        Thread.sleep(loadTimeout);

        // Reset the pictogram view
        view.resetPictogramView();

        // Give the UI-thread some time to reset the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram was actually reset
        Assert.assertNotNull(iconImageView);
        Assert.assertNull(iconImageView.getDrawable());
    }

    public void testSetPictogramTwice() throws InterruptedException {
        // Instantiate variables used in test
        imageModel.setImage(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_copy));
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);

        // Set the pictogram twice (while setting the first, the other set will interrupt the first)
        view.setImageModel(imageModel);
        view.setImageModel(imageModel);

        // Give the UI-thread some time to reset the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram was actually reset
        Assert.assertNotNull(iconImageView);
        Assert.assertNotNull(iconImageView.getDrawable());
    }

    public void testSetPictogramWithNull() throws InterruptedException {
        // Instantiate variables used in test
        view = new GirafPictogramItemView(getContext(), imageModel);

        // Find the views in the inflated layout
        ImageView iconImageView = (ImageView) view.findViewById(R.id.pictogram_icon);

        // Try to set the image to null
        view.setImageModel(null);

        // Give the UI-thread some time to reset the pictogram
        Thread.sleep(loadTimeout);

        // Test if the pictogram was actually reset
        Assert.assertNotNull(iconImageView);
        Assert.assertNull(iconImageView.getDrawable());
    }
}
