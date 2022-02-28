package frc.robot.utilities;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class AutoConfigurable implements ITickable, IInitializable {
    static class DontGrabFrom {
        static List<Field> allFields;
        static HashMap<String, NetworkTableEntry> shuffleBoardElements = new HashMap<String, NetworkTableEntry>();
    }

    public void InitializeShuffleBoard() {
        DontGrabFrom.allFields = getAllVariablesAndInit(null, null, null);
    }

    public void UpdateVariables() {
        for (Field classField : DontGrabFrom.allFields) {
            classField.setAccessible(true);

            if (classField.getType() == double.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(classField.getName()).getDouble((double) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == int.class) {
                try {
                    classField.set(this, (int) DontGrabFrom.shuffleBoardElements.get(classField.getName()).getNumber((Number) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == float.class) {
                try {
                    classField.set(this, (float) DontGrabFrom.shuffleBoardElements.get(classField.getName()).getNumber((Number) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == boolean.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(classField.getName()).getBoolean((boolean) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == String.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(classField.getName()).getString((String) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Field> getAllVariablesAndInit(Class<?> getFieldsFrom , List<Field> fieldsSoFar, String nameTree) {
        if (fieldsSoFar == null) fieldsSoFar = new ArrayList<>();
        if (getFieldsFrom == null) getFieldsFrom = this.getClass();
        if (nameTree == null) nameTree = "";

        for (Field f : getFieldsFrom.getDeclaredFields()) {
            fieldsSoFar.add(f);
            initShuffles(f, getFieldsFrom, nameTree);
        }

        for (Class<?> c : getFieldsFrom.getDeclaredClasses()) {
            if (c.getName() != "DontGrabFrom") {
                getAllVariablesAndInit(c, fieldsSoFar, nameTree + c.getSimpleName() + '/');
            }
        }

        return fieldsSoFar;
    }

    public void initShuffles(Field fieldToInit, Class<?> classFieldIn, String nameTree) {
        fieldToInit.setAccessible(true);

        String fullName = nameTree + fieldToInit.getName();

        try {
            if (fieldToInit.getType() == int.class || fieldToInit.getType() == float.class || fieldToInit.getType() == double.class) {
                DontGrabFrom.shuffleBoardElements.put(fullName,
                        Shuffleboard.getTab("Configuration")
                                .addPersistent(fullName, fieldToInit.get(this))
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(1, 1)
                                .withSize(2, 1)
                                .getEntry());
            } else if (fieldToInit.getType() == boolean.class) {
                DontGrabFrom.shuffleBoardElements.put(fullName,
                        Shuffleboard.getTab("Configuration")
                                .addPersistent(fullName, fieldToInit.get(this))
                                .withWidget(BuiltInWidgets.kToggleButton)
                                .withPosition(1, 1)
                                .withSize(1, 1)
                                .getEntry());
            } else if (fieldToInit.getType() == String.class) {
                DontGrabFrom.shuffleBoardElements.put(fullName,
                        Shuffleboard.getTab("Configuration")
                                .addPersistent(fullName, fieldToInit.get(this))
                                .withWidget(BuiltInWidgets.kToggleButton)
                                .withPosition(1, 1)
                                .withSize(3, 1)
                                .getEntry());
            } else {
                System.out.println("The variable:" + fullName + " is of type (" + fieldToInit.getType() + ") which the current auto config shuffle baord code cannot process, curent avalible types are int, double, float, and boolean.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onInitialize() {
        InitializeShuffleBoard();
    }

    public void onTick() {
        UpdateVariables();
    }
}