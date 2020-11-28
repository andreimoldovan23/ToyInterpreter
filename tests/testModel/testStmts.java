package ToyInterpreter.tests.testModel;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        testVarDecl.class, testPrintStmt.class, testAssignStmt.class,
        testIfStmt.class, testOpenFileStmt.class, testCloseFileStmt.class,
        testReadFileStmt.class, testNOP.class, testCompStmt.class,
        testNewStmt.class, testWriteHeapStmt.class, testWhileStmt.class, testForkStmt.class
})


public class testStmts {
}
