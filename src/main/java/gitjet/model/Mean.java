package gitjet.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Mean {

    private static List<Repo> readDat() {
        List<Repo> results = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> splittedLine = List.of(line.split(" "));
                String name = splittedLine.get(0);
                String author = splittedLine.get(1);
                int numberOfContributors = Integer.parseInt(splittedLine.get(2));
                int numberOfCommits = Integer.parseInt(splittedLine.get(3));
                int numberOfLinesInProject = Integer.parseInt(splittedLine.get(4));
                boolean testsInProject = Objects.equals(splittedLine.get(5), "+");
                int numberOfLinesInTests = Integer.parseInt(splittedLine.get(6));
                boolean readmeInProject = Objects.equals(splittedLine.get(7), "+");

                Set<String> dependencies = new HashSet<>();
                for (int i = 8; i < splittedLine.size(); i++) {
                    dependencies.add(splittedLine.get(i));
                }
                results.add(new Repo(name, author, numberOfContributors, numberOfCommits, numberOfLinesInProject, testsInProject, numberOfLinesInTests, readmeInProject, dependencies));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }

        return results;
    }

    public static Repo mean() {

        List<Repo> repos = readDat();

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

            if (repo.hasReadMe()) {
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
