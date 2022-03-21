package frc.robot.subsystems.Vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import frc.robot.RobotConfig;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class ComputerVision extends DiSubsystem implements IInitializable, IDisposable  {
    Thread cvThread;

    HttpCamera targetingCamera;
    CvSink targetingCameraSink;

    public static class TargetingCameraProperties {

    }

    @Override
    public void onInitialize() {
        this.targetingCamera = new HttpCamera("Targeting Camera", RobotConfig.ComputerVision.TARGETING_CAMERA_URL);
        this.targetingCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

        this.targetingCameraSink = new CvSink("Targeting Camera CV");
        this.targetingCameraSink.setSource(this.targetingCamera);
        
        this.cvThread = new Thread(() -> {
            Mat inMat = new Mat();
            Mat hsvMat = new Mat();
            Mat filteredMat = new Mat();

            while (!Thread.interrupted()) {
                long frameTime = this.targetingCameraSink.grabFrame(inMat);
                if (frameTime == 0) continue;

                Imgproc.cvtColor(inMat, hsvMat, Imgproc.COLOR_BGR2HSV);

                Core.inRange(hsvMat, RobotConfig.ComputerVision.MIN_HSV_RANGE, RobotConfig.ComputerVision.MAX_HSV_RANGE, filteredMat);
                
                
            }
        });
        this.cvThread.setDaemon(true);
        this.cvThread.start();
    }

    @Override
    public void onDispose() {
        this.targetingCamera.close();
    }
}
