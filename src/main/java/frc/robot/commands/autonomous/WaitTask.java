package frc.robot.commands.autonomous;

import frc.robot.utilities.AutoTask;

public class WaitTask extends AutoTask {
    long millis;

    long endTime;

    public WaitTask(long millis) {
        this.millis = millis;
    }

    @Override
    public void begin() {
        this.endTime = System.currentTimeMillis() + this.millis;
    }

    @Override
    public boolean execute() {
        return (this.endTime < System.currentTimeMillis());
    }

    @Override
    public void stop() { }

    @Override
    public ETA getETA() {
        return new ETA(this.endTime - this.millis, System.currentTimeMillis(), this.endTime);
    }
}