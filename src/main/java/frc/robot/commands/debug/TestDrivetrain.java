package frc.robot.commands.debug;

import frc.robot.utilities.DiTest;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.subsystems.Drivetrain;

public class TestDrivetrain extends DiTest implements IInitializable, ITickable, IDisposable {
    @Inject
    Drivetrain drivetrain;

    public void prepare() {
        
    }

    public void cleanup() {
        drivetrain.run(0, 0);
    }
}
