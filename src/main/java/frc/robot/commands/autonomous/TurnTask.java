package frc.robot.commands.autonomous;

import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class TurnTask extends AutoTask {
    @Inject
    Drivetrain drivetrain;

    @Inject
    NavX navx;

    double power;
    double relativeAngle;

    double startAngle;
    double endAngle;

    public TurnTask(double power, double relativeAngle) {
        this.power = power;
        this.relativeAngle = relativeAngle;
        
    }

    @Override
    public void begin() {
        this.startAngle = navx.getYaw();
        this.endAngle = this.startAngle + relativeAngle;
    }

    @Override
    public boolean execute() {
        if(this.navx.getYaw() > this.endAngle){
            drivetrain.arcadeDrive(0.0, this.power);
        }
        else if (this.navx.getYaw() < this.endAngle){
            drivetrain.arcadeDrive(0.0, -this.power);
        }
        else if(Math.abs(this.navx.getYaw()-this.endAngle)< 2){
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        drivetrain.arcadeDrive(0.0, 0.0);
    }

    @Override
    public ETA getETA() {
        return new ETA();
    }
}
