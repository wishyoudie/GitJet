package gitjet.model;

import java.util.*;

public class Repo {

    private String name;
    private String author;
    private int numberOfContributors;
    private int numberOfCommits;
    private int numberOfLinesInProject;
    private int numberOfLinesInTests;
    private String hasTests;
    private String hasReadMe;
    private final Set<String> mavenDependencies = new HashSet<>();

    public Repo(String name, String author,
                int numberOfContributors, int numberOfCommits, int numberOfLinesInProject, boolean testInProject, int numberOfLinesInTests,
                boolean readmeInProject, Set<String> mavenDependencies) {
        this.name = name;
        this.author = author;
        this.numberOfContributors = numberOfContributors;
        this.numberOfCommits = numberOfCommits;
        this.numberOfLinesInProject = numberOfLinesInProject;
        this.numberOfLinesInTests = numberOfLinesInTests;
        this.hasTests = refactorBoolean(testInProject);
        this.hasReadMe = refactorBoolean(readmeInProject);
        this.mavenDependencies.addAll(mavenDependencies);
    }

    public Repo() {
    }

    public Repo(String rawRepo) {
        String[] rawParts = rawRepo.split(" ");
        List<String> parts = new ArrayList<>(Arrays.asList(rawParts));
        this.name = parts.get(0);
        this.author = parts.get(1);
        this.numberOfContributors = Integer.parseInt(parts.get(2));
        this.numberOfCommits = Integer.parseInt(parts.get(3));
        this.numberOfLinesInProject = Integer.parseInt(parts.get(4));
        this.hasTests = parts.get(5);
        this.numberOfLinesInTests = Integer.parseInt(parts.get(6));
        this.hasReadMe = parts.get(7);
        for (int i = 8; i < parts.size(); i++) {
            this.mavenDependencies.add(parts.get(i));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getHasTests() {
        return this.hasTests;
    }

    public boolean hasTests() {
        return Objects.equals(this.hasTests, "+");
    }

    public void setHasTests(boolean hasTests) {
        this.hasTests = refactorBoolean(hasTests);
    }

    public int getNumberOfLinesInTests() {
        return this.numberOfLinesInTests;
    }

    public void setNumberOfLinesInTests(int amountOfLines) {
        this.numberOfLinesInTests = amountOfLines;
    }

    public String getHasReadMe() {
        return this.hasReadMe;
    }

    public boolean hasReadMe() {
        return Objects.equals(this.hasReadMe, "+");
    }

    public void setHasReadMe(boolean hasReadMe) {
        this.hasReadMe = refactorBoolean(hasReadMe);
    }

    public void setReadmeInProject(String readmeInProject) {
        this.hasReadMe = readmeInProject;
    }

    public Set<String> getMavenDependencies() {
        return this.mavenDependencies;
    }

    public void setMavenDependencies(Set<String> mavenDependencies) {
        this.mavenDependencies.addAll(mavenDependencies);
    }

    private String refactorBoolean(boolean condition) {
        if (condition)
            return "+";
        return "-";
    }

    public static String getNameFromLink(String link) {
        List<String> linkSplit = Arrays.asList(link.split("/"));
        return linkSplit.get(linkSplit.size() - 1);
    }

    public static String getAuthorFromLink(String link) {
        List<String> linkSplit = Arrays.asList(link.split("/"));
        return linkSplit.get(linkSplit.size() - 2);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s %d %d %d %s %d %s", this.name, this.author, this.numberOfContributors, this.numberOfCommits, this.numberOfLinesInProject,
                this.hasTests, this.numberOfLinesInTests, this.hasReadMe));
        for (String dep : mavenDependencies) {
            sb.append(" ");
            sb.append(dep);
        }
        return sb.toString();
    }
}