package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.opmodes.teleop.Teleop;
import frc.robot.utilities.di.DiContainer;

public abstract class DiRobot extends TimedRobot {
    public DiContainer Container = new DiContainer();

    DiOpMode currentOpMode = null;

    public abstract void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Override
    public void robotInit() {
        try {
            this.Install();
        } catch (Exception e) {
            System.out.println("Failed to start main robot.");
            e.printStackTrace();
        }

        this.Container.onInject();
    }
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();

        this.Container.onTick();
    }

    @Override
    public void disabledInit() {
        if (this.currentOpMode != null) {
            this.currentOpMode.Container.onDispose();

            this.currentOpMode = null;
        }
    }

    @Override
    public void autonomousInit() {
        if (this.currentOpMode != null) this.disabledInit();

        //Get Auto OpMode from ShuffleBoard
        
        this.currentOpMode.Container.bindInstance(this.currentOpMode);

        if (this.currentOpMode != null) {
            try {
                this.currentOpMode.install();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                System.out.println("Failed to start the autonomous OpMode.");
                e.printStackTrace();
            }

            this.currentOpMode.Container.onInject();
        }
    }
    @Override
    public void autonomousPeriodic() {
        if (this.currentOpMode != null) this.currentOpMode.Container.onTick();
    }

    @Override
    public void teleopInit() {
        if (this.currentOpMode != null) this.disabledInit();

        DriverStation.Alliance alliance = DriverStation.getAlliance();

        this.currentOpMode = new Teleop();

        this.currentOpMode.Container.bindInstance(this.currentOpMode);
        this.currentOpMode.Container.bindInstance(alliance);
        
        try {
            this.currentOpMode.install();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.out.println("Failed to start the autonomous OpMode.");
            e.printStackTrace();
        }

        this.currentOpMode.Container.onInject();
    }
    @Override
    public void teleopPeriodic() {
        if (this.currentOpMode != null) this.currentOpMode.Container.onTick();
    }
}
