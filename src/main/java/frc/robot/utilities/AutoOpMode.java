package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import com.fizzyapple12.javadi.DiContainer.Inject;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.ITickable;
import com.fizzyapple12.wpilibdi.DiCommand;
import com.fizzyapple12.wpilibdi.DiOpMode;

public abstract class AutoOpMode extends DiOpMode {
    @Inject
    public AutoSequencer Sequencer;

    public void install() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Shuffleboard.selectTab("Autonomous");

        Sequencer = (AutoSequencer) this.Container.bindCommand(AutoSequencer.class).schedule().getRawCommand();

        this.sequence();
    }

    public abstract void sequence();

    public static class AutoSequencer extends DiCommand implements ITickable, IDisposable {
        @Inject
        DiOpMode opMode;

        List<AutoTask> tasks = new ArrayList<>();

        private boolean hasInitTask = false;
        private boolean hasInitlate = false;
        private String currentTaskName = "Unknown";

        private static NetworkTableEntry statusEntry = null;
        private static NetworkTableEntry etaEntry = null;

        public void lateInitialize() {
            if (AutoSequencer.statusEntry == null) AutoSequencer.statusEntry = 
                Shuffleboard.getTab("Autonomous")
                .add("Status", "pog")
                .withWidget(BuiltInWidgets.kTextView)
                .withSize(3, 1)
                .getEntry();
            if (AutoSequencer.etaEntry == null) AutoSequencer.etaEntry = 
                Shuffleboard.getTab("Autonomous")
                .add("ETA", "wow")
                .withWidget(BuiltInWidgets.kTextView)
                .withSize(3, 1)
                .getEntry();

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

            if (!this.hasInitlate) {
                this.lateInitialize();
                this.hasInitlate = true;
            }

            if (this.tasks.size() < 1) {
                if (AutoSequencer.statusEntry != null) AutoSequencer.statusEntry.setString("No more tasks to do :)");
                if (AutoSequencer.statusEntry != null) AutoSequencer.etaEntry.setString("Never gonna give you up, never gonna let you down, never gonna run around, or desert you. Never gonna make you cry, never gonna say goodbye, never gonna tell a lie, or hurt you.");
                
                return;
            } else if (!this.hasInitTask) {
                this.tasks.get(0).begin();
                this.currentTaskName = this.tasks.get(0).getClass().getName();
                this.hasInitTask = true;
            }

            if (AutoSequencer.statusEntry != null) AutoSequencer.statusEntry.setString(this.currentTaskName);
            if (AutoSequencer.statusEntry != null) AutoSequencer.etaEntry.setString(this.tasks.get(0).getETA().formatETA());
    
            //System.out.println("Auto Status: Running " + this.currentTaskName + " ETA: " + this.tasks.get(0).getETA().formatETA());
    
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
