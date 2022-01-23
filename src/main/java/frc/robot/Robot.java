package frc.robot;

import java.lang.reflect.InvocationTargetException;
import frc.robot.utilities.DiRobot;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class Robot extends DiRobot {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.Bind(Drivetrain.class);
        this.Container.Bind(Shooter.class);
        this.Container.Bind(Magazine.class);
        this.Container.Bind(Intake.class);
        this.Container.Bind(Climber.class);
    }
}
