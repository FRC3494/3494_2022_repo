package frc.robot.subsystems.Vision;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CameraServerCvJNI;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class CameraServerSubsystem extends DiSubsystem implements IInitializable, IDisposable  {
    Thread cvThread;

    UsbCamera frontCamera;
    CvSink frontCameraSink;
    int frontSourceHandle;
    CvSource frontCameraSource;
    VideoSource frontCameraServer;

    UsbCamera backCamera;
    CvSink backCameraSink;
    int backSourceHandle;
    CvSource backCameraSource;
    VideoSource backCameraServer;

    @Override
    public void onInitialize() {
        //frontCamera = CameraServer.startAutomaticCapture();
        frontCamera = new UsbCamera("Front Camera", 0);
        frontCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        frontCamera.setVideoMode(VideoMode.PixelFormat.kYUYV, 640, 480, 30);
        frontCamera.setExposureAuto();

        frontCameraSink = CameraServer.getVideo(frontCamera);

        frontSourceHandle = CameraServerCvJNI.createCvSource("Front Camera", VideoMode.PixelFormat.kYUYV.getValue(), 160, 120, 30);

        //frontCameraSource = new CvSource("Front Camera", VideoMode.PixelFormat.kYUYV, 160, 120, 30);

        //frontCameraServer = new VideoSource();
        //frontCameraServer.setSource(frontCameraSource);


        //backCamera = CameraServer.startAutomaticCapture();
        /*backCamera = new UsbCamera("Turret Camera", 1);
        backCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        backCamera.setVideoMode(VideoMode.PixelFormat.kYUYV, 640, 480, 30);
        backCamera.setExposureAuto();

        backCameraSink = CameraServer.getVideo(backCamera);

        backSourceHandle = CameraServerCvJNI.createCvSource("Turret Camera", VideoMode.PixelFormat.kYUYV.getValue(), 160, 120, 30);*/

        cvThread = new Thread(() -> {
            Mat inMat = new Mat();
            Mat outMat = new Mat();

            while (!Thread.interrupted()) {
                if (frontCameraSink.grabFrame(inMat) != 0) {
                    Imgproc.resize(inMat, outMat, new Size(160, 120));
                    CameraServerCvJNI.putSourceFrame(frontSourceHandle, outMat.nativeObj);
                }

                /*if (backCameraSink.grabFrame(inMat) != 0) {
                    Imgproc.resize(inMat, outMat, new Size(160, 120));
                    CameraServerCvJNI.putSourceFrame(backSourceHandle, outMat.nativeObj);
                }*/
            }
        });
        cvThread.setDaemon(true);
        cvThread.start();
    }

    @Override
    public void onDispose() {
        frontCamera.close();
        //backCamera.close();
    }
}
