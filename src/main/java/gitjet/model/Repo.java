package gitjet.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Repo(String rawRepo) {
        String[] rawParts = rawRepo.split(" ");
        List<String> parts = new ArrayList<>(Arrays.asList(rawParts));
        this.name = parts.get(0);
        this.numberOfContributors = Integer.parseInt(parts.get(1));
        this.amountOfLines = Integer.parseInt(parts.get(2));
        this.numberOfCommits = Integer.parseInt(parts.get(3));
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
        return this.numberOfCommits;
    }

    public void setNumberOfCommits(int numberOfCommits) {
        this.numberOfCommits = numberOfCommits;
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d", this.name, this.numberOfContributors, this.amountOfLines, this.numberOfCommits);
    }
}