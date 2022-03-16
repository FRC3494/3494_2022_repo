package frc.robot.commands.debug;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiTest;
import frc.robot.subsystems.Intake;

public class TestIntake extends DiTest implements IInitializable, ITickable, IDisposable {
    @Inject
    Intake intake;

    public void prepare() {
        
    }

    public void cleanup() {
        intake.run(0, 0);
    }
}
