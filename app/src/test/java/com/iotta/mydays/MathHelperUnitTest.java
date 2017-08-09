package com.iotta.mydays;

import com.iotta.mydays.viewpresenter.view.MathHelper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MathHelperUnitTest {
    @Test
    public void progressAngle() throws Exception {
        assertEquals(MathHelper.convertAngleToProgress(360, 31), 31);
        assertEquals(MathHelper.convertAngleToProgress(0, 28), 0);
        assertEquals(MathHelper.convertAngleToProgress(90, 30), 8);
    }
}