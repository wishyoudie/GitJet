package gitjet.model;

import java.util.*;
import java.util.stream.Collectors;

public class Mean {

    public static Repo mean(List<Repo> repos) {

        int numberOfRepos = repos.size();

        int numberOfContributors = 0,
                numberOfCommits = 0,
                numberOfLinesInProject = 0,
                numberOfLinesInTests = 0,
                numberOfReadmes = 0,
                numberOfTests = 0;
        Map<String, Integer> dependenciesUsage = new HashMap<>();

        for (Repo repo : repos) {

            numberOfContributors += repo.getNumberOfContributors();
            numberOfCommits += repo.getNumberOfCommits();
            numberOfLinesInProject += repo.getNumberOfLinesInProject();
            numberOfLinesInTests += repo.getNumberOfLinesInTests();

            if (repo.isReadmeInProject()) {
                numberOfReadmes += 1;
            }

            if (repo.getNumberOfLinesInTests() > 0) {
                numberOfTests += 1;
            }

            Set<String> dependencies = repo.getMavenDependencies();

            for(String dependency : dependencies) {
                if (!dependenciesUsage.containsKey(dependency)) {
                    dependenciesUsage.put(dependency, 1);
                } else {
                    Integer counter = dependenciesUsage.get(dependency);
                    dependenciesUsage.put(dependency, counter + 1);
                }
            }
        }

        Map<String,Integer> topThree =
                dependenciesUsage.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(3)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String readmesInProject = numberOfReadmes + "/" + numberOfRepos;
        String testsInProject = numberOfTests + "/" + numberOfRepos;

//        return new Repo(Math.round(1.0 * numberOfContributors / numberOfRepos), ...);

        return new Repo();

    }
}
