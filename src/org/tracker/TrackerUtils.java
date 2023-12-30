package org.tracker;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackerUtils {

    public static String createFileWithNumber(String fileName, String path) {
        String fileNameWithPrefix = fileName + ".csv";

        File file = new File(path + fileNameWithPrefix);

        if (file.exists()) {
            File[] files = new File(path).listFiles((dir, name) ->
                    name.matches("^" + fileName + " \\(" + "(\\d+)\\" + ")" + ".csv$"));
            int maxNumber = 0;

            if (files != null) {
                for (File f : files) {
                    Matcher matcher = Pattern.compile("^" + fileName + " \\(" + "(\\d+)\\" + ")" + ".csv$")
                            .matcher(f.getName());

                    if (matcher.find()) {
                        int number = Integer.parseInt(matcher.group(1));
                        maxNumber = Math.max(maxNumber, number);
                    }
                }
            }

            return fileName + " (" + (maxNumber + 1) + ").csv";

        } else {
            return fileNameWithPrefix;
        }
    }
}
