package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.sensors.Linebreaker;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class Magazine extends DiSubsystem implements IInitializable, ITickable, IDisposable {
    private TalonSRX leftTreeMotor = new TalonSRX(RobotMap.Magazine.LEFT_TREE_MOTOR_CHANNEL);;
    private Linebreaker leftTreeLinebreak = new Linebreaker(RobotMap.Magazine.LEFT_TREE_LINEBREAK_CHANNEL, true);
    
    private TalonSRX rightTreeMotor = new TalonSRX(RobotMap.Magazine.RIGHT_TREE_MOTOR_CHANNEL);
    private Linebreaker rightTreeLinebreak = new Linebreaker(RobotMap.Magazine.RIGHT_TREE_LINEBREAK_CHANNEL, true);
    
    private TalonSRX treeStemMotor = new TalonSRX(RobotMap.Magazine.TREE_STEM_MOTOR_CHANNEL);
    private Linebreaker treeStemLinebreak = new Linebreaker(RobotMap.Magazine.TREE_STEM_LINEBREAK_CHANNEL, true);

    boolean autoOperate = true;

    public void onInitialize() {
        this.leftTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.rightTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.treeStemMotor.setNeutralMode(NeutralMode.Brake);

        this.leftTreeMotor.setInverted(true);
        this.rightTreeMotor.setInverted(false);
        this.treeStemMotor.setInverted(false);
    }

    public void setAutoOperate(boolean autoOperate) {
        this.autoOperate = autoOperate;
    }

    public void sendBall() {
        int maxBallRequests = 0;
        if (this.treeStemLinebreak.Broken()) maxBallRequests++;
        if (this.leftTreeLinebreak.Broken() || this.rightTreeLinebreak.Broken()) maxBallRequests++;

        this.sendBalls++;

        if (this.sendBalls > maxBallRequests) this.sendBalls = maxBallRequests;
    }

    public void useFastSpeed(boolean use) {
        this.runFast = use;
    }

    public void run(double leftPower, double rightPower, double verticalPower) {
        if (this.autoOperate) return;

        this.runRaw(leftPower, rightPower, verticalPower);
    }

    public void runRaw(double leftPower, double rightPower, double verticalPower) {
        this.runHorizontals(leftPower, rightPower);
        this.runVertical(verticalPower);
    }

    public void runHorizontals(double leftPower, double rightPower) {
        this.leftTreeMotor.set(ControlMode.PercentOutput, leftPower);
        this.rightTreeMotor.set(ControlMode.PercentOutput, rightPower);
    }

    public void runVertical(double verticalPower) {
        this.treeStemMotor.set(ControlMode.PercentOutput, verticalPower);
    }

    boolean ejectingBall = false;
    Timer ejectingBallTimer = new Timer();
    boolean ejectLeft = true;

    private void ejectBall() {
        if (this.ejectingBallTimer.advanceIfElapsed(RobotConfig.Magazine.SECONDS_TO_EJECT_HORIZONTAL)) {
            this.ejectingBall = false;
            this.ejectingBallTimer.stop();
            this.ejectingBallTimer.reset();
        }

        if (this.ejectLeft) this.runRaw(RobotConfig.Magazine.OUTTAKE_SPEED, 0, 0);
        else this.runRaw(0, RobotConfig.Magazine.OUTTAKE_SPEED, 0);
    }

    boolean reloadingVertical = true;
    Timer reloadVertialTimer = new Timer();
    int reloadStage = 0;

    private void reloadVertical() {
        double horizontalSpeed = runFast ? RobotConfig.Magazine.INTAKE_SPEED : RobotConfig.Magazine.IDLE_SPEED;

        switch (this.reloadStage) {
            case 0:
                this.runRaw(horizontalSpeed, horizontalSpeed, RobotConfig.Magazine.IDLE_SPEED);

                if (this.treeStemLinebreak.Broken()) {
                    this.reloadVertialTimer.start();
                    this.reloadStage++;
                }
                break;
            case 1: 
                this.runRaw(0, 0, RobotConfig.Magazine.IDLE_SPEED);

                if (this.reloadVertialTimer.advanceIfElapsed(RobotConfig.Magazine.SECONDS_RELOAD_RUN_UP)) this.reloadStage++;
                break;
            case 2: 
                this.runRaw(0, 0, -RobotConfig.Magazine.IDLE_SPEED);

                if (this.reloadVertialTimer.advanceIfElapsed(RobotConfig.Magazine.SECONDS_RELOAD_RUN_DOWN)) this.reloadStage++;
                break;
            default:
                this.runRaw(0, 0, 0);
                this.reloadingVertical = false;
                this.reloadStage = 0;
                this.reloadVertialTimer.stop();
                this.reloadVertialTimer.reset();
        }
    }

    int sendBalls = 0;
    boolean startedSendTimer = false;
    Timer sendBallTimer = new Timer();

    private void sendBallMagazine() {
        this.runRaw(0, 0, RobotConfig.Magazine.LAUNCH_SPEED);

        if (!this.treeStemLinebreak.Broken() && !this.startedSendTimer) {
            this.sendBallTimer.start();
            this.startedSendTimer = true;
        }
        
        if (this.sendBallTimer.advanceIfElapsed(RobotConfig.Magazine.SECONDS_TO_SEND_BALL)) {
            this.runRaw(0, 0, 0);
            this.sendBalls--;
            if (this.sendBalls < 0) this.sendBalls = 0;
            this.reloadingVertical = true;
            this.sendBallTimer.stop();
            this.sendBallTimer.reset();
            this.startedSendTimer = false;
        }
    }

    boolean runFast = false;

    public void onTick() {
        if (!this.autoOperate) return;

        //System.out.println(this.autoOperate + "\t\t" + this.ejectingBall + "\t\t" + this.reloadingVertical + "\t\t" + this.reloadStage + "\t\t" + this.sendBalls + "\t\t" + this.runFast);

        if (this.ejectingBall) { 
            this.ejectingBallTimer.start();
            this.ejectBall();
            return;
        }
        if (this.treeStemLinebreak.Broken() && this.leftTreeLinebreak.Broken() && this.rightTreeLinebreak.Broken()) this.ejectingBall = true;

        if (this.sendBalls > 0 && !this.reloadingVertical) {
            this.sendBallMagazine();
            return;
        }

        if (this.reloadingVertical) {
            this.reloadVertical();
            return;
        }
        if (!this.treeStemLinebreak.Broken()) this.reloadingVertical = true;

        double horizontalSpeed = runFast ? RobotConfig.Magazine.INTAKE_SPEED : RobotConfig.Magazine.IDLE_SPEED;
        if (this.leftTreeLinebreak.Broken()) {
            this.ejectLeft = false;
            horizontalSpeed = 0;
        }
        if (this.rightTreeLinebreak.Broken()) {
            this.ejectLeft = true;
            horizontalSpeed = 0;
        }
        this.runHorizontals(horizontalSpeed, horizontalSpeed);
    }

    public void onDispose() {
        this.runRaw(0, 0, 0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Magazine");

        builder.setActuator(true);

        builder.addDoubleProperty("Left Tree Speed", this.leftTreeMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Left Tree Full", this.leftTreeLinebreak::Broken, (boolean value) -> {});

        builder.addDoubleProperty("Right Tree Speed", this.rightTreeMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Right Tree Full", this.rightTreeLinebreak::Broken, (boolean value) -> {});

        builder.addDoubleProperty("Tree Stem Speed", this.treeStemMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Tree Stem Full", this.treeStemLinebreak::Broken, (boolean value) -> {});
    }
}
