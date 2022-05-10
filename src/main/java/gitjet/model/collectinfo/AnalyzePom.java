package gitjet.model.collectinfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyzePom {

    public static Set<String> getDependencies(File file) throws IOException {
        File pom = new File(file + File.separator + "pom.xml");
        try (BufferedReader reader = new BufferedReader(new FileReader(pom))) {
            boolean dependenciesOpened = false;
            String line;
            Set<String> dependencies = new HashSet<>();

            while (((line = reader.readLine()) != null) && !line.contains("</dependencies>")) {

                if (line.contains("<dependencies>")) {
                    dependenciesOpened = true;
                }

                if (dependenciesOpened && line.contains("<groupId>")) {
                    dependencies.add(line.replace("<groupId>", "").replace("</groupId>", "").trim());
                }
            }

            System.out.println("Dependencies: " + dependencies);
            return dependencies;
        }
    }

}
