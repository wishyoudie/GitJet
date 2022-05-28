package gitjet.model.collectinfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import gitjet.model.repository.RepositoriesHandler;

/**
 * A class to analyze pom.xml file of a repository.
 */
public class AnalyzePom {

    /**
     * Get dependencies of a repository.
     *
     * @param file Directory of a cloned repository.
     * @return Set of repository dependencies.
     * @throws IOException Throws if errors occurred while reading pom.xml file.
     */
    public Set<String> getDependencies(File file) throws IOException {

        File pom = new File(file + File.separator + "pom.xml");

        try (BufferedReader reader = new BufferedReader(new FileReader(pom))) {
            boolean dependenciesOpened = false;
            String line;
            Set<String> dependencies = new HashSet<>();

            while (((line = reader.readLine()) != null) && !line.contains("</dependencies>")) {

                if (line.contains("<dependencies>")) {
                    dependenciesOpened = true;
                }

                if (dependenciesOpened && line.contains("<groupId>") && !line.contains("<!--") && !line.contains("-->")) {
                    dependencies.add(line.replace("<groupId>", "").replace("</groupId>", "")
                            .replace("<dependency>", "").trim());
                }
            }

            System.out.println("Dependencies: " + dependencies);
            System.out.println("Finished");
            return dependencies;
        }
    }

    /**
     * Check if a repository is maven-based or not. Searches default branch for pom.xml file.
     *
     * @param link Repository URL.
     * @return True if and only if a repository is maven-based.
     * @throws IOException Throws if errors occurred while getting default branch.
     */
    public boolean isMavenRepository(String link) throws IOException {
        String branch = new RepositoriesHandler().getDefaultBranch(link);

        if (branch == null) {
            return false;
        }

        try {
            URL u = new URL(link + "/blob/" + branch + "/pom.xml");
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
