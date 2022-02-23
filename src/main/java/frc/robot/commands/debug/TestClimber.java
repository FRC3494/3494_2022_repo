package frc.robot.commands.debug;

import frc.robot.utilities.DiTest;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.subsystems.Climber;

public class TestClimber extends DiTest implements IInitializable, ITickable, IDisposable {
    @Inject
    Climber climber;

    public void prepare() {
        
    }

    public void cleanup() {
        climber.runRaw(0);
    }
}
