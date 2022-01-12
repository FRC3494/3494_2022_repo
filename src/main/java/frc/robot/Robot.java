// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.reflect.InvocationTargetException;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.sensors.Linebreaker;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.DiRobot;

public class Robot extends DiRobot {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //Container.BindInstance(new Compressor(0));

        this.container.bindInstance(
            new TalonFX(RobotConfig.Drivetrain.LEFT_LEADER_MOTOR_ID)
        ).withId("drivetrainLeftLeader");

        this.container.bindInstance(
            new TalonFX(RobotConfig.Drivetrain.LEFT_FOLLOWER_MOTOR_ID)
        ).withId("drivetrainLeftFollower");

        this.container.bindInstance(
            new TalonFX(RobotConfig.Drivetrain.RIGHT_LEADER_MOTOR_ID)
        ).withId("drivetrainRightLeader");

        this.container.bindInstance(
            new TalonFX(RobotConfig.Drivetrain.RIGHT_FOLLOWER_MOTOR_ID)
        ).withId("drivetrainRightFollower");

        this.container.bindInstance(
            new TalonFX(RobotConfig.Shooter.SHOOTER_MOTOR_ID)
        ).withId("shooterMotor");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Intake.FRONT_INTAKE_MOTOR_ID)
        ).withId("frontIntakeMotor");
        
        this.container.bindInstance(
            new TalonSRX(RobotConfig.Intake.FRONT_INNER_MOTOR_ID)
        ).withId("frontIntakeInnerMotor");
        
        this.container.bindInstance(
            new TalonSRX(RobotConfig.Intake.BACK_INTAKE_MOTOR_ID)
        ).withId("backIntakeMotor");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Magazine.LEFT_TREE_UPPER_MOTOR_ID)
        ).withId("leftTreeUpperMotor");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Magazine.LEFT_TREE_LOWER_MOTOR_ID)
        ).withId("leftTreeLowerMotor");

        this.container.bindInstance(
            new Linebreaker(RobotConfig.Magazine.LEFT_TREE_LINEBREAK_ID)
        ).withId("leftTreeLinebreak");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Magazine.RIGHT_TREE_UPPER_MOTOR_ID)
        ).withId("rightTreeUpperMotor");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Magazine.RIGHT_TREE_LOWER_MOTOR_ID)
        ).withId("rightTreeLowerMotor");

        this.container.bindInstance(
            new Linebreaker(RobotConfig.Magazine.RIGHT_TREE_UPPER_MOTOR_ID)
        ).withId("rightTreeLinebreak");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Magazine.STEM_LEFT_MOTOR_ID)
        ).withId("treeStemLeftMotor");

        this.container.bindInstance(
            new TalonSRX(RobotConfig.Magazine.STEM_RIGHT_MOTOR_ID)
        ).withId("treeStemRightMotor");

        this.container.bindInstance(
            new Linebreaker(RobotConfig.Magazine.STEM_LINEBREAK_ID)
        ).withId("treeStemLinebreak");

        this.container.bindInstance(
            new CANSparkMax(RobotConfig.Climber.LEFT_CLIMB_MOTOR_ID, MotorType.kBrushless)
        ).withId("leftClimbMotor");

        this.container.bindInstance(
            new CANSparkMax(RobotConfig.Climber.RIGHT_CLIMB_MOTOR_ID, MotorType.kBrushless)
        ).withId("rightClimbMotor");

        this.container.bindInstance(
            new Servo(RobotConfig.Climber.CLIMBER_RELEASE_SERVO_ID)
        ).withId("climberReleaseServo");
        
        this.container.bind(Drivetrain.class);
        this.container.bind(Shooter.class);
        this.container.bind(Magazine.class);
        this.container.bind(Intake.class);
        this.container.bind(Climber.class);
    }
}
