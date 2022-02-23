package frc.robot.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.utilities.TestProctor.TestStatus;
import frc.robot.utilities.di.DiContainer.Inject;

public abstract class DiTest extends DiCommand {
    @Inject
    TestProctor testProctor;

    HashMap<Method, TestProctor.TestStatus> testMethodMap = new HashMap<>();
    List<Method> testMethods = new ArrayList<>();
    int currentTest = 0;
    Thread currentTestThread;

    public abstract void prepare();

    public abstract void cleanup();

    public void onInitialize() {
        this.testProctor.registerTest(this);

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!Subsystem.class.isAssignableFrom(field.getType())) continue;

            try {
                ((Subsystem) field.get(this)).setDefaultCommand(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Method method : this.getClass().getMethods()) {
            method.setAccessible(true);
            if (!method.getName().startsWith("test_") || method.getReturnType() != TestProctor.TestStatus.class || method.getParameterTypes().length != 0) continue;
            
            this.testMethods.add(method);
            this.testMethodMap.put(method, TestStatus.Testing);
        }

        prepare();
    }

    public void onTick() {
        if (this.currentTest >= this.testMethods.size()) {
            this.isFinished = true;
        }

        if (this.currentTestThread == null) {
            this.currentTestThread = createNextTestThread();
            this.currentTestThread.start();
        }

        if (!this.currentTestThread.isAlive()) {
            this.currentTest++;
            this.currentTestThread = null;
        }
    }

    public Thread createNextTestThread() {
        return new Thread(() -> {
            try {
                Method currentTestMethod = this.testMethods.get(this.currentTest);
                TestStatus testStatus = TestStatus.Testing;

                while (testStatus == TestStatus.Testing) {
                    testStatus = (TestStatus) currentTestMethod.invoke(this, new Object[0]);
                }
                
                this.testMethodMap.replace(currentTestMethod, testStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void onDispose() {
        this.cleanup();

        boolean passing = true;

        for (TestStatus testStatus : this.testMethodMap.values()) {
            if (testStatus != TestStatus.Passed) passing = false;
        }

        if (passing) this.testProctor.pass(this);
        else this.testProctor.fail(this);
    }
}
