package gitjet.model;

import gitjet.WindowsUtils;

import static gitjet.Utils.getSetting;

import java.io.*;
import java.util.Objects;

/**
 * Write data to storage class. Thread-safe by singleton pattern.
 */
public class DataWriter {

    /**
     * Single instance of DataWriter class.
     */
    private static volatile DataWriter instance;

    /**
     * Get singleton instance.
     *
     * @return Instance.
     */
    public static synchronized DataWriter getInstance() {
        DataWriter localInstance = instance;
        if (localInstance == null) {
            synchronized (DataWriter.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DataWriter();
                }
            }
        }
        return localInstance;
    }

    /**
     * Write or append specified text to storage.
     *
     * @param text   Text to write/append.
     * @param append Flag to indicate whether to write or append.
     */
    public void write(String text, boolean append) {
        try (Writer writer = new BufferedWriter(new FileWriter(Objects.requireNonNull(getSetting("storage")), append))) {
            writer.append(text).append(System.lineSeparator());
        } catch (IOException e) {
            WindowsUtils.createErrorWindow(Errors.DATA_ERROR.getMessage());
        }
    }
}
