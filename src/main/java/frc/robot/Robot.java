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
        this.Container.bind(Drivetrain.class);
        this.Container.bind(Shooter.class);
        this.Container.bind(Magazine.class);
        this.Container.bind(Intake.class);
        this.Container.bind(Climber.class);
    }
}
