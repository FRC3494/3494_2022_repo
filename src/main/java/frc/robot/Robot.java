package frc.robot;

import java.lang.reflect.InvocationTargetException;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.utilities.DiRobot;
import frc.robot.sensors.Linebreaker;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

public class Robot extends DiRobot {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //Container.BindInstance(new Compressor(0));
        this.Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.LeftMasterChannel)).WithId("drivetrainLeftMaster");
        this.Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.LeftSlaveChannel)).WithId("drivetrainLeftSlave");
        this.Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.RightMasterChannel)).WithId("drivetrainRightMaster");
        this.Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.RightSlaveChannel)).WithId("drivetrainRightSlave");

        this.Container.BindInstance(new TalonFX(RobotConfig.Shooter.ShooterMotorChannel)).WithId("shooterMotor");

        this.Container.BindInstance(new TalonSRX(RobotConfig.Intake.FrontIntakeMotorChannel)).WithId("frontIntakeMotor");
        this.Container.BindInstance(new TalonSRX(RobotConfig.Intake.FrontIntakeInnerMotorChannel)).WithId("frontIntakeInnerMotor");
        this.Container.BindInstance(new TalonSRX(RobotConfig.Intake.BackIntakeMotorChannel)).WithId("backIntakeMotor");

        this.Container.BindInstance(new TalonSRX(RobotConfig.Magazine.LeftTreeUpperMotorChannel)).WithId("leftTreeUpperMotor");
        this.Container.BindInstance(new TalonSRX(RobotConfig.Magazine.LeftTreeLowerMotorChannel)).WithId("leftTreeLowerMotor");
        this.Container.BindInstance(new Linebreaker(RobotConfig.Magazine.LeftTreeLinebreakChannel)).WithId("leftTreeLinebreak");

        this.Container.BindInstance(new TalonSRX(RobotConfig.Magazine.RightTreeUpperMotorChannel)).WithId("rightTreeUpperMotor");
        this.Container.BindInstance(new TalonSRX(RobotConfig.Magazine.RightTreeLowerMotorChannel)).WithId("rightTreeLowerMotor");
        this.Container.BindInstance(new Linebreaker(RobotConfig.Magazine.RightTreeUpperMotorChannel)).WithId("rightTreeLinebreak");

        this.Container.BindInstance(new TalonSRX(RobotConfig.Magazine.TreeStemLeftMotorChannel)).WithId("treeStemLeftMotor");
        this.Container.BindInstance(new TalonSRX(RobotConfig.Magazine.TreeStemRightMotorChannel)).WithId("treeStemRightMotor");
        this.Container.BindInstance(new Linebreaker(RobotConfig.Magazine.TreeStemLinebreakChannel)).WithId("treeStemLinebreak");

        this.Container.BindInstance(new CANSparkMax(RobotConfig.Climber.LeftClimbMotorChannel, MotorType.kBrushless)).WithId("leftClimbMotor");
        this.Container.BindInstance(new CANSparkMax(RobotConfig.Climber.RightClimbMotorChannel, MotorType.kBrushless)).WithId("rightClimbMotor");
        this.Container.BindInstance(new Servo(RobotConfig.Climber.ClimberReleaseServoChannel)).WithId("climberReleaseServo");
        
        this.Container.Bind(Drivetrain.class);
        this.Container.Bind(Shooter.class);
        this.Container.Bind(Magazine.class);
        this.Container.Bind(Intake.class);
        this.Container.Bind(Climber.class);
    }
}
