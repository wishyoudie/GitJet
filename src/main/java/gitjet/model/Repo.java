package gitjet.model;

import java.util.*;

public class Repo {

    private String name;
    private int numberOfContributors;
    private int numberOfCommits;
    private int numberOfLinesInProject;
    private int numberOfLinesInTests;
    private String readmeInProject;
    private final Set<String> mavenDependencies = new HashSet<>();

    public Repo(String name,
                int numberOfContributors, int numberOfCommits, int numberOfLinesInProject, int numberOfLinesInTests,
                boolean readmeInProject, Set<String> mavenDependencies) {
        this.name = name;
        this.numberOfContributors = numberOfContributors;
        this.numberOfCommits = numberOfCommits;
        this.numberOfLinesInProject = numberOfLinesInProject;
        this.numberOfLinesInTests = numberOfLinesInTests;
        this.readmeInProject = refactorReadMe(readmeInProject);
        this.mavenDependencies.addAll(mavenDependencies);
    }

    public Repo() {
    }

    public Repo(String rawRepo) {
        String[] rawParts = rawRepo.split(" ");
        List<String> parts = new ArrayList<>(Arrays.asList(rawParts));
        this.name = parts.get(0);
        this.numberOfContributors = Integer.parseInt(parts.get(1));
        this.numberOfCommits = Integer.parseInt(parts.get(2));
        this.numberOfLinesInProject = Integer.parseInt(parts.get(3));
        this.numberOfLinesInTests = Integer.parseInt(parts.get(4));
        this.readmeInProject = parts.get(5);
        for (int i = 6; i < parts.size(); i++) {
            this.mavenDependencies.add(parts.get(i));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfContributors() {
        return this.numberOfContributors;
    }

    public void setNumberOfContributors(int numberOfContributors) {
        this.numberOfContributors = numberOfContributors;
    }

    public int getNumberOfCommits() {
        return this.numberOfCommits;
    }

    public void setNumberOfCommits(int numberOfCommits) {
        this.numberOfCommits = numberOfCommits;
    }

    public int getNumberOfLinesInProject() {
        return this.numberOfLinesInProject;
    }

    public void setNumberOfLinesInProject(int amountOfLines) {
        this.numberOfLinesInProject = amountOfLines;
    }

    public int getNumberOfLinesInTests() {
        return this.numberOfLinesInTests;
    }

    public void setNumberOfLinesInTests(int amountOfLines) {
        this.numberOfLinesInTests = amountOfLines;
    }

    public String getReadmeInProject() {
        return this.readmeInProject;
    }

    public boolean isReadmeInProject() {
        return Objects.equals(this.readmeInProject, "+");
    }

    public void setReadmeInProject(boolean readmeInProject) {
        this.readmeInProject = refactorReadMe(readmeInProject);
    }

    public void setReadmeInProject(String readmeInProject) {
        this.readmeInProject = readmeInProject;
    }

    public Set<String> getMavenDependencies() {
        return this.mavenDependencies;
    }

    public void setMavenDependencies(Set<String> mavenDependencies) {
        this.mavenDependencies.addAll(mavenDependencies);
    }

    private String refactorReadMe(boolean condition) {
        if (condition)
            return "+";
        return "-";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %d %d %d %d %s", this.name, this.numberOfContributors, this.numberOfCommits, this.numberOfLinesInProject,
                this.numberOfLinesInTests, this.readmeInProject));
        for (String dep : mavenDependencies) {
            sb.append(" ");
            sb.append(dep);
        }
        return sb.toString();
    }
}