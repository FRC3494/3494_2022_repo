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

    public void initialize() {
        for (Object objectInstance: objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IInitializable) {
                ((DiInterfaces.IInitializable) objectInstance).initialize();
            }
        }
    }

    public void tick() {
        for (Object objectInstance: objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.ITickable) {
                ((DiInterfaces.ITickable) objectInstance).tick();
            }
        }
    }

    public void dispose() {
        for (Object objectInstance: objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IDisposable) {
                ((DiInterfaces.IDisposable) objectInstance).dispose();
            }
        }
    }

    public void setParent(DiContainer parent) {
        parentContainer = parent;
    }

    protected List<Object> resolveAll(Class<?> searchClass, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> values = new ArrayList<>();
        context.memberClass = searchClass;

        for (DiRule rule: rules) {
            if (rule.ruleApplies(context)) {
                values.add(rule.getObjectValue(context));
            }
        }

        if (parentContainer != null) values.addAll(parentContainer.resolveAll(searchClass, context));

        return values;
    }

    protected Object resolve(Class<?> searchClass, DiContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Object> values = resolveAll(searchClass, context);

        if (values.size() > 1) throw new DiExceptions.MultipleInstancesFoundException();
        if (values.size() < 1) throw new DiExceptions.InstanceNotFoundException();

        return values.get(0);
    }

    protected Object instantiate(Class<?> inClass, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructors = inClass.getConstructors();
        Class<?>[] parameterClasses = constructors[0].getParameterTypes();
        List<Object> parameterValues = new ArrayList<>();

        context.targetClass = inClass;

        for (Class<?> parameterClass: parameterClasses) {
            parameterValues.add(resolve(parameterClass, context));
        }

        Object instance = constructors[0].newInstance(parameterValues.toArray(new Object[0]));

        return inject(instance, context);
    }

    protected Object inject(Object instance, DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
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

            field.set(instance, resolve(field.getType(), context));
        }

        return instance;
    }

    public Object inject(Object instance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, instance.getClass(), instance, "", null, false, this);

        return inject(instance, context);
    }

    public Object instantiate(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, inClass, null, "", null, false, this);

        return instantiate(inClass, context);
    }

    public Object resolve(Class<?> searchClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        return resolve(searchClass, context);
    }

    public Object resolveId(Class<?> searchClass, String id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiContext context = new DiContext(id, searchClass, null, "", null, false, this);

        return resolve(searchClass, context);
    }

    public List<Object> resolveAll(Class<?> searchClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        return resolveAll(searchClass, context);
    }

    public Object tryResolve(Class<?> searchClass) {
        DiContext context = new DiContext(null, searchClass, null, "", null, false, this);

        try {
            return resolve(searchClass, context);
        } catch (Exception e) {
            return null;
        }
    }

    public Object tryResolveId(Class<?> searchClass, String id) {
        DiContext context = new DiContext(id, searchClass, null, "",null, false, this);

        try {
            return resolve(searchClass, context);
        } catch (Exception e) {
            return null;
        }
    }

    public DiRuleBuilder bind(Class<?> ...inClasses) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.bind(inClasses);

        return diRuleBuilder;
    }

    public DiRuleBuilder bindInstance(Object instance) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.bindInstance(instance);

        return diRuleBuilder;
    }

    public DiRuleBuilder bindInstances(Object ...instances) {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.bindInstances(instances);

        return diRuleBuilder;
    }

    public DiRuleBuilder bindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.bindInterfacesTo(inClass);

        return diRuleBuilder;
    }

    public DiRuleBuilder bindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DiRuleBuilder diRuleBuilder = new DiRuleBuilder(this);

        diRuleBuilder.bindInterfacesAndSelfTo(inClass);

        return diRuleBuilder;
    }
}