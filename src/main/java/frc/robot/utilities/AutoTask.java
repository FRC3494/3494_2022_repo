package frc.robot.utilities;

public abstract class AutoTask {
    public abstract void Begin();
    public abstract boolean Execute();
    public abstract void Stop();
    public abstract ETA GetETA();

    public class ETA {
        public double StartTime;
        public double CurrentTime = 0;
        public double EndTime = 0;
        public boolean HasEstimate = false;

        public ETA(double startTime, double currentTime, double endTime) {
            this.StartTime = startTime;
            this.CurrentTime = currentTime;
            this.EndTime = endTime;
            this.HasEstimate = true;
        }
        public ETA() {
            this.CurrentTime = 0;
            this.EndTime = 0;
            this.HasEstimate = false;
        }

        public String FormatETA() {
            if (!this.HasEstimate) return "Unknown";

            double totalTime = this.EndTime - this.StartTime;
            double percent = -((this.EndTime - this.CurrentTime) - totalTime) / totalTime;

            return (this.CurrentTime - this.StartTime) + "/" + totalTime + " (" + percent + "%)";
        }
    }
}
