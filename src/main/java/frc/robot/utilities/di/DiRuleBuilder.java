package frc.robot.utilities.di;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DiRuleBuilder {
    private final DiContainer container;
    private final List<DiRule> targetRules = new ArrayList<>();

    private Boolean bindDone = false;
    private Boolean resolutionSet = false;

    private Class<?> target = null;

    protected DiRuleBuilder(DiContainer containerIn) {
        container = containerIn;
    }

    // User Facing
    public DiRuleBuilder Bind(Class<?> ...inClasses) {
        if (bindDone) throw new DiExceptions.RuleBuilderException();

        for (Class<?> inClass: inClasses) {
            DiRule rule = new DiRule(container, inClass);

            targetRules.add(rule);
            container.rules.add(rule);
        }

        return this;
    }

    public DiRuleBuilder BindInstance(Object instance) {
        BindInstances(instance);

        return this;
    }

    public DiRuleBuilder BindInstances(Object ...instances) {
        if (bindDone || resolutionSet) throw new DiExceptions.RuleBuilderException();

        bindDone = true;
        resolutionSet = true;

        for (Object instance: instances) {
            UUID uuid = UUID.randomUUID();

            container.objectPool.put(uuid, instance);

            DiRule rule = new DiRule(container, instance.getClass());

            rule.setupReturn(uuid);

            targetRules.add(rule);
            container.rules.add(rule);
        }

        return this;
    }

    public DiRuleBuilder BindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Bind(inClass.getInterfaces());

        To(inClass);

        AsSingle();

        return this;
    }

    public DiRuleBuilder BindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Class<?>> classes = Arrays.asList(inClass.getInterfaces());
        classes.add(inClass);

        Bind(classes.toArray(new Class<?>[0]));

        To(inClass);

        AsSingle();

        return this;
    }

    public DiRuleBuilder AsSingle() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (resolutionSet) throw new DiExceptions.RuleBuilderException();

        resolutionSet = true;

        if (target != null) {
            FromInstance(container.Instantiate(target));
        } else {
            for (DiRule rule: targetRules) {
                Object instance = container.Instantiate(rule.targetClass);
                UUID uuid = UUID.randomUUID();

                container.objectPool.put(uuid, instance);

                rule.setupReturn(uuid);
            }
        }

        return this;
    }

    public DiRuleBuilder AsTransient() {
        if (resolutionSet) throw new DiExceptions.RuleBuilderException();

        resolutionSet = true;

        if (target != null) {
            for (DiRule rule: targetRules) {
                rule.setupCreate(target);
            }
        } else {
            for (DiRule rule: targetRules) {
                rule.setupCreate(rule.targetClass);
            }
        }

        return this;
    }

    /*public DiRuleBuilder AsCached() {
        // Create new Cached Instances
        return this;
    }*/

    public DiRuleBuilder To(Class<?> inClass) {
        if (target != null) throw new DiExceptions.RuleBuilderException();

        target = inClass;

        return this;
    }

    public DiRuleBuilder FromInstance(Object instance) {
        if (resolutionSet) throw new DiExceptions.RuleBuilderException();

        resolutionSet = true;

        UUID uuid = UUID.randomUUID();

        container.objectPool.put(uuid, instance);

        for (DiRule rule: targetRules) {
            rule.setupReturn(uuid);
        }

        return this;
    }

    public DiRuleBuilder When(DiCondition condition) {
        for (DiRule rule: targetRules) {
            rule.conditions.add(condition);
        }

        return this;
    }

    public DiRuleBuilder WhenInjectedInto(Class<?> inClass) {
        for (DiRule rule: targetRules) {
            rule.conditions.add(new DiCondition() {
                @Override
                public Boolean check(DiContext context) {
                    return context.targetClass == inClass;
                }
            });
        }

        return this;
    }

    public DiRuleBuilder WithId(String id) {
        for (DiRule rule: targetRules) {
            rule.conditions.add(new DiCondition() {
                @Override
                public Boolean check(DiContext context) {
                    return context.id.equals(id);
                }
            });
        }

        return this;
    }
}
