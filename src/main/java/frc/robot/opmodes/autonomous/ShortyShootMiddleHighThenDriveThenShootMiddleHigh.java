package frc.robot.opmodes.autonomous;

import frc.robot.RobotConfig;
import frc.robot.commands.autonomous.AimbotTask;
import frc.robot.commands.autonomous.AsyncShootTask;
import frc.robot.commands.autonomous.IntakeTask;
import frc.robot.commands.autonomous.MovementTask;
import frc.robot.commands.autonomous.ShootBallTask;
import frc.robot.commands.autonomous.ShootTask;
import frc.robot.commands.autonomous.WaitTask;
import frc.robot.utilities.AutoOpMode;
import frc.robot.utilities.ShooterSetting;

public class ShortyShootMiddleHighThenDriveThenShootMiddleHigh extends AutoOpMode {
    @Override
    public void sequence() {
        this.Sequencer.queue(new AimbotTask(true));
        this.Sequencer.queue(new AsyncShootTask(RobotConfig.Shooter.RPMS.get(2)));
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new IntakeTask(0, RobotConfig.Magazine.INTAKE_SPEED));
        this.Sequencer.queue(new WaitTask(500));
        this.Sequencer.queue(new MovementTask(2250, -1, 0, 0.35));
        this.Sequencer.queue(new IntakeTask(0, 0));
        this.Sequencer.queue(new MovementTask(2200, 1, 0, 0.35));
        this.Sequencer.queue(new ShootBallTask());
        this.Sequencer.queue(new WaitTask(100));
        this.Sequencer.queue(new ShootTask(ShooterSetting.Off));
        this.Sequencer.queue(new IntakeTask(0, RobotConfig.Magazine.INTAKE_SPEED));
        this.Sequencer.queue(new WaitTask(500));
        this.Sequencer.queue(new MovementTask(1200, -1, 0, 0.5));
        this.Sequencer.queue(new IntakeTask(0, 0));
        this.Sequencer.queue(new AimbotTask(false));
    }
}
