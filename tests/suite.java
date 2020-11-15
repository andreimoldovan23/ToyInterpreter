package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.testController.testControllerSuite;
import tests.testModel.testModelSuite;
import tests.testRepo.testRepoSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        testModelSuite.class, testRepoSuite.class, testControllerSuite.class
})


public class suite {
}

