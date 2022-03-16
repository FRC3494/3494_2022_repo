package frc.robot;

import java.lang.reflect.InvocationTargetException;

import frc.robot.subsystems.Pneumatics;
import frc.robot.opmodes.autonomous.DriveAndHighShoot;
import frc.robot.opmodes.debug.Test;
import frc.robot.opmodes.teleop.Teleop;
import frc.robot.subsystems.AutoNav;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Electronics;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoConfigurable;
//import frc.robot.subsystems.Vision.CameraServerSubsystem;
import frc.robot.utilities.wpilibdi.DiOpMode;
import frc.robot.utilities.wpilibdi.DiRobot;

public class Robot extends DiRobot {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        AutoConfigurable.DontGrabFrom.enableConfiguration = false; // disable for comp
        this.Container.bind(RobotConfig.class).asSingle();
        this.Container.bind(OI.class).asSingle();

        this.Container.bindSubsystem(Electronics.class);
        this.Container.bindSubsystem(Pneumatics.class);
        this.Container.bindSubsystem(NavX.class);
        this.Container.bindSubsystem(AutoNav.class);
        //this.Container.bind(CameraServerSubsystem.class).asSingle();

        this.Container.bindSubsystem(Drivetrain.class);
        this.Container.bindSubsystem(Shooter.class);
        this.Container.bindSubsystem(Magazine.class);
        this.Container.bindSubsystem(Intake.class);
        this.Container.bindSubsystem(Climber.class);

        //this.Container.bind(OI.class).asSingle();
    }

    public DiOpMode CreateTeleop() {
        return new Teleop();
    }

    public DiOpMode CreateAutonomous() {
        return new DriveAndHighShoot();
    }

    public DiOpMode CreateTest() {
        return new Test();
    }
}
