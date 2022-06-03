package gitjet;

import gitjet.model.Errors;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static gitjet.model.Errors.DIRECTORY_ERROR;

/**
 * Utility functions.
 */
public class Utils {

    /**
     * Get file array containing all files in a directory (or a single file).
     *
     * @param file Directory or a file.
     * @return an array of files of the received directory.
     */
    public static File[] getFilesArray(File file) {
        if (!file.isDirectory()) {
            return new File[]{file};
        }

        File[] filesList = file.listFiles();
        if (filesList == null) {
            throw new IllegalArgumentException(DIRECTORY_ERROR.getMessage());
        }

        return filesList;
    }

    /**
     * Check if a string is a correct URL link.
     *
     * @param text String to check.
     * @return Result of check.
     */
    public static boolean isLink(String text) {
        return Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)").matcher(text).find();
    }

    /**
     * Check if a string is a number.
     *
     * @param text String to check.
     * @return Result of check.
     */
    public static boolean isNumber(String text) {
        return Pattern.compile("^[0-9]+$").matcher(text).find();
    }

    /**
     * Delete all contents of a file.
     *
     * @param fileName File to be cleaned.
     */
    public static void cleanFile(String fileName) {
        try (Writer cleaner = new BufferedWriter(new FileWriter(fileName))) {
            cleaner.write("");
        } catch (IOException e) {
            String msg = "Couldn't open file " + fileName;
            WindowsUtils.createErrorWindow(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Get setting from settings file by its name.
     *
     * @param parameterName Name of setting.
     * @return Setting value for given name.
     */
    public static String getSetting(String parameterName) {
        try (BufferedReader br = new BufferedReader(new FileReader("settings.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> parts = Arrays.asList(line.split(getSettingValueSeparator()));
                if (Objects.equals(parts.get(0), parameterName)) {
                    return parts.get(1);
                }
            }
        } catch (IOException e) {
            WindowsUtils.createErrorWindow("Couldn't find setting " + parameterName);
            throw new IllegalStateException(Errors.SETTINGS_ERROR.getMessage());
        }
        return null;
    }

    /**
     * settingValueSeparator getter.
     *
     * @return Separator in settings file constant.
     */
    public static String getSettingValueSeparator() {
        return " %: ";
    }
}
