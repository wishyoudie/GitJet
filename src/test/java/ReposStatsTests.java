import gitjet.model.Repo;
import gitjet.model.ReposHandler;
import gitjet.model.clonerepo.GitCloningException;
import gitjet.model.collectinfo.CheckTests;
import gitjet.model.collectinfo.CommitsHistory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static gitjet.model.clonerepo.CloneProjects.deleteClone;
import static gitjet.model.clonerepo.CloneProjects.runCloning;
import static gitjet.model.collectinfo.AnalyzePom.getDependencies;
import static gitjet.model.collectinfo.CheckReadme.isReadmeInProject;
import static gitjet.model.collectinfo.LineSize.getAmountOfLines;
import static org.junit.jupiter.api.Assertions.*;

class ReposStatsTests {

    private static File repo1, repo2;
    private static Repo handledRepo1, handledRepo2;
    private static final ReposHandler reposHandler = new ReposHandler();

    @BeforeAll
    static void setUp() throws GitCloningException, IOException {
        repo1 = runCloning("https://github.com/s1ckoleg/PolytechTerminalApp", "TestRepo1");
        repo2 = runCloning("https://github.com/wishyoudie/coursework_sem2_t1", "TestRepo2");
        handledRepo1 = reposHandler.handle("https://github.com/s1ckoleg/PolytechTerminalApp");
        handledRepo2 = reposHandler.handle("https://github.com/wishyoudie/coursework_sem2_t1");
    }

    @AfterAll
    static void cleanUp() {
        deleteClone(repo1);
        deleteClone(repo2);
        new File("clones").deleteOnExit();
    }

    @Test
    void authorTest() {
        String repo1author = handledRepo1.getAuthor();
        String repo2author = handledRepo2.getAuthor();

        assertEquals("s1ckoleg", repo1author);
        assertEquals("wishyoudie", repo2author);
    }

    @Test
    void commitsHistoryTest() {

        CommitsHistory commitsHistory1 = new CommitsHistory(repo1);
        assertEquals(23, commitsHistory1.getNumberOfCommits());
        assertEquals(1, commitsHistory1.getNumberOfContributors());

        CommitsHistory commitsHistory2 = new CommitsHistory(repo2);
        assertEquals(13, commitsHistory2.getNumberOfCommits());
        assertEquals(2, commitsHistory2.getNumberOfContributors());
    }

    @Test
    void checkReadmeTest() {
        assertFalse(isReadmeInProject(repo1));
        assertEquals("-", handledRepo1.getHasReadMe());
        assertTrue(isReadmeInProject(repo2));
        assertEquals("+", handledRepo2.getHasReadMe());
    }

    @Test
    void testsSizeTest() throws IOException {
        CheckTests checkTests1 = new CheckTests();
        CheckTests checkTests2 = new CheckTests();
        assertEquals("+", handledRepo1.getHasTests());
        assertEquals(210, checkTests1.getNumberOfLinesInTests(repo1));
        assertEquals("+", handledRepo2.getHasTests());
        assertEquals(234, checkTests2.getNumberOfLinesInTests(repo2));
    }

    @Test
    void repositorySizeTest() throws IOException {
        assertEquals(573, getAmountOfLines(repo1));
        assertEquals(517, getAmountOfLines(repo2));
    }

    @Test
    void analyzePomTest() throws IOException {
        Set<String> dependencies1 = Set.of("org.jetbrains", "org.junit.jupiter", "junit", "commons-io", "args4j", "org.projectlombok");
        Set<String> dependencies2 = Set.of("junit", "org.junit.jupiter");
        assertEquals(dependencies1, getDependencies(repo1));
        assertEquals(dependencies2, getDependencies(repo2));

    }
}