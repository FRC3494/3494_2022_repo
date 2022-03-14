package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import frc.robot.subsystems.AutoNav;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public abstract class AutoOpMode extends DiOpMode {
    @Inject
    public AutoSequencer Sequencer;

    public void install() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        this.Container.bind(AutoSequencer.class).asSingle();

        System.out.println("hiiii");

        while (Sequencer == null) Sequencer = (AutoSequencer) this.Container.tryResolve(AutoSequencer.class);

        System.out.println("pogger");

        this.sequence();
    }

    public abstract void sequence();

    public class AutoSequencer extends DiCommand implements IInitializable, ITickable, IDisposable {
        @Inject
        DiOpMode opMode;

        @Inject
        AutoNav autoNav;

        List<AutoTask> tasks = new ArrayList<>();

        private boolean hasInitTask = false;
        private boolean hasInitlate = false;
        private String currentTaskName = "Unknown";

        public void onInitialize() {
            autoNav.setDefaultCommand(this);
        }

        public void lateInitialize() {
            for (int i = 0; i < this.tasks.size(); i++) {
                try {
                    this.tasks.set(i, (AutoTask) this.opMode.Container.inject(this.tasks.get(i)));
                } catch (Exception e) {
                    System.out.println("Failed to initialize task #" + i + " (" + this.tasks.get(i).getClass().getSimpleName() + ")");
                    e.printStackTrace();
                    return;
                }
            }
        }

        @Override
        public void onTick() {
            System.out.println("auto ticky");

            if (!this.hasInitlate) {
                this.lateInitialize();
                this.hasInitlate = true;
            }

            if (this.tasks.size() < 1) {
                System.out.println("Auto Status: Waiting for more tasks...");
                return;
            } else if (!this.hasInitTask) {
                this.tasks.get(0).begin();
                this.currentTaskName = this.tasks.get(0).getClass().getName();
                this.hasInitTask = true;
            }
    
            System.out.println("Auto Status: Running " + this.currentTaskName + "\n ETA: " + this.tasks.get(0).getETA().formatETA());
    
            if (this.tasks.get(0).execute()) {
                this.tasks.get(0).stop();
                this.tasks.remove(0);
                this.hasInitTask = false;
                this.currentTaskName = "Unknown";
            }
        }
        
        @Override
        public void onDispose() {
            for (AutoTask task : this.tasks) {
                task.stop();
            }
        }

        public void queue(AutoTask task) {
            this.tasks.add(task);
        }
    }
}
