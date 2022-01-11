// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.reflect.InvocationTargetException;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
        Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.leftMaster)).WithId("drivetrainLeftMaster");
        Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.leftSlave)).WithId("drivetrainLeftSlave");
        Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.rightMaster)).WithId("drivetrainRightMaster");
        Container.BindInstance(new TalonFX(RobotConfig.Drivetrain.rightSlave)).WithId("drivetrainRightSlave");

        Container.BindInstance(new TalonFX(RobotConfig.Shooter.shooterMotor)).WithId("shooterMotor");

        Container.BindInstance(new TalonSRX(RobotConfig.Intake.frontIntakeMotor)).WithId("frontIntakeMotor");
        Container.BindInstance(new TalonSRX(RobotConfig.Intake.frontIntakeInnerMotor)).WithId("frontIntakeInnerMotor");
        Container.BindInstance(new TalonSRX(RobotConfig.Intake.backIntakeMotor)).WithId("backIntakeMotor");

        Container.BindInstance(new TalonSRX(RobotConfig.Magazine.leftTreeUpperMotor)).WithId("leftTreeUpperMotor");
        Container.BindInstance(new TalonSRX(RobotConfig.Magazine.leftTreeLowerMotor)).WithId("leftTreeLowerMotor");
        Container.BindInstance(new Linebreaker(RobotConfig.Magazine.leftTreeLinebreak)).WithId("leftTreeLinebreak");

        Container.BindInstance(new TalonSRX(RobotConfig.Magazine.rightTreeUpperMotor)).WithId("rightTreeUpperMotor");
        Container.BindInstance(new TalonSRX(RobotConfig.Magazine.rightTreeLowerMotor)).WithId("rightTreeLowerMotor");
        Container.BindInstance(new Linebreaker(RobotConfig.Magazine.rightTreeUpperMotor)).WithId("rightTreeLinebreak");

        Container.BindInstance(new TalonSRX(RobotConfig.Magazine.treeStemLeftMotor)).WithId("treeStemLeftMotor");
        Container.BindInstance(new TalonSRX(RobotConfig.Magazine.treeStemRightMotor)).WithId("treeStemRightMotor");
        Container.BindInstance(new Linebreaker(RobotConfig.Magazine.treeStemLinebreak)).WithId("treeStemLinebreak");

        Container.BindInstance(new CANSparkMax(RobotConfig.Climber.leftClimbMotor, MotorType.kBrushless)).WithId("leftClimbMotor");
        Container.BindInstance(new CANSparkMax(RobotConfig.Climber.rightClimbMotor, MotorType.kBrushless)).WithId("rightClimbMotor");
        Container.BindInstance(new Servo(RobotConfig.Climber.climberReleaseServo)).WithId("climberReleaseServo");
        
        Container.Bind(Drivetrain.class);
        Container.Bind(Shooter.class);
        Container.Bind(Magazine.class);
        Container.Bind(Intake.class);
        Container.Bind(Climber.class);
    }
}
