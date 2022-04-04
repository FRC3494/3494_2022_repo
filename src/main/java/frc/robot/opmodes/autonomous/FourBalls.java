package frc.robot.opmodes.autonomous;

import frc.robot.RobotConfig;
import frc.robot.commands.autonomous.AsyncShootTask;
import frc.robot.commands.autonomous.IntakeTask;
import frc.robot.commands.autonomous.MovementTask;
import frc.robot.commands.autonomous.ShootBallTask;
import frc.robot.commands.autonomous.ShootTask;
import frc.robot.commands.autonomous.WaitTask;
import frc.robot.utilities.AutoOpMode;
import frc.robot.utilities.ShooterSetting;

public class FourBalls extends AutoOpMode {
    @Override
    public void sequence() {
        //this.Sequencer.queue(new ShootTask(RobotConfig.Shooter.RPMS.get(1)));
        //this.Sequencer.queue(new ShootTask(RobotConfig.Shooter.RPMS.get(3)));
        this.Sequencer.queue(new AsyncShootTask(RobotConfig.Shooter.RPMS.get(3)));
        this.Sequencer.queue(new IntakeTask(0, RobotConfig.Magazine.INTAKE_SPEED));
        this.Sequencer.queue(new MovementTask(3000, -1, 0, 0.3));
        //this.Sequencer.queue(new MovementTask(1083, 1, 0, 0.35));
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new WaitTask(100));
        this.Sequencer.queue(new MovementTask(4000, -1, 0, 0.4));
        this.Sequencer.queue(new IntakeTask(RobotConfig.Magazine.INTAKE_SPEED, RobotConfig.Magazine.INTAKE_SPEED));
        this.Sequencer.queue(new MovementTask(4000, 1, 0, 0.4));
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new WaitTask(100));
        this.Sequencer.queue(new IntakeTask(0, 0));
        this.Sequencer.queue(new ShootTask(ShooterSetting.Off));
    }
}
