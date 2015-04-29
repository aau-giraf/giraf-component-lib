package dk.aau.cs.giraf.gui.test.gui;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.R;

/**
 * Created on 29/04/2015.
 */
public class GirafButtonTest extends ApplicationTestCase<Application> {

    private String buttonText;
    private Drawable buttonIcon;

    public GirafButtonTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        buttonText = "button";
        buttonIcon = getContext().getResources().getDrawable(R.drawable.icon_copy);
    }

    // Notice: This test will only fail if the buttons constructor throws an exception
    public void testGirafButtonConstructorTextOnly() {
        GirafButton button = new GirafButton(getContext(), buttonText);

        Assert.assertNotNull(button);
    }

    // Notice: This test will only fail if the buttons constructor throws an exception
    public void testGirafButtonConstructorIconOnly() {
        GirafButton button = new GirafButton(getContext(), buttonIcon);

        Assert.assertNotNull(button);
    }

    // Notice: This test will only fail if the buttons constructor throws an exception
    public void testGirafButtonConstructorIconAndText() {
        GirafButton button = new GirafButton(getContext(), buttonIcon, buttonText);

        Assert.assertNotNull(button);
    }

    public void testGirafButtonCheckedChanges() {
        final boolean checked = true;
        GirafButton button = new GirafButton(getContext(), buttonIcon, buttonText);

        // Set the checked status of the button
        button.setChecked(checked);

        Assert.assertEquals(checked, button.isChecked());
    }

    public void testGirafButtonCheckedFalseDefault() {
        final boolean checked = false;
        GirafButton button = new GirafButton(getContext(), buttonIcon, buttonText);

        Assert.assertEquals(checked, button.isChecked());
    }

    public void testGirafButtonToggleChecked() {
        GirafButton button = new GirafButton(getContext(), buttonIcon, buttonText);

        // Toggle the button
        button.setChecked(true);
        button.toggle();

        // The button should not be checked at this point
        Assert.assertEquals(false, button.isChecked());

        // Toggle the button back

        button.toggle();

        // The button should be checked at this point
        Assert.assertEquals(true, button.isChecked());
    }

}
