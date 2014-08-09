package pwalch.net.opensms.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by pierre on 09/08/14.
 */
public class InternalStorage {
    protected static void writeToFile(File file, String textToWrite)
            throws IOException {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(textToWrite);
        writer.close();
    }
}
