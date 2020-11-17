package ToyInterpreter.tests.testModel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        testConstExp.class, testVarExp.class, testArithmeticExp.class,
        testLogicExp.class, testRelationalExp.class
})


public class testExps {
}
