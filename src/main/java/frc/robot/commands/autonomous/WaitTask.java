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
        this.endTime = System.currentTimeMillis() + this.millis;
    }

    @Override
    public boolean Execute() {
        return (this.endTime < System.currentTimeMillis());
    }

    @Override
    public void Stop() { }

    @Override
    public ETA GetETA() {
        return new ETA(this.endTime - this.millis, System.currentTimeMillis(), this.endTime);
    }
}