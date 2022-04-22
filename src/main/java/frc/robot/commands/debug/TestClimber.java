package frc.robot.commands.debug;

import com.fizzyapple12.javadi.DiContainer.Inject;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.IInitializable;
import com.fizzyapple12.javadi.DiInterfaces.ITickable;
import com.fizzyapple12.wpilibdi.DiTest;
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
