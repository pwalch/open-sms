package pwalch.net.opensms;

import android.app.Application;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import pwalch.net.opensms.storage.Storage;

/**
 * Created by pierre on 12.09.14.
 */
public class OpenSmsApplication extends Application {

    private Storage mStorage;

    public void onCreate() {
        super.onCreate();

        try {
            mStorage = new Storage(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Storage getStorage() {
        return mStorage;
    }
}
