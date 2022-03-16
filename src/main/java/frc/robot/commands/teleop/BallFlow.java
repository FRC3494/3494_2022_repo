package frc.robot.commands.teleop;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiCommand;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.DriverStation;

public class BallFlow extends DiCommand implements ITickable, IDisposable {
    @Inject
    OI oi;

    @Inject
    DriverStation.Alliance alliance;

    @Inject
    Intake intake;

    @Inject
    Magazine magazine;

    boolean autoClimberDeploy = false; // fix this

    public void onTick() {
        System.out.println("balls");
        
        double frontIntakeSpeed = (this.oi.GetClimberPower() == 0 && this.oi.GetClimberWithoutRachetPower() == 0) ? RobotConfig.Intake.PowerCurve(this.oi.GetFrontIntakePower()) * RobotConfig.Intake.INTAKE_SPEED : 0.00001f;
        double backIntakeSpeed = RobotConfig.Intake.PowerCurve(this.oi.GetBackIntakePower()) * RobotConfig.Intake.INTAKE_SPEED;

        if (!this.autoClimberDeploy) this.intake.run(frontIntakeSpeed, backIntakeSpeed);

        this.magazine.setAutoOperate(!this.oi.GetOverrideMagazineStateMachine());

        if (this.oi.GetOverrideMagazineStateMachine()) {
            double leftMagazineSpeed = this.oi.GetLeftTreeMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
            double rightMagazineSpeed = this.oi.GetRightTreeMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
            double stemMagazineSpeed = this.oi.GetTreeStemMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
    
            if (this.oi.GetNeedOuttake()) {
                leftMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
                rightMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
                stemMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
            }
    
            this.magazine.run(leftMagazineSpeed, rightMagazineSpeed, stemMagazineSpeed);
        } else {
            if (this.oi.QueueBall()) this.magazine.sendBall();

            this.magazine.useFastSpeed(this.oi.GetFrontIntakePower() > 0 || this.oi.GetBackIntakePower() > 0);
        }
    }

    public void onDispose() {
        this.intake.run(0, 0);
        this.magazine.runRaw(0, 0, 0);
    }
}
