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
        endTime = System.currentTimeMillis() + millis;
    }

    @Override
    public boolean execute() {
        return (endTime < System.currentTimeMillis());
    }

    @Override
    public void stop() { }

    @Override
    public ETA getETA() {
        return new ETA(endTime - millis, System.currentTimeMillis(), endTime);
    }
}