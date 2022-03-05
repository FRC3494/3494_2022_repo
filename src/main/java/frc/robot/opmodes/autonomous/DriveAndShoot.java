package frc.robot.opmodes.autonomous;

import frc.robot.RobotConfig;
import frc.robot.commands.autonomous.MagazineTask;
import frc.robot.commands.autonomous.MovementTask;
import frc.robot.commands.autonomous.ShootTask;
import frc.robot.commands.autonomous.WaitTask;
import frc.robot.utilities.AutoOpMode;

public class DriveAndShoot extends AutoOpMode {
    @Override
    public void sequence() {
        this.Sequencer.queue(new ShootTask(RobotConfig.Shooter.RPMS.get(0).Value));
        this.Sequencer.queue(new WaitTask(3000));
        this.Sequencer.queue(new MagazineTask(RobotConfig.Magazine.INTAKE_SPEED, RobotConfig.Magazine.INTAKE_SPEED, RobotConfig.Magazine.INTAKE_SPEED, true));
        this.Sequencer.queue(new WaitTask(5000));
        this.Sequencer.queue(new ShootTask(0));
        this.Sequencer.queue(new MovementTask(5000, 1, 0, 0.3));
    }
}
