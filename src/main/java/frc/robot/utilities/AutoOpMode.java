package frc.robot.utilities;

import java.util.ArrayList;
import java.util.List;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public abstract class AutoOpMode extends DiOpMode {
    public AutoSequencer Sequencer;

    public void Install() {
        this.Sequencer = new AutoSequencer();
        this.Container.BindInstance(this.Sequencer);

        this.Sequence();
    }

    public abstract void Sequence();

    public class AutoSequencer implements ITickable, IDisposable {
        @Inject
        DiOpMode opMode;

        List<AutoTask> tasks = new ArrayList<>();

        private boolean hasInitTask = false;
        private boolean hasInitlate = false;
        private String currentTaskName = "Unknown";

        public void LateInitialize() {
            for (int i = 0; i < this.tasks.size(); i++) {
                try {
                    this.tasks.set(i, (AutoTask) this.opMode.Container.Inject(this.tasks.get(i)));
                } catch (Exception e) {
                    System.out.println("Failed to initialize task #" + i + " (" + this.tasks.get(i).getClass().getSimpleName() + ")");
                    e.printStackTrace();
                    return;
                }
            }
        }

        @Override
        public void Tick() {
            if (!this.hasInitlate) {
                this.LateInitialize();
                this.hasInitlate = true;
            }

            if (this.tasks.size() < 1) {
                System.out.println("Auto Status: Waiting for more tasks...");
                return;
            } else if (!this.hasInitTask) {
                this.tasks.get(0).Begin();
                this.currentTaskName = this.tasks.get(0).getClass().getName();
                this.hasInitTask = true;
            }
    
            System.out.println("Auto Status: Running " + this.currentTaskName + "\n ETA: " + this.tasks.get(0).GetETA().FormatETA());
    
            if (this.tasks.get(0).Execute()) {
                this.tasks.get(0).Stop();
                this.tasks.remove(0);
                this.hasInitTask = false;
                this.currentTaskName = "Unknown";
            }
        }
        
        @Override
        public void Dispose() {
            for (AutoTask task : this.tasks) {
                task.Stop();
            }
        }

        public void Queue(AutoTask task) {
            this.tasks.add(task);
        }
    }
}
