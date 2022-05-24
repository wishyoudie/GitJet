package gitjet.model.collectinfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.*;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import static gitjet.model.Repo.getAuthorFromLink;
import static gitjet.model.Repo.getNameFromLink;

public class AnalyzePom {

    private String githubLogin, githubPassword;

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
        GitHub gitHub = new GitHubBuilder().withPassword(githubLogin, githubPassword).build();
        GHRepository ghRepository = gitHub.getRepository(getAuthorFromLink(link) + "/" + getNameFromLink(link));

        return ghRepository.getDefaultBranch();
    }

    public void setGithubPassword(String githubPassword) {
        this.githubPassword = githubPassword;
    }

    public void setGithubLogin(String githubLogin) {
        this.githubLogin = githubLogin;
    }
}
