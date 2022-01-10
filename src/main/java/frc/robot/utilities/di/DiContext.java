package frc.robot.utilities.di;

public class DiContext {
    public String id;
    public Class<?> targetClass;
    public Object objectInstance;
    public String memberName;
    public Class<?> memberClass;
    public boolean optional;
    public DiContainer container;

    protected DiContext(String id, Class<?> targetClass, Object objectInstance, String memberName, Class<?> memberClass, boolean optional, DiContainer container) {
        this.id = id;
        this.targetClass = targetClass;
        this.objectInstance = objectInstance;
        this.memberName = memberName;
        this.memberClass = memberClass;
        this.optional = optional;
        this.container = container;
    }
}
