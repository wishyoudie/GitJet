package gitjet.model;

public class Repo {

    private String name;
    private int numberOfContributors;
    private int amountOfLines;
    private int numberOfCommits;

    public Repo(String name, int numberOfContributors, int amountOfLines, int numberOfCommits) {
        this.name = name;
        this.numberOfContributors = numberOfContributors;
        this.amountOfLines = amountOfLines;
        this.numberOfCommits = numberOfCommits;
    }

    public Repo() {
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

    public int getAmountOfLines() {
        return this.amountOfLines;
    }

    public void setAmountOfLines(int amountOfLines) {
        this.amountOfLines = amountOfLines;
    }

    public int getNumberOfCommits() {
        return this.amountOfLines;
    }

    public void setNumberOfCommits(int numberOfCommits) {
        this.numberOfCommits = numberOfCommits;
    }
}
