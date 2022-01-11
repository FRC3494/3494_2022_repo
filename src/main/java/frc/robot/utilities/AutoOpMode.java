package frc.robot.utilities;

import java.util.ArrayList;
import java.util.List;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public abstract class AutoOpMode extends DiOpMode {
    AutoSequencer Sequencer;

    public void Install() {
        Sequencer = new AutoSequencer();
        Container.BindInstance(Sequencer);

        Sequence();
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
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    tasks.set(i, (AutoTask) opMode.Container.Inject(tasks.get(i)));
                } catch (Exception e) {
                    System.out.println("Failed to initialize task #" + i + " (" + tasks.get(i).getClass().getSimpleName() + ")");
                    e.printStackTrace();
                    return;
                }
            }
        }

        @Override
        public void Tick() {
            if (!hasInitlate) {
                LateInitialize();
                hasInitlate = true;
            }

            if (tasks.size() < 1) {
                System.out.println("Auto Status: Waiting for more tasks...");
                return;
            } else if (!hasInitTask) {
                tasks.get(0).Begin();
                currentTaskName = tasks.get(0).getClass().getName();
                hasInitTask = true;
            }
    
            System.out.println("Auto Status: Running " + currentTaskName + "\n ETA: " + tasks.get(0).GetETA().FormatETA());
    
            if (tasks.get(0).Execute()) {
                tasks.get(0).Stop();
                tasks.remove(0);
                hasInitTask = false;
                currentTaskName = "Unknown";
            }
        }
        
        @Override
        public void Dispose() {
            for (AutoTask task : tasks) {
                task.Stop();
            }
        }

        public void Queue(AutoTask task) {
            tasks.add(task);
        }
    }
}
