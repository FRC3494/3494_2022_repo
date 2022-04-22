package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.commands.teleop.Drive;
import com.fizzyapple12.wpilibdi.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Shuffleboard.selectTab("Teleop");
        
        this.Container.bindCommand(Drive.class).schedule();
    }
}
