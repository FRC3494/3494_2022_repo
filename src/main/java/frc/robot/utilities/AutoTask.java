package frc.robot.utilities;

public abstract class AutoTask {
    public abstract void begin();
    public abstract boolean execute();
    public abstract void stop();
    public abstract ETA getETA();

    public class ETA {
        public double startTime;
        public double currentTime = 0;
        public double endTime = 0;
        public boolean hasEstimate = false;

        public ETA(double startTime, double currentTime, double endTime) {
            this.startTime = startTime;
            this.currentTime = currentTime;
            this.endTime = endTime;
            hasEstimate = true;
        }
        public ETA() {
            this.currentTime = 0;
            this.endTime = 0;
            hasEstimate = false;
        }

        public String formatETA() {
            if (!hasEstimate) return "Unknown";

            double totalTime = endTime - startTime;
            double percent = -((endTime - currentTime) - totalTime) / totalTime;

            return (currentTime - startTime) + "/" + totalTime + " (" + percent + "%)";
        }
    }
}
