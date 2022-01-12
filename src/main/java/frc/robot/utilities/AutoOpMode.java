package frc.robot.utilities;

import java.util.ArrayList;
import java.util.List;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public abstract class AutoOpMode extends DiOpMode {
    AutoSequencer sequencer;

    public void install() {
        this.sequencer = new AutoSequencer();
        this.m_container.bindInstance(this.sequencer);

        sequence();
    }

    public abstract void sequence();

    public class AutoSequencer implements ITickable, IDisposable {
        @Inject
        DiOpMode opMode;

        List<AutoTask> tasks = new ArrayList<>();

        private boolean m_hasInitTask = false;
        private boolean m_hasInitLate = false;
        private String m_currentTaskName = "Unknown";

        public void lateInitialize() {
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    tasks.set(i, (AutoTask) opMode.m_container.inject(tasks.get(i)));
                } catch (Exception e) {
                    System.out.println("Failed to initialize task #" + i + " (" + tasks.get(i).getClass().getSimpleName() + ")");
                    e.printStackTrace();
                    return;
                }
            }
        }

        @Override
        public void tick() {
            if (!m_hasInitLate) {
                this.lateInitialize();
                this.m_hasInitLate = true;
            }

            if (this.tasks.size() < 1) {
                System.out.println("Auto Status: Waiting for more tasks...");
                return;
            } else if (!this.m_hasInitTask) {
                this.tasks.get(0).begin();
                this.m_currentTaskName = this.tasks.get(0).getClass().getName();
                this.m_hasInitTask = true;
            }
    
            System.out.println("Auto Status: Running " + m_currentTaskName + "\n ETA: " + tasks.get(0).getETA().formatETA());
    
            if (this.tasks.get(0).execute()) {
                this.tasks.get(0).stop();
                this.tasks.remove(0);
                this.m_hasInitTask = false;
                this.m_currentTaskName = "Unknown";
            }
        }
        
        @Override
        public void dispose() {
            for (AutoTask task : this.tasks) {
                task.stop();
            }
        }

        public void Queue(AutoTask task) {
            this.tasks.add(task);
        }
    }
}
