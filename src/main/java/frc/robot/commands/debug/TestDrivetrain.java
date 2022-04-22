package frc.robot.commands.debug;

import com.fizzyapple12.javadi.DiContainer.Inject;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.IInitializable;
import com.fizzyapple12.javadi.DiInterfaces.ITickable;
import com.fizzyapple12.wpilibdi.DiTest;
import frc.robot.subsystems.Drivetrain;

public class TestDrivetrain extends DiTest implements IInitializable, ITickable, IDisposable {
    @Inject
    Drivetrain drivetrain;

    public void prepare() {
        
    }

    public void cleanup() {
        drivetrain.tankDrive(0, 0);
    }
}
