package gitjet.model.collectinfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.*;

import static gitjet.model.Repo.getAuthorFromLink;
import static gitjet.model.Repo.getNameFromLink;

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

                if (dependenciesOpened && line.contains("<groupId>") && !line.contains("<!--") && !line.contains("-->")) {
                    dependencies.add(line.replace("<groupId>", "").replace("</groupId>", "")
                            .replace("<dependency>", "").trim());
                }
            }

            System.out.println("Dependencies: " + dependencies);
            return dependencies;
        }
    }

    public static boolean isMavenRepository(String link) {

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

    public static String getDefaultBranch(String link) {
        String url = "https://api.github.com/repos/" + getAuthorFromLink(link) + "/" + getNameFromLink(link);
        String data = readUrl(url);

        if (data == null) {
            return null;
        }

        JSONArray jsonArr = new JSONArray("[" + data + "]");
        String branch = null;

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);

            branch = jsonObj.get("default_branch").toString();
        }

        return branch;
    }

    private static String readUrl(String urlString) {
        for (int i = 1; i <= 5; i++) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()))) {

                StringBuilder buffer = new StringBuilder();
                int read;
                char[] chars = new char[1024];

                while ((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);

                return buffer.toString();
            } catch (IOException e) {
                System.err.println("Can't connect, retrying: " + i + "/5");
            }
        }
        return null;
    }
}
