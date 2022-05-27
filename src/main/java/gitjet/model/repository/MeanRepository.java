package gitjet.model.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that calculates mean data of GitHub repositories.
 */
public class MeanRepository extends RepositoryEvaluation {

    private final int numberOfContributors;
    private final int numberOfCommits;
    private final int numberOfLinesInProject;
    private final int numberOfLinesInTests;
    private final Set<String> mavenDependencies = new HashSet<>();

    public MeanRepository(List<Repository> repositories) {
        int numberOfRepos = repositories.size();

        if (numberOfRepos == 0) {
            numberOfContributors = 0;
            numberOfCommits = 0;
            numberOfLinesInProject = 0;
            numberOfLinesInTests = 0;
        } else {
            List<Integer> dependenciesUsage = new ArrayList<>();

            int totalNumberOfContributors = 0;
            int totalNumberOfCommits = 0;
            int totalNumberOfLinesInProject = 0;
            int totalNumberOfLinesInTests = 0;

            for (Repository repository : repositories) {

                totalNumberOfContributors += repository.getNumberOfContributors();
                totalNumberOfCommits += repository.getNumberOfCommits();
                totalNumberOfLinesInProject += repository.getNumberOfLinesInProject();
                totalNumberOfLinesInTests += repository.getNumberOfLinesInTests();

                dependenciesUsage.add(repository.getMavenDependencies().size());
            }

            this.numberOfContributors = totalNumberOfContributors / numberOfRepos;
            this.numberOfCommits = totalNumberOfCommits / numberOfRepos;
            this.numberOfLinesInProject = totalNumberOfLinesInProject / numberOfRepos;
            this.numberOfLinesInTests = totalNumberOfLinesInTests / numberOfRepos;
            mavenDependencies.add(String.valueOf(dependenciesUsage.stream().mapToInt(Integer::intValue).sum() / numberOfRepos));
        }
    }

    public String getName() {
        return "Mean";
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
        return null;
    }

    public int getNumberOfLinesInTests() {
        return this.numberOfLinesInTests;
    }

    public String getHasReadMe() {
        return null;
    }

    public Set<String> getMavenDependencies() {
        return this.mavenDependencies;
    }
}
