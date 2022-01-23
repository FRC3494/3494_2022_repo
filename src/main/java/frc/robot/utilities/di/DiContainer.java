package frc.robot.utilities.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import frc.robot.utilities.di.DiInterfaces.IInjected;

public class DiContainer {
    /**
    * An Annotation to mark the Field to be injected by the Container
    *
    * @param Id The optional Id of the instance
    * @param Optional Whetheher or not injecting the field is mandatory or not
    * @see DiContainer
    */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Inject {
        String id() default "";
        boolean optional() default false;
    }

    DiContainer parentContainer = null;

    List<DiRule> rules = new ArrayList<>();
    HashMap<UUID, Object> objectPool = new HashMap<>();

    /**
    * Initializes all objects in the object pool
    * Doesn't check if initialization has already happened, please only call this once
    *
    * @see DiContext
    */
    public void Initialize() {
        for (Object objectInstance: this.objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IInitializable) {
                ((DiInterfaces.IInitializable) objectInstance).Initialize();
            }
        }
    }

    /**
    * Ticks all objects in the object pool
    *
    * @see DiContext
    */
    public void Tick() {
        for (Object objectInstance: this.objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.ITickable) {
                ((DiInterfaces.ITickable) objectInstance).Tick();
            }
        }
    }

    /**
    * Disposes all objects in the object pool
    *
    * @see DiContext
    */
    public void Dispose() {
        for (Object objectInstance: this.objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IDisposable) {
                ((DiInterfaces.IDisposable) objectInstance).Dispose();
            }
        }
    }

    /**
    * Sets this Container to have a parent
    * Containers with parents resolve instances both from themselves and their parent.
    *
    * @param parent The Container to set as a parent
    * @see DiContainer
    */
    public void SetParent(DiContainer parent) {
        this.parentContainer = parent;
    }

    /**
    * Resolves all instances that could be valid for a given Context
    *
    * @param searchClass The class to search for
    * @param context The Context to find instances for
    * @return All objects valid for the Context
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    protected List<Object> ResolveAll(Class<?> searchClass, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> values = new ArrayList<>();
        context.MemberClass = searchClass;

        for (DiRule rule: this.rules) {
            if (rule.ruleApplies(context)) {
                values.add(rule.getObjectValue(context));
            }
        }

        if (this.parentContainer != null) values.addAll(this.parentContainer.ResolveAll(searchClass, context));

        return values;
    }

    /**
    * Gives you a single instance that is valid for a given context
    *
    * @param searchClass The class to search for
    * @param context The Context to find instances for
    * @return A single object valid for the Context
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @throws DiExceptions.MultipleInstancesFoundException Caused if multiple instances are found
    * @throws DiExceptions.InstanceNotFoundException Caused if no instances are found
    * @see DiContainer
    */
    protected Object Resolve(Class<?> searchClass, DiContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Object> values = this.ResolveAll(searchClass, context);

        if (values.size() > 1) throw new DiExceptions.MultipleInstancesFoundException();
        if (values.size() < 1) throw new DiExceptions.InstanceNotFoundException();

        return values.get(0);
    }

    /**
    * Instantiates an instance of a class using a given context and container
    *
    * @param inClass The class to instantiate
    * @param context The Context to use during instantiation
    * @return The instantiated and injected object
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    protected Object Instantiate(Class<?> inClass, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructors = inClass.getConstructors();
        Class<?>[] parameterClasses = constructors[0].getParameterTypes();
        List<Object> parameterValues = new ArrayList<>();

        context.TargetClass = inClass;

        for (Class<?> parameterClass: parameterClasses) {
            parameterValues.add(this.Resolve(parameterClass, context));
        }

        Object instance = constructors[0].newInstance(parameterValues.toArray(new Object[0]));

        return this.Inject(instance, context);
    }

    /**
    * Injects any annotations on a class
    *
    * @param instance The object to inject in to
    * @param context The Context to use during injection
    * @return The injected object
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    protected Object Inject(Object instance, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] fields = instance.getClass().getFields();

        for (Field field: fields) {
            if (!field.isAnnotationPresent(Inject.class)) continue;
            Inject inject = field.getAnnotation(Inject.class);

            try {
                context.Id = inject.id();

                if (context.Id.equals("")) context.Id = null;
            } catch (NullPointerException e) {
                context.Id = null;
            }
            context.Optional = inject.optional();
            context.MemberName = field.getName();
            context.MemberClass = field.getType();

            field.set(instance, this.Resolve(field.getType(), context));
        }

        if (instance instanceof IInjected) ((IInjected) instance).Inject();

        return instance;
    }

    /**
    * Injects any annotations on a class
    *
    * @param instance The object to inject in to
    * @return The injected object
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    public Object Inject(Object instance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, instance.getClass(), instance, "", null, false, this);

        return this.Inject(instance, context);
    }

    /**
    * Instantiates an instance of the class
    *
    * @param inClass The class to instantiate 
    * @return The new object instance
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    public Object Instantiate(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, inClass, null, "", null, false, this);

        return this.Instantiate(inClass, context);
    }

    /**
    * Resolves a single instance of a class
    *
    * @param searchClass The class to search for
    * @return The instance of the class
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    public Object Resolve(Class<?> searchClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        return this.Resolve(searchClass, context);
    }

    /**
    * Resolves a single instance of a class with a specific id
    *
    * @param searchClass The class to search for
    * @param id The id to search for
    * @return The instance of the class
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    public Object ResolveId(Class<?> searchClass, String id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiContext context = new DiContext(id, searchClass, null, "", null, false, this);

        return this.Resolve(searchClass, context);
    }

    /**
    * Resolves all instances of a class
    *
    * @param searchClass The class to search for
    * @return The instance of the class
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiContainer
    */
    public List<Object> ResolveAll(Class<?> searchClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        return this.ResolveAll(searchClass, context);
    }

    /**
    * Tries to resolve a single instance of a class
    *
    * @param searchClass The class to search for
    * @return The instance of the class or null
    * @see DiContainer
    */
    public Object TryResolve(Class<?> searchClass) {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        try {
            return this.Resolve(searchClass, context);
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * Tries to resolve a single instance of a class with a specific id
    *
    * @param searchClass The class to search for
    * @param id The id to search for
    * @return The instance of the class or null
    * @see DiContainer
    */
    public Object TryResolveId(Class<?> searchClass, String id) {
        DiContext context = new DiContext(id, searchClass, null, "", null, false, this);

        try {
            return this.Resolve(searchClass, context);
        } catch (Exception e) {
            return null;
        }
    }

    /**
    * Creates a new set of rules which registers raw classes with the Conteiners
    *
    * @param inClasses The classes to bind
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder Bind(Class<?> ...inClasses) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.Bind(inClasses);

        return diRuleBuilder;
    }

    /**
    * Creates a new rule which registers an instance to be resolved by the Container
    *
    * @param instance The instance to add to the Container
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder BindInstance(Object instance) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInstance(instance);

        return diRuleBuilder;
    }

    /**
    * Creates a new set of rules which registers multiple instances to be resolved by the Container
    *
    * @param instances The instances to add to the Container
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder BindInstances(Object ...instances) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInstances(instances);

        return diRuleBuilder;
    }

    /**
    * Creates a new rule which registers a class' interfaces to resolve to one single class 
    *
    * @param inClass The class to get interfaces from and bind
    * @return A new RuleBuilder 
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiRule
    */
    public DiRuleBuilder BindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInterfacesTo(inClass);

        return diRuleBuilder;
    }

    /**
    * Creates a new rule which registers a class and its interfaces to resolve to one single class 
    *
    * @param inClass The class to get interfaces from and bind
    * @return A new RuleBuilder 
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiRule
    */
    public DiRuleBuilder BindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInterfacesAndSelfTo(inClass);

        return diRuleBuilder;
    }
}