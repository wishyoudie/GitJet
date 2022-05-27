package gitjet.model.repository;

import gitjet.model.Errors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * Repository abstract class.
 */
public abstract class RepositoryEvaluation {

    /**
     * Name getter.
     *
     * @return Repository name.
     */
    public abstract String getName();

    /**
     * Author getter.
     *
     * @return Repository authors username.
     */
    public abstract String getAuthor();

    /**
     * Number of contributors getter.
     *
     * @return Number of contributors to repository.
     */
    public abstract int getNumberOfContributors();

    /**
     * Number of commits getter.
     *
     * @return Number of commits to repository.
     */
    public abstract int getNumberOfCommits();

    /**
     * Total number of lines in project getter.
     *
     * @return Number of lines in all project source code files.
     */
    public abstract int getNumberOfLinesInProject();

    /**
     * Flag if a repository has tests getter.
     *
     * @return '+' if repository has test files, '-' otherwise.
     */
    public abstract String getHasTests();

    /**
     * Number of lines in tests getter.
     *
     * @return Number of lines in test files.
     */
    public abstract int getNumberOfLinesInTests();

    /**
     * ReadMe getter (String).
     *
     * @return '+' if repository has a ReadMe file, '-' otherwise.
     */
    public abstract String getHasReadMe();

    /**
     * Dependencies getter.
     *
     * @return Set of repository dependencies.
     */
    public abstract Set<String> getMavenDependencies();

    /**
     * Adds repository to storage (default: data.dat file).
     */
    public void addToStorage() {
        try (Writer writer = new BufferedWriter(new FileWriter("data.dat", true))) {
            writer.append(this.toString()).append(System.lineSeparator());
        } catch (IOException e) {
            throw new IllegalArgumentException(Errors.DATA_ERROR.getMessage());
        }
    }
}
