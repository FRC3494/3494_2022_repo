package frc.robot.commands.debug;

import com.fizzyapple12.javadi.DiContainer.Inject;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.IInitializable;
import com.fizzyapple12.javadi.DiInterfaces.ITickable;
import com.fizzyapple12.wpilibdi.DiTest;
import frc.robot.subsystems.Magazine;

public class TestMagazine extends DiTest implements IInitializable, ITickable, IDisposable {
    @Inject
    Magazine magazine;

    public void prepare() {
        
    }

    public void cleanup() {
        magazine.runRaw(0, 0, 0);
    }
}
