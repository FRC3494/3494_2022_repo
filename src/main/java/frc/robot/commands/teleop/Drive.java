package frc.robot.commands.teleop;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiCommand;
import frc.robot.OI;
import frc.robot.subsystems.AutoNav;
import frc.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.DriverStation;

public class Drive extends DiCommand implements ITickable, IDisposable {
    @Inject
    OI oi;

    @Inject
    DriverStation.Alliance alliance;

    @Inject
    Drivetrain drivetrain;

    @Inject
    AutoNav autoNav;

    boolean singing = false;
    boolean startedSinging = false;

    public void onTick() {
        System.out.println("vroom vroom");

        if (this.singing) {
            if (!this.startedSinging) {
                this.drivetrain.singTheTheme();
                this.startedSinging = true;
            }

            if (this.drivetrain.isDoneSinging()) {
                this.singing = false;
                this.startedSinging = false;
            }

            return;
        }

        //this.drivetrain.run(RobotConfig.Drivetrain.PowerCurve(this.oi.GetLeftDriveSpeed()), RobotConfig.Drivetrain.PowerCurve(this.oi.GetRightDriveSpeed()));

        this.drivetrain.run((this.oi.GetLeftDriveSpeed()), (this.oi.GetRightDriveSpeed()));

        //if (this.oi.StartSinging()) this.singing = true;
    }

    public void onDispose() {
        this.drivetrain.run(0, 0);
    }
}
