package gitjet.model;

import gitjet.Utils;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.CheckTests;
import gitjet.model.collectinfo.CommitsHistory;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.*;
import java.util.*;

import static gitjet.model.Repo.getAuthorFromLink;
import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.AnalyzePom.getDependencies;
import static gitjet.model.collectinfo.AnalyzePom.isMavenRepository;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;
import static gitjet.model.Repo.getNameFromLink;

public class ReposHandler {

    public Repo handle(String link) {

        String repoName = getNameFromLink(link);
        String repoAuthor = getAuthorFromLink(link);

        System.out.println("Starting cloning process");
        File clone = null;
        try {
            clone = runCloning(link, repoName);
        } catch (GitCloningException | IOException e) {
            e.printStackTrace();
        }

        if (clone == null) {
            return null;
        }

        CommitsHistory commitsHistory = new CommitsHistory();
        commitsHistory.commitsStats(clone);

        int numberOfContributors = commitsHistory.getNumberOfContributors();
        int numberOfCommits = commitsHistory.getNumberOfCommits();
        double commitsPerContributor = (numberOfCommits * 1.0) / numberOfContributors;

        int numberOfLinesInProject = 0;
        try {
            numberOfLinesInProject = getAmountOfLines(clone);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CheckTests checkTests = new CheckTests();
        int numberOfLinesInTests = 0;
        try {
            numberOfLinesInTests = checkTests.getNumberOfLinesInTests(clone);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean readmeInProject = isReadmeInProject(clone);

        Set<String> mavenDependencies = null;
        try {
            mavenDependencies = getDependencies(clone);
        } catch (IOException e) {
            e.printStackTrace();
        }

        deleteClone(clone);
        System.out.println("Deleted");
        Repo result = new Repo(repoName, repoAuthor, numberOfContributors, numberOfCommits, numberOfLinesInProject, (numberOfLinesInTests != 0), numberOfLinesInTests, readmeInProject, mavenDependencies);
        result.addToStorage();
        return result;
    }

    public void handleSearchedRepos(int requiredNumber) throws IOException {
        RepositoryService repositoryService = new RepositoryService();
        int page = 1;
        int counter = 0;

        while (counter < requiredNumber) {
            List<SearchRepository> repos = repositoryService.searchRepositories("size:>0", "java", page);

            for (SearchRepository repo : repos) {
                String link = "https://github.com/" + repo.toString();
                System.out.println("Checking " + link);
                if (isMavenRepository(link)) {
                    Repo result = handle(link);
                    if (!Objects.equals(result, null)) {
                        counter++;
                    }
                    if (counter == requiredNumber) {
                        return;
                    }
                }
            }

            page++;
        }

    }

    public void handleLinksFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                handle(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't open file " + file);
        }
    }

    public List<Repo> readData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            List<Repo> result = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(new Repo(line));
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }
    }

    public boolean alreadyHandled(String name) {
        List<String> scannedNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                scannedNames.add(Arrays.asList(line.split(" ")).get(0));
            }
            return scannedNames.contains(name);
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }
    }

    // CHECK
    public void update(String link) {
        String name = getNameFromLink(link);
        if (alreadyHandled(name)) {
            List<String> before = new ArrayList<>();
            List<String> after = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("data.dat"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (Objects.equals(Arrays.asList(line.split(" ")).get(0), name)) {
                        break;
                    }
                    before.add(line);
                }
                while ((line = br.readLine()) != null) {
                    after.add(line);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
            }

            Utils.cleanFile("data.dat");

            try (FileWriter writer = new FileWriter("data.dat", true)) {
                for (String line : before) {
                    writer.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
            }

            handle(link);

            try (FileWriter writer = new FileWriter("data.dat", true)) {
                for (String line : after) {
                    writer.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
            }

        } else {
            handle(link);
        }
    }
}