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
    public static class DontGrabFrom {
        static List<Field> allFields;
        static HashMap<String, NetworkTableEntry> shuffleBoardElements = new HashMap<String, NetworkTableEntry>();
        public static boolean enableConfiguration = true;
    }

    public void InitializeShuffleBoard() {
        //if (!DontGrabFrom.enableConfiguration) NetworkTableInstance.getDefault().getTable(Shuffleboard.kBaseTableName + "/Configuration").

        /*for (ShuffleboardComponent co : Shuffleboard.getTab("Configuration").getComponents()) {
            co.getEntry();
        }*/

        DontGrabFrom.allFields = getAllVariablesAndInit(null, null, null);
    }

    public String getShuffleboardName(Field classField) {
        String name = classField.getName();
        Class<?> parentClass = classField.getDeclaringClass();

        while (parentClass != this.getClass()) {
            name = parentClass.getSimpleName() + "/" + name;

            parentClass = parentClass.getDeclaringClass();
        }

        return name;
    }

    public void UpdateVariables() {
        if (!DontGrabFrom.enableConfiguration) {
            for (Field classField : DontGrabFrom.allFields) {
                classField.setAccessible(true);
    
                String fullName = getShuffleboardName(classField);

                if (classField.getType() == double.class) {
                    try {
                        DontGrabFrom.shuffleBoardElements.get(fullName).setDouble((double) classField.get(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classField.getType() == int.class) {
                    try {
                        DontGrabFrom.shuffleBoardElements.get(fullName).setNumber((Number) classField.get(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classField.getType() == float.class) {
                    try {
                        DontGrabFrom.shuffleBoardElements.get(fullName).setNumber((Number) classField.get(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classField.getType() == boolean.class) {
                    try {
                        DontGrabFrom.shuffleBoardElements.get(fullName).setBoolean((boolean) classField.get(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (classField.getType() == String.class) {
                    try {
                        DontGrabFrom.shuffleBoardElements.get(fullName).setString((String) classField.get(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return;
        }

        for (Field classField : DontGrabFrom.allFields) {
            classField.setAccessible(true);

            String fullName = getShuffleboardName(classField);

            if (classField.getType() == double.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(fullName).getDouble((double) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == int.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(fullName).getNumber((Number) classField.get(this)).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == float.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(fullName).getNumber((Number) classField.get(this)).floatValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == boolean.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(fullName).getBoolean((boolean) classField.get(this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (classField.getType() == String.class) {
                try {
                    classField.set(this, DontGrabFrom.shuffleBoardElements.get(fullName).getString((String) classField.get(this)));
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
            initShuffles(f, nameTree);
        }

        for (Class<?> c : getFieldsFrom.getDeclaredClasses()) {
            if (c.getName() != "DontGrabFrom") {
                getAllVariablesAndInit(c, fieldsSoFar, nameTree + c.getSimpleName() + '/');
            }
        }

        return fieldsSoFar;
    }

    public void initShuffles(Field fieldToInit, String nameTree) {
        fieldToInit.setAccessible(true);

        String fullName = nameTree + fieldToInit.getName();

        try {
            if (fieldToInit.getType() == int.class || fieldToInit.getType() == float.class || fieldToInit.getType() == double.class) {
                DontGrabFrom.shuffleBoardElements.put(fullName,
                        Shuffleboard.getTab("Configuration")
                                .addPersistent(fullName, fieldToInit.get(this))
                                .withWidget(BuiltInWidgets.kTextView)
                                .withSize(1, 1)
                                .getEntry());
            } else if (fieldToInit.getType() == boolean.class) {
                DontGrabFrom.shuffleBoardElements.put(fullName,
                        Shuffleboard.getTab("Configuration")
                                .addPersistent(fullName, fieldToInit.get(this))
                                .withWidget(BuiltInWidgets.kToggleButton)
                                .withSize(1, 1)
                                .getEntry());
            } else if (fieldToInit.getType() == String.class) {
                DontGrabFrom.shuffleBoardElements.put(fullName,
                        Shuffleboard.getTab("Configuration")
                                .addPersistent(fullName, fieldToInit.get(this))
                                .withWidget(BuiltInWidgets.kTextView)
                                .withSize(2, 1)
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