package ToyInterpreter.tests.testModel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        testTypes.class, testValues.class, testAdts.class, testExps.class, testStmts.class, testPrgState.class,
        testTypeChecker.class
})


public class testModelSuite {
}
