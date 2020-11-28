package ToyInterpreter.tests;

import ToyInterpreter.tests.testController.testControllerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ToyInterpreter.tests.testModel.testModelSuite;
import ToyInterpreter.tests.testRepo.testRepoSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        testModelSuite.class, testRepoSuite.class, testControllerSuite.class
})


public class suite {
}

