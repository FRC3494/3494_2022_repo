package frc.robot.utilities;

import java.util.HashMap;

public class TestProctor {
    public enum TestStatus {
        Passed,
        Failed,
        Testing
    }

    HashMap<DiTest, TestStatus> tests = new HashMap<>();

    public void registerTest(DiTest test) {
        this.tests.put(test, TestStatus.Testing);
    }

    public void pass(DiTest test) {
        this.tests.replace(test, TestStatus.Passed);
        
        checkTests();
    }

    public void fail(DiTest test) {
        this.tests.replace(test, TestStatus.Failed);
        
        checkTests();
    }

    public void checkTests() {
        boolean allDone = true;

        for (TestStatus status : this.tests.values()) {
            if (status == TestStatus.Testing) allDone = false;
        }

        if (allDone) stopTesting();
    }

    public void stopTesting() {
        for (DiTest test : this.tests.keySet()) {
            test.isFinished = true;
        }

        publishResults();
    }

    public void publishResults() {
        
        // Display test results to the driver here
    }
}
