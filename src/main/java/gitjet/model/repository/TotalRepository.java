package gitjet.model.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that summarizes data about GitHub repositories.
 */
public class TotalRepository extends RepositoryEvaluation {

    private int numberOfContributors;
    private int numberOfCommits;
    private int numberOfLinesInProject;
    private int numberOfLinesInTests;
    private final String testsTotal;
    private final String readmesTotal;
    private final Set<String> mavenDependencies = new HashSet<>();

    public TotalRepository(List<Repository> repositories) {

        int numberOfRepos = repositories.size();
        int numberOfReadmes = 0;
        int numberOfTests = 0;
        Set<String> dependencies = new HashSet<>();

        for (Repository repository : repositories) {

            numberOfContributors += repository.getNumberOfContributors();
            numberOfCommits += repository.getNumberOfCommits();
            numberOfLinesInProject += repository.getNumberOfLinesInProject();
            numberOfLinesInTests += repository.getNumberOfLinesInTests();

            if (repository.hasReadMe()) {
                numberOfReadmes += 1;
            }

            if (repository.getNumberOfLinesInTests() > 0) {
                numberOfTests += 1;
            }

            dependencies.addAll(repository.getMavenDependencies());
        }

        readmesTotal = numberOfReadmes + "/" + numberOfRepos;
        testsTotal = numberOfTests + "/" + numberOfRepos;
        mavenDependencies.add(String.valueOf(dependencies.size()));
    }

    public String getName() {
        return "Total";
    }

    public String getAuthor() {
        return null;
    }

    public int getNumberOfContributors() {
        return this.numberOfContributors;
    }

    public int getNumberOfCommits() {
        return this.numberOfCommits;
    }

    public int getNumberOfLinesInProject() {
        return this.numberOfLinesInProject;
    }

    public String getHasTests() {
        return this.testsTotal;
    }

    public int getNumberOfLinesInTests() {
        return this.numberOfLinesInTests;
    }

    public String getHasReadMe() {
        return this.readmesTotal;
    }

    public Set<String> getMavenDependencies() {
        return this.mavenDependencies;
    }
}
