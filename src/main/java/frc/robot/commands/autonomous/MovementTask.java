package frc.robot.commands.autonomous;

import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.AutoTask;
import com.fizzyapple12.javadi.DiContainer.Inject;

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
    public void begin() {
        this.now = System.currentTimeMillis();

        this.startTime = now;
        this.stopTime = this.startTime + this.millis;

        this.rampDuration = (long) (0.4 * this.millis);
        this.rampUpTime = this.startTime + this.rampDuration;
        this.rampDownTime = this.stopTime - this.rampDuration;
    }

    @Override
    public boolean execute() {
        this.now = System.currentTimeMillis();

        if (this.now >= this.stopTime) return true;

        double computedSpeed = this.speed;

        if (this.now < this.rampUpTime){
            computedSpeed = (this.speed * Math.min(((float)(this.now-this.startTime)/(float)(this.rampUpTime-this.startTime)), 1));
        } else if (now > rampDownTime) {
            computedSpeed = (this.speed * (1 - ((float)(this.now - this.rampDownTime)/(float)(this.stopTime - this.rampDownTime))));
        }
        
        this.drivetrain.tankDrive(this.left * computedSpeed, this.right * computedSpeed);

        return false;
    }

    @Override
    public void stop() {
        this.drivetrain.tankDrive(0, 0);
    }

    @Override
    public ETA getETA() {
        return new ETA(this.startTime, this.now, this.stopTime);
    }
}
