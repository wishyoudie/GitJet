package gitjet.model;

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
    private static DataWriter instance;

    /**
     * Get singleton instance.
     *
     * @return Instance.
     */
    public static synchronized DataWriter getInstance() {
        if (instance == null) {
            instance = new DataWriter();
        }
        return instance;
    }

    /**
     * Write or append specified text to storage.
     *
     * @param text   Text to write/append.
     * @param append Flag to indicate whether to write or append.
     */
    public synchronized void write(String text, boolean append) throws IOException {
        try (Writer writer = new BufferedWriter(new FileWriter(Objects.requireNonNull(getSetting("storage")), append))) {
            writer.append(text).append(System.lineSeparator());
        } catch (IOException e) {
            throw new IOException(Errors.DATA_ERROR.getMessage());
        }
    }
}
