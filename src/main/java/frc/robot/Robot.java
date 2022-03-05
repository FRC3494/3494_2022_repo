package frc.robot;

import java.lang.reflect.InvocationTargetException;

import frc.robot.utilities.DiOpMode;
import frc.robot.utilities.DiRobot;
import frc.robot.subsystems.Pneumatics;
import frc.robot.opmodes.autonomous.DriveAndShoot;
import frc.robot.opmodes.debug.Test;
import frc.robot.opmodes.teleop.Teleop;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Electronics;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;

public class Robot extends DiRobot {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.bind(RobotConfig.class).asSingle();

        this.Container.bind(Electronics.class).asSingle();
        this.Container.bind(Pneumatics.class).asSingle();
        this.Container.bind(NavX.class).asSingle();

        this.Container.bind(Drivetrain.class).asSingle();
        this.Container.bind(Shooter.class).asSingle();
        this.Container.bind(Magazine.class).asSingle();
        this.Container.bind(Intake.class).asSingle();
        this.Container.bind(Climber.class).asSingle();

        //this.Container.bind(OI.class).asSingle();
    }

    public DiOpMode CreateTeleop() {
        return new Teleop();
    }

    public DiOpMode CreateAutonomous() {
        return new DriveAndShoot();
    }

    public DiOpMode CreateTest() {
        return new Test();
    }
}
