package frc.robot.commands.autonomous;

import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class MovementTask extends AutoTask {
    @Inject
    Drivetrain drivetrain;

    long millis;
    double left;
    double right;
    double speed;

    long now;
    long startTime;
    long stopTime;
    long rampDuration;
    long rampUpTime;
    long rampDownTime;

    public MovementTask(long millis, double forwards, double rotation, double speed) {
        this.millis = millis;

        this.left = forwards + rotation;
        this.right = forwards - rotation;
        
        this.speed = Math.max(-1.0f, Math.min(1.0f, speed));
    }

    public MovementTask(long millis, double leftSpeed, double rightSpeed) {
        this.millis = millis;

        this.left = leftSpeed;
        this.right = rightSpeed;

        this.speed = 1;
    }

    @Override
    public void Begin() {
        now = System.currentTimeMillis();

        startTime = now;
        stopTime = startTime + millis;

        rampDuration = (long) (0.4 * millis);
        rampUpTime = startTime + rampDuration;
        rampDownTime = stopTime - rampDuration;
    }

    @Override
    public boolean Execute() {
        now = System.currentTimeMillis();

        if (now >= stopTime) return true;

        double computedSpeed = speed;

        if (now < rampUpTime){
            computedSpeed = (speed * Math.min(((float)(now-startTime)/(float)(rampUpTime-startTime)), 1));
        } else if (now > rampDownTime) {
            computedSpeed = (speed * (1 - ((float)(now - rampDownTime)/(float)(stopTime - rampDownTime))));
        }
        
        drivetrain.TankDrive(left * computedSpeed, right * computedSpeed);

        return false;
    }

    @Override
    public void Stop() {
        drivetrain.TankDrive(0, 0);
    }

    @Override
    public ETA GetETA() {
        return new ETA(startTime, now, stopTime);
    }
}
