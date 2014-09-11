package pwalch.net.opensms.storage;

import android.content.Context;
import android.test.ActivityTestCase;

/**
 * Created by pierre on 11.09.14.
 */
public class BaseTest extends ActivityTestCase {

    public Context findContext() {
        return this.getInstrumentation().getTargetContext().getApplicationContext();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        new InternalStorage(findContext()).resetAppFolder();
    }
}
