package dk.aau.cs.giraf.gui.test.utilities;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 29/04/2015.
 */
public class GirafScalingUtilitiesTest extends ApplicationTestCase<Application> {

    public GirafScalingUtilitiesTest() {
        super(Application.class);
    }

    // Notice: This test will only fail if the convertDpToPixel method casts an exception
    public void testConvertDpToPixel() {
        final float dp = 100;

        final float result = GirafScalingUtilities.convertDpToPixel(getContext(), dp);

        Assert.assertNotNull(result);
    }

}
