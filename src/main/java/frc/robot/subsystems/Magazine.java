package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.sensors.Linebreaker;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class Magazine extends DiSubsystem implements IInitializable, ITickable, IDisposable {
    private TalonSRX leftTreeMotor = new TalonSRX(RobotMap.Magazine.LEFT_TREE_MOTOR_CHANNEL);;
    private Linebreaker leftTreeLinebreak = new Linebreaker(RobotMap.Magazine.LEFT_TREE_LINEBREAK_CHANNEL, true);
    
    private TalonSRX rightTreeMotor = new TalonSRX(RobotMap.Magazine.RIGHT_TREE_MOTOR_CHANNEL);
    private Linebreaker rightTreeLinebreak = new Linebreaker(RobotMap.Magazine.RIGHT_TREE_LINEBREAK_CHANNEL, true);
    
    private TalonSRX treeStemMotor = new TalonSRX(RobotMap.Magazine.TREE_STEM_MOTOR_CHANNEL);
    private Linebreaker treeStemLinebreak = new Linebreaker(RobotMap.Magazine.TREE_STEM_LINEBREAK_CHANNEL, true);

    private AddressableLED ballStatusLeds = new AddressableLED(RobotMap.Magazine.MAGAZINE_LED_PORT);
    private AddressableLEDBuffer ballStatusLedsBuffer = new AddressableLEDBuffer(RobotMap.Magazine.MAGAZINE_LED_PORT);

    boolean autoOperate = true;

    public void onInitialize() {
        this.leftTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.rightTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.treeStemMotor.setNeutralMode(NeutralMode.Brake);

        this.leftTreeMotor.setInverted(true);
        this.rightTreeMotor.setInverted(false);
        this.treeStemMotor.setInverted(false);

        this.ballStatusLeds.setLength(RobotConfig.Magazine.NUMBER_OF_LEDS);
        this.ballStatusLeds.start();
    }

    public boolean leftFull() {
        return this.leftTreeLinebreak.Broken();
    }

    public boolean rightFull() {
        return this.rightTreeLinebreak.Broken();
    }

    public boolean stemFull() {
        return this.treeStemLinebreak.Broken();
    }

    public boolean isEjecting() {
        return ejectingBall;
    }

    public void setAutoOperate(boolean autoOperate) {
        this.autoOperate = autoOperate;
    }

    public void sendBall(boolean runThrough) {
        this.sendBall = true;
        this.runThrough = runThrough;
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
        double horizontalSpeed = runFast ? RobotConfig.Magazine.INTAKE_SPEED : ((leftTreeLinebreak.Broken() || rightTreeLinebreak.Broken()) ? RobotConfig.Magazine.IDLE_SPEED : 0);

        switch (this.reloadStage) {
            case 0:
                this.runRaw(horizontalSpeed, horizontalSpeed, horizontalSpeed);

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

    boolean sendBall = false;
    boolean runThrough = false;
    boolean startedSendTimer = false;
    Timer sendBallTimer = new Timer();

    private void sendBallMagazine() {
        double horizontalSpeed = runThrough ? RobotConfig.Magazine.LAUNCH_SPEED : 0;

        this.runRaw(horizontalSpeed, horizontalSpeed, RobotConfig.Magazine.LAUNCH_SPEED);

        if (!this.treeStemLinebreak.Broken() && !this.startedSendTimer) {
            this.sendBallTimer.start();
            this.startedSendTimer = true;
        }
        
        if (this.sendBallTimer.advanceIfElapsed(RobotConfig.Magazine.SECONDS_TO_SEND_BALL)) {
            this.runRaw(0, 0, 0);
            this.sendBall = false;
            this.reloadingVertical = true;
            this.sendBallTimer.stop();
            this.sendBallTimer.reset();
            this.startedSendTimer = false;
            this.runThrough = false;
        }
    }
    public boolean getSendingBall() {
        return this.sendBall;
    }

    boolean runFast = false;

    public void onTick() {
        int numberBalls = 0;
        if (leftTreeLinebreak.Broken()) numberBalls++;
        if (rightTreeLinebreak.Broken()) numberBalls++;
        if (treeStemLinebreak.Broken()) numberBalls++;

        switch (numberBalls) {
            case 0: this.ballStatusLedsBuffer = RobotConfig.Magazine.zeroPattern(this.ballStatusLedsBuffer);
                break;
            case 1: this.ballStatusLedsBuffer = RobotConfig.Magazine.onePattern(this.ballStatusLedsBuffer);
                break;
            case 2: this.ballStatusLedsBuffer = RobotConfig.Magazine.twoPattern(this.ballStatusLedsBuffer);
                break;
            default: this.ballStatusLedsBuffer = RobotConfig.Magazine.overfullPattern(this.ballStatusLedsBuffer);
                break;
        }
        this.ballStatusLeds.setData(ballStatusLedsBuffer);

        if (!this.autoOperate) return;

        //System.out.println(this.autoOperate + "\t\t" + this.ejectingBall + "\t\t" + this.reloadingVertical + "\t\t" + this.reloadStage + "\t\t" + this.sendBalls + "\t\t" + this.runFast);

        if (this.ejectingBall) { 
            this.ejectingBallTimer.start();
            this.ejectBall();
            return;
        }
        if (numberBalls == 3) this.ejectingBall = true;

        if (this.sendBall && !this.reloadingVertical) {
            this.sendBallMagazine();
            return;
        }

        if (this.reloadingVertical) {
            this.reloadVertical();
            return;
        }
        if (!this.treeStemLinebreak.Broken()) {
            this.reloadingVertical = true;
            return;   
        }

        double horizontalSpeed = runFast ? RobotConfig.Magazine.INTAKE_SPEED : 0;
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
        this.ballStatusLeds.stop();
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
