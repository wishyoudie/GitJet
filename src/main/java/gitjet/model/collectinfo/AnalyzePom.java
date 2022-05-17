package gitjet.model.collectinfo;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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

    public static boolean isMavenRepository(File file) {
        File pom = new File(file + File.separator + "pom.xml");
        return pom.exists();
    }

    public static boolean isMavenRepository(String link) {
        List<String> branches = new ArrayList<>();
        try {
            Collection<Ref> refs = Git.lsRemoteRepository().setHeads(true).setRemote(link).call();
            for (Ref r : refs) {
                List<String> branchFullName = Arrays.asList(r.getName().split("/"));
                branches.add(branchFullName.get(branchFullName.size() - 1));
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        for (String branch : branches) {
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
        }
        return false;
    }
}
