// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.reflect.InvocationTargetException;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.utilities.DiRobot;

public class Robot extends DiRobot {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        //Container.BindInstance(new TalonFX(deviceNumber));
    }
}
