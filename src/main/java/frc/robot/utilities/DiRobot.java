package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.utilities.di.DiContainer;

public abstract class DiRobot extends TimedRobot {
    public DiContainer Container = new DiContainer();

    DiOpMode currentOpMode = null;

    public abstract void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException;

    public abstract DiOpMode CreateTeleop();
    public abstract DiOpMode CreateAutonomous();
    public abstract DiOpMode CreateTest();

    @Override
    public void robotInit() {
        try {
            this.Install();
            
            this.Container.onInject();
        } catch (Exception e) {
            System.out.println("Failed to start main robot.");

            e.printStackTrace();

            return;
        }
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();

        this.Container.onTick();

        if (this.currentOpMode != null) this.currentOpMode.Container.onTick();
    }

    @Override
    public void disabledInit() {
        if (this.currentOpMode != null) this.currentOpMode.Container.onDispose();

        this.currentOpMode = null;
    }

    @Override
    public void autonomousInit() {
        if (this.currentOpMode != null) this.disabledInit();

        DriverStation.Alliance alliance = DriverStation.getAlliance();

        this.currentOpMode = CreateAutonomous();
        
        this.currentOpMode.Container.setParent(this.Container);
        
        this.currentOpMode.Container.bindInstance(this.currentOpMode);
        this.currentOpMode.Container.bindInstance(alliance);

        if (this.currentOpMode != null) {
            try {
                this.currentOpMode.install();

                this.currentOpMode.Container.onInject();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                System.out.println("Failed to start the autonomous OpMode.");

                e.printStackTrace();

                return;
            }
        }
    }

    @Override
    public void testInit() {
        if (this.currentOpMode != null) this.disabledInit();

        DriverStation.Alliance alliance = DriverStation.getAlliance();

        this.currentOpMode = CreateTest();

        this.currentOpMode.Container.setParent(this.Container);

        this.currentOpMode.Container.bindInstance(this.currentOpMode);
        this.currentOpMode.Container.bindInstance(alliance);
        
        try {
            this.currentOpMode.install();

            this.currentOpMode.Container.onInject();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.out.println("Failed to start the teleop OpMode.");

            e.printStackTrace();

            return;
        }
    }
    
    @Override
    public void teleopInit() {
        if (this.currentOpMode != null) this.disabledInit();

        DriverStation.Alliance alliance = DriverStation.getAlliance();

        this.currentOpMode = CreateTeleop();

        this.currentOpMode.Container.setParent(this.Container);

        this.currentOpMode.Container.bindInstance(this.currentOpMode);
        this.currentOpMode.Container.bindInstance(alliance);
        
        try {
            this.currentOpMode.install();

            this.currentOpMode.Container.onInject();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            System.out.println("Failed to start the teleop OpMode.");

            e.printStackTrace();

            return;
        }
    }
}
