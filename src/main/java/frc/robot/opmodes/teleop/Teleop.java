package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.commands.teleop.BallFlow;
import frc.robot.commands.teleop.Climb;
import frc.robot.commands.teleop.Drive;
import frc.robot.commands.teleop.Shoot;
import frc.robot.utilities.wpilibdi.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Shuffleboard.selectTab("Teleop");
        
        this.Container.bindCommand(Drive.class).schedule();
        this.Container.bindCommand(BallFlow.class).schedule();
        this.Container.bindCommand(Shoot.class).schedule();
        this.Container.bindCommand(Climb.class).schedule();
    }
}
