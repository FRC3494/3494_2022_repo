package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.opmodes.teleop.Teleop;
import frc.robot.utilities.di.DiContainer;

public abstract class DiRobot extends TimedRobot {
    public DiContainer container = new DiContainer();

    public DiOpMode currentOpMode = null;

    public abstract void install() throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Override
    public void robotInit() {
        try {
            install();
        } catch (Exception e) {
            System.out.println("Failed to start main robot.");
            e.printStackTrace();
        }

        this.container.initialize();
    }
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();

        this.container.tick();
    }

    @Override
    public void disabledInit() {
        if (this.currentOpMode != null) {
            this.currentOpMode.m_container.dispose();
            this.currentOpMode = null;
        }
    }

    @Override
    public void autonomousInit() {
        if (this.currentOpMode != null) { 
            disabledInit();
        }

        //Get Auto OpMode from ShuffleBoard
        
        this.currentOpMode.m_container.bindInstance(this.currentOpMode);

        if (currentOpMode != null) {
            try {
                this.currentOpMode.install();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                System.out.println("Failed to start the autonomous OpMode.");
                e.printStackTrace();
            }

            this.currentOpMode.m_container.initialize();
        }
    }
    @Override
    public void autonomousPeriodic() {
        if (currentOpMode != null) {
            this.currentOpMode.m_container.tick();
        }
    }

    @Override
    public void teleopInit() {
        if (this.currentOpMode != null) {
            disabledInit();
        }

        DriverStation.Alliance alliance = DriverStation.getAlliance();

        this.currentOpMode = new Teleop();

        this.currentOpMode.m_container.bindInstance(currentOpMode);
        this.currentOpMode.m_container.bindInstance(alliance);
        
        try {
            currentOpMode.install();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.out.println("Failed to start the autonomous OpMode.");
            e.printStackTrace();
        }

        currentOpMode.m_container.initialize();
    }
    @Override
    public void teleopPeriodic() {
        if (currentOpMode != null) {
            currentOpMode.m_container.tick();
        }
    }
}
