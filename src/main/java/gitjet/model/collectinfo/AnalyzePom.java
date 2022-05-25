package gitjet.model.collectinfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import gitjet.model.Errors;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import static gitjet.Utils.getSetting;
import static gitjet.model.Repo.getAuthorFromLink;
import static gitjet.model.Repo.getNameFromLink;

public class AnalyzePom {

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
            return dependencies;
        }
    }

    public boolean isMavenRepository(String link) throws IOException {

        String branch = getDefaultBranch(link);

        if (branch == null) {
            return false;
        }


        try {
            URL u = new URL(link + "/blob/" + branch + "/pom.xml");
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                return true;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getDefaultBranch(String link) throws IOException {
        try {
            GitHub gitHub = new GitHubBuilder().withOAuthToken(getSetting("token")).build();
            GHRepository ghRepository = gitHub.getRepository(getAuthorFromLink(link) + "/" + getNameFromLink(link));
            String branch = ghRepository.getDefaultBranch();
            System.out.println(gitHub.getRateLimit());
            return branch;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(Errors.TOKEN_ERROR.getMessage());
        }
    }
}
