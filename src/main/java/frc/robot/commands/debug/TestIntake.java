package frc.robot.commands.debug;

import com.fizzyapple12.javadi.DiContainer.Inject;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.IInitializable;
import com.fizzyapple12.javadi.DiInterfaces.ITickable;
import com.fizzyapple12.wpilibdi.DiTest;
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
