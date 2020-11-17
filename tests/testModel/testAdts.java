package ToyInterpreter.tests.testModel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        testIExeStack.class, testISymTable.class, testIOut.class,
        testIFileTable.class, testMyBufferedReader.class
})


public class testAdts {
}
