package frc.robot.opmodes.autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.commands.autonomous.OdometryMovement;
import frc.robot.utilities.AutoOpMode;

public class TestODO extends AutoOpMode {
    @Override
    public void sequence() {
        this.Sequencer.queue(new OdometryMovement(new Pose2d(2, 2, new Rotation2d(0))));
        /*this.Sequencer.queue(new ShootTask(RobotConfig.Shooter.RPMS.get(1)));
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new WaitTask(100));
        this.Sequencer.queue(new ShootTask(RobotConfig.Shooter.RPMS.get(3)));
        //this.Sequencer.queue(new ShootTask(new ShooterSetting("extreme far woah", 2925, true, false)));
        this.Sequencer.queue(new IntakeTask(0, RobotConfig.Magazine.INTAKE_SPEED));
        this.Sequencer.queue(new MovementTask(3250, -1, 0, 0.35));
        this.Sequencer.queue(new IntakeTask(0, 0));
        this.Sequencer.queue(new MovementTask(1083, 1, 0, 0.35));
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new WaitTask(100));
        this.Sequencer.queue(new ShootTask(ShooterSetting.Off));*/
        
    }
}
