package frc.robot.commands.debug;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiTest;
import frc.robot.subsystems.Shooter;

public class TestShooter extends DiTest implements IInitializable, ITickable, IDisposable {
    @Inject
    Shooter shooter;

    public void prepare() {
        
    }

    public void cleanup() {
        shooter.run(0);
        shooter.runHood(false);
        shooter.runTurret(0);
    }
}
