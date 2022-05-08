package gitjet.model;

import gitjet.model.clonerepo.GitCloningException;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.List;

import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.Commits.commitsStats;
import static gitjet.model.collectinfo.LineSize.printLineSize;

public class ReposHandler {
    String numberOfCommits, numberofContributors, commitsPerContributor, numberOfStringsInProject, testsInProject,
            numberOfStringsInTests, readmeInProject;
    List<String> mavenDependencies;

    public void handler(String repo) throws GitCloningException, GitAPIException, IOException {
        System.out.println("Starting cloning process");
        runCloning(repo);

        List<String> commits = commitsStats();
        numberofContributors = commits.get(0);
        numberOfCommits = commits.get(1);
        commitsPerContributor = commits.get(2);

        numberOfStringsInProject = printLineSize();
    }
}
