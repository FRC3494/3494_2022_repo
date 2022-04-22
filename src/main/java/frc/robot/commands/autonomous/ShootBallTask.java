package frc.robot.commands.autonomous;

import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import com.fizzyapple12.javadi.DiContainer.Inject;

public class ShootBallTask extends AutoTask {
    @Inject
    Magazine magazine;

    @Inject
    Shooter shooter;

    long endTime;

    public ShootBallTask() {
    }

    @Override
    public void begin() {
        this.endTime = System.currentTimeMillis() + 3000;

        this.magazine.sendBall(shooter.getSetting().feedThrough);
        this.magazine.reportShooterReady(this.shooter.atRPM() && this.shooter.isCentered() && !this.shooter.isZeroing());
    }
 
    @Override
    public boolean execute() {
        this.magazine.reportShooterReady(this.shooter.atRPM() && this.shooter.isCentered() && !this.shooter.isZeroing());
        return !this.magazine.getSendingBall() || (this.endTime < System.currentTimeMillis());
    }

    @Override
    public void stop() {}

    @Override
    public ETA getETA() {
        return new ETA();
    }
}
