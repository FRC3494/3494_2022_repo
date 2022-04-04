package frc.robot;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.subsystems.Pneumatics;
import frc.robot.opmodes.autonomous.FourBalls;
import frc.robot.opmodes.autonomous.ShootCloseHighThenDriveThenShootCloseHigh;
import frc.robot.opmodes.autonomous.ShootCloseHighThenDriveThenShootFarHigh;
import frc.robot.opmodes.autonomous.ShootCloseLowThenDrive;
import frc.robot.opmodes.autonomous.ShootCloseLowThenDriveThenShootCloseLow;
import frc.robot.opmodes.autonomous.TestODO;
import frc.robot.opmodes.debug.Test;
import frc.robot.opmodes.teleop.Teleop;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Electronics;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision.ComputerVision;
import frc.robot.utilities.AutoConfigurable;
import frc.robot.utilities.wpilibdi.DiOpMode;
import frc.robot.utilities.wpilibdi.DiRobot;

public class Robot extends DiRobot {
    SendableChooser<Class<?>> autoChooser;

    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        AutoConfigurable.DontGrabFrom.enableConfiguration = true; // disable for comp
        this.Container.bind(RobotConfig.class).asSingle();
        this.Container.bind(OI.class).asSingle();

        this.autoChooser = new SendableChooser<>();
        this.autoChooser.setDefaultOption("No Auto", null);
        this.autoChooser.addOption(ShootCloseLowThenDrive.class.getSimpleName(), ShootCloseLowThenDrive.class);
        this.autoChooser.addOption(ShootCloseLowThenDriveThenShootCloseLow.class.getSimpleName(), ShootCloseLowThenDriveThenShootCloseLow.class);
        this.autoChooser.addOption(ShootCloseHighThenDriveThenShootCloseHigh.class.getSimpleName(), ShootCloseHighThenDriveThenShootCloseHigh.class);
        this.autoChooser.addOption(ShootCloseHighThenDriveThenShootFarHigh.class.getSimpleName(), ShootCloseHighThenDriveThenShootFarHigh.class);
        this.autoChooser.addOption(FourBalls.class.getSimpleName(), FourBalls.class);
        this.autoChooser.addOption(TestODO.class.getSimpleName(), TestODO.class);
        Shuffleboard.getTab("Autonomous").add(this.autoChooser).withSize(3, 1);

        this.Container.bindSubsystem(Electronics.class);
        this.Container.bindSubsystem(Pneumatics.class);
        this.Container.bindSubsystem(NavX.class);
        this.Container.bindSubsystem(AutoNav.class);
        this.Container.bindSubsystem(ComputerVision.class);

        this.Container.bindSubsystem(Drivetrain.class);
        this.Container.bindSubsystem(Shooter.class);
        this.Container.bindSubsystem(Magazine.class);
        this.Container.bindSubsystem(Climber.class);
        this.Container.bindSubsystem(Intake.class);

        //this.Container.bind(OI.class).asSingle();
    }

    public DiOpMode CreateTeleop() {
        try {
            return (DiOpMode) this.Container.instantiate(Teleop.class);
        } catch (Exception e) {
            System.out.println("Couldn't create teleop! the uh oh is listed belo:");
            e.printStackTrace();
        }

        return null;
    }

    public DiOpMode CreateAutonomous() {
        Class<?> chosenAuto = this.autoChooser.getSelected();

        if (chosenAuto == null) return new ShootCloseLowThenDrive();

        try {
            return (DiOpMode) this.Container.instantiate(chosenAuto);
        } catch (Exception e) {
            System.out.println("Couldn't create auto! the uh oh is listed belo:");
            e.printStackTrace();
        }

        return null;
    }

    public DiOpMode CreateTest() {
        try {
            return (DiOpMode) this.Container.instantiate(Test.class);
        } catch (Exception e) {
            System.out.println("Couldn't create teleop! the uh oh is listed belo:");
            e.printStackTrace();
        }

        return null;
    }
}
