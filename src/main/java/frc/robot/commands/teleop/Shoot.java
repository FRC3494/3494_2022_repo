package frc.robot.commands.teleop;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiCommand;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.DriverStation;

public class Shoot extends DiCommand implements ITickable, IDisposable {
    @Inject
    OI oi;

    @Inject
    DriverStation.Alliance alliance;

    @Inject
    Shooter shooter;

    int selectedRPM = 0;
    double turretPosition = 0;

    public void onTick() {
        System.out.println("pew pew");

        this.shooter.run(RobotConfig.Shooter.RPMPowerCurve(this.oi.GetShooterPower()) * RobotConfig.Shooter.RPMS.get(this.selectedRPM).Value);
        
        this.turretPosition = RobotConfig.Shooter.TurretPowerCurve(this.oi.GetTurretPower()) * RobotConfig.Shooter.TURRET_SPEED;
        if (this.oi.GetTurretGoToFront()) this.turretPosition = RobotConfig.Shooter.TURRET_FRONT_POSITION;
        if (this.oi.GetTurretGoToBack()) this.turretPosition = RobotConfig.Shooter.TURRET_BACK_POSITION;
        this.shooter.runTurret(this.turretPosition);

        if (this.oi.ToggleAimBot()) {
            if (this.shooter.aimbotEnabled()) this.shooter.disableAimBot();
            else this.shooter.enableAimBot();
        }

        if (this.oi.GetSwitchRPM()) {
            this.selectedRPM++;
            if (this.selectedRPM >= RobotConfig.Shooter.RPMS.size()) this.selectedRPM = 0;
        }

        if (this.oi.ToggleShooterHood()) this.shooter.setHood(!this.shooter.getHood());
    }

    public void onDispose() {
        this.shooter.run(0);
        this.shooter.runTurret(0);
        this.shooter.disableAimBot();
    }
}
