package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.opmodes.teleop.Teleop;
import frc.robot.utilities.di.DiContainer;

public abstract class DiRobot extends TimedRobot {
    public DiContainer Container = new DiContainer();

    public DiOpMode currentOpMode = null;

    public abstract void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Override
    public void robotInit() {
        try {
            Install();
        } catch (Exception e) {
            System.out.println("Failed to start main robot.");
            e.printStackTrace();
        }

        Container.Initialize();
    }
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();

        Container.Tick();
    }

    @Override
    public void disabledInit() {
        if (currentOpMode != null) {
            currentOpMode.Container.Dispose();

            currentOpMode = null;
        }
    }

    @Override
    public void autonomousInit() {
        if (currentOpMode != null) disabledInit();

        //Get Auto OpMode from ShuffleBoard
        
        currentOpMode.Container.BindInstance(currentOpMode);

        if (currentOpMode != null) {
            try {
                currentOpMode.Install();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                System.out.println("Failed to start the autonomous OpMode.");
                e.printStackTrace();
            }

            currentOpMode.Container.Initialize();
        }
    }
    @Override
    public void autonomousPeriodic() {
        if (currentOpMode != null) currentOpMode.Container.Tick();
    }

    @Override
    public void teleopInit() {
        if (currentOpMode != null) disabledInit();

        DriverStation.Alliance alliance = DriverStation.getAlliance();

        currentOpMode = new Teleop();

        currentOpMode.Container.BindInstance(currentOpMode);
        currentOpMode.Container.BindInstance(alliance);
        
        try {
            currentOpMode.Install();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.out.println("Failed to start the autonomous OpMode.");
            e.printStackTrace();
        }

        currentOpMode.Container.Initialize();
    }
    @Override
    public void teleopPeriodic() {
        if (currentOpMode != null) currentOpMode.Container.Tick();
    }
}
