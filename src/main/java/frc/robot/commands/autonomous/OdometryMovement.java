package frc.robot.commands.autonomous;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utilities.AutoTask;
import com.fizzyapple12.javadi.DiContainer.Inject;

public class OdometryMovement extends AutoTask {
    @Inject
    Drivetrain drivetrain;

    Pose2d position;

    public OdometryMovement(Pose2d position) {
        this.position = position;
        
    }

    @Override
    public void begin() {
        this.drivetrain.funnyprint();
        this.drivetrain.goToRelativePose(position);
    }

    @Override
    public boolean execute() {
        return !this.drivetrain.isAutoNav();
    }

    @Override
    public void stop() {
        this.drivetrain.funnyprint();
    }

    @Override
    public ETA getETA() {
        return new ETA();
    }
}
