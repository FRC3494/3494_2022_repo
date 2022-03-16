package frc.robot.commands.teleop;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiCommand;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class Climb extends DiCommand implements IInitializable, ITickable, IDisposable {
    @Inject
    OI oi;

    @Inject
    DriverStation.Alliance alliance;

    @Inject
    Climber climber;

    boolean autoClimberDeploy = false;
    int autoClimbStage = 0;
    Timer autoClimbTimer = new Timer();

    public void onInitialize() {
        autoClimbTimer.reset();
    }

    public void onTick() {
        System.out.println("clib");

        this.climber.runRaw(RobotConfig.Climber.PowerCurve(this.oi.GetClimberPower() * RobotConfig.Climber.ANALOG_CLIMB_SPEED + this.oi.GetClimberWithoutRachetPower() * RobotConfig.Climber.BINARY_CLIMB_SPEED));
        
        if (!this.autoClimberDeploy && this.oi.GetClimberAutoDeploy()) {
            this.autoClimbTimer.reset();
            this.autoClimbTimer.start();

            this.autoClimberDeploy = true;
        }

        if (this.autoClimberDeploy) this.runClimberAutoDeploy();
        else this.runClimberManual();
    }

    public void runClimberAutoDeploy() {
        this.climber.release(true);
        //this.intake.run(0.00001f, 0);

        switch (autoClimbStage) {
            case 0:
                if (this.autoClimbTimer.advanceIfElapsed(1000)) this.autoClimbStage++; 

                this.climber.runRaw(this.climberReleaseEquation(this.autoClimbTimer.get() * 1000));
            case 1:
                if (this.autoClimbTimer.advanceIfElapsed(3000)) this.autoClimbStage++; 

                this.climber.runRaw(1);
            default:
                this.autoClimberDeploy = false;
        }

    }

    public double climberReleaseEquation(double t) {
        return (1 / 90000) * Math.pow(t - 300, 2) - 1;
    }

    public void runClimberManual() {
        if (this.oi.GetClimberPower() < 0 || this.oi.GetClimberWithoutRachetPower() != 0) this.climber.release(true);
        if (this.oi.GetClimberPower() > 0) this.climber.release(false);
    }

    public void onDispose() {
        this.climber.run(0);
    }
}
