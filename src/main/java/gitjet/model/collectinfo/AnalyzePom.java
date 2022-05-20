package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

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

                if (dependenciesOpened && line.contains("<groupId>")) {
                    dependencies.add(line.replace("<groupId>", "").replace("</groupId>", "").trim());
                }
            }

            System.out.println("Dependencies: " + dependencies);
            return dependencies;
        }
    }

    public static boolean isMavenRepository(String link) throws Exception {
//        List<String> branches = new ArrayList<>();
//        try {
//            Collection<Ref> refs = Git.lsRemoteRepository().setHeads(true).setRemote(link).call();
//            for (Ref r : refs) {
//                List<String> branchFullName = Arrays.asList(r.getName().split("/"));
//                branches.add(branchFullName.get(branchFullName.size() - 1));
//            }
//        } catch (GitAPIException e) {
//            e.printStackTrace();
//        }

        String branch = getDefaultBranch(link);
        System.out.println();


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

    public static String getDefaultBranch(String link) throws Exception {
        String url = "https://api.github.com/repos/" + getAuthorFromLink(link) + "/" + getNameFromLink(link);
        String data = readUrl(url);
        JSONArray jsonArr = new JSONArray("[" + data + "]");
        String branch = null;

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);

            branch = jsonObj.get("default_branch").toString();
        }

        return branch;
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
