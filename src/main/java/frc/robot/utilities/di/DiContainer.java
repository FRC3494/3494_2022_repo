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

public class DiContainer {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Inject {
        String id() default "";
        boolean optional() default false;
    }

    public DiContainer parentContainer = null;

    List<DiRule> rules = new ArrayList<>();
    HashMap<UUID, Object> objectPool = new HashMap<>();

    public void Initialize() {
        for (Object objectInstance: objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IInitializable) {
                ((DiInterfaces.IInitializable) objectInstance).Initialize();
            }
        }
    }

    public void Tick() {
        for (Object objectInstance: objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.ITickable) {
                ((DiInterfaces.ITickable) objectInstance).Tick();
            }
        }
    }

    public void Dispose() {
        for (Object objectInstance: objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IDisposable) {
                ((DiInterfaces.IDisposable) objectInstance).Dispose();
            }
        }
    }

    public void setParent(DiContainer parent) {
        parentContainer = parent;
    }

    protected List<Object> ResolveAll(Class<?> searchClass, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> values = new ArrayList<>();
        context.memberClass = searchClass;

        for (DiRule rule: rules) {
            if (rule.ruleApplies(context)) {
                values.add(rule.getObjectValue(context));
            }
        }

        if (parentContainer != null) values.addAll(parentContainer.ResolveAll(searchClass, context));

        return values;
    }

    protected Object Resolve(Class<?> searchClass, DiContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Object> values = ResolveAll(searchClass, context);

        if (values.size() > 1) throw new DiExceptions.MultipleInstancesFoundException();
        if (values.size() < 1) throw new DiExceptions.InstanceNotFoundException();

        return values.get(0);
    }

    protected Object Instantiate(Class<?> inClass, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructors = inClass.getConstructors();
        Class<?>[] parameterClasses = constructors[0].getParameterTypes();
        List<Object> parameterValues = new ArrayList<>();

        context.targetClass = inClass;

        for (Class<?> parameterClass: parameterClasses) {
            parameterValues.add(Resolve(parameterClass, context));
        }

        Object instance = constructors[0].newInstance(parameterValues.toArray(new Object[0]));

        return Inject(instance, context);
    }

    protected Object Inject(Object instance, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] fields = instance.getClass().getFields();

        for (Field field: fields) {
            if (!field.isAnnotationPresent(Inject.class)) continue;
            Inject inject = field.getAnnotation(Inject.class);

            try {
                context.id = inject.id();

                if (context.id.equals("")) context.id = null;
            } catch (NullPointerException e) {
                context.id = null;
            }
            context.optional = inject.optional();
            context.memberName = field.getName();
            context.memberClass = field.getType();

            field.set(instance, Resolve(field.getType(), context));
        }

        return instance;
    }

    public Object Inject(Object instance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, instance.getClass(), instance, "", null, false, this);

        return Inject(instance, context);
    }

    public Object Instantiate(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, inClass, null, "", null, false, this);

        return Instantiate(inClass, context);
    }

    public Object Resolve(Class<?> searchClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        return Resolve(searchClass, context);
    }

    public Object ResolveId(Class<?> searchClass, String id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiContext context = new DiContext(id, searchClass, null, "", null, false, this);

        return Resolve(searchClass, context);
    }

    public List<Object> ResolveAll(Class<?> searchClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        return ResolveAll(searchClass, context);
    }

    public Object TryResolve(Class<?> searchClass) {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        try {
            return Resolve(searchClass, context);
        } catch (Exception e) {
            return null;
        }
    }

    public Object TryResolveId(Class<?> searchClass, String id) {
        DiContext context = new DiContext(id, searchClass, null, "",null, false, this);

        try {
            return Resolve(searchClass, context);
        } catch (Exception e) {
            return null;
        }
    }

    public DiRuleBuilder Bind(Class<?> ...inClasses) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.Bind(inClasses);

        return diRuleBuilder;
    }

    public DiRuleBuilder BindInstance(Object instance) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInstance(instance);

        return diRuleBuilder;
    }

    public DiRuleBuilder BindInstances(Object ...instances) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInstances(instances);

        return diRuleBuilder;
    }

    public DiRuleBuilder BindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInterfacesTo(inClass);

        return diRuleBuilder;
    }

    public DiRuleBuilder BindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.BindInterfacesAndSelfTo(inClass);

        return diRuleBuilder;
    }
}