package frc.robot.utilities.di;

public class DiContext {
    public String Id;
    public Class<?> TargetClass;
    public Object ObjectInstance;
    public String MemberName;
    public Class<?> MemberClass;
    public boolean Optional;
    public DiContainer Container;

    /**
    * Initializes a new Context
    *
    * @param id The current Id we're using
    * @param targetClass The class we're trying to find
    * @param objectInstance The instance of the object we're trying to fill, might be null
    * @param memberName The name of the Field we're trying to fill
    * @param memberClass The class of the Field we're trying to fill
    * @param optional Whether or not we're in an optional context
    * @param container The Container we're targeting
    * @see DiContainer
    */
    protected DiContext(String id, Class<?> targetClass, Object objectInstance, String memberName, Class<?> memberClass, boolean optional, DiContainer container) {
        this.Id = id;
        this.TargetClass = targetClass;
        this.ObjectInstance = objectInstance;
        this.MemberName = memberName;
        this.MemberClass = memberClass;
        this.Optional = optional;
        this.Container = container;
    }
}
