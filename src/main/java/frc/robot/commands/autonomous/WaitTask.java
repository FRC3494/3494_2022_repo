package frc.robot.commands.autonomous;

import frc.robot.utilities.AutoTask;

public class WaitTask extends AutoTask {
    long millis;

    long endTime;

    public WaitTask(long millis) {
        this.millis = millis;
    }

    @Override
    public void Begin() {
        endTime = System.currentTimeMillis() + millis;
    }

    @Override
    public boolean Execute() {
        return (endTime < System.currentTimeMillis());
    }

    @Override
    public void Stop() { }

    @Override
    public ETA GetETA() {
        return new ETA(endTime - millis, System.currentTimeMillis(), endTime);
    }
}