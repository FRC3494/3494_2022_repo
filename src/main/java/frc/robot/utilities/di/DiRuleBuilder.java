package frc.robot.utilities.di;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DiRuleBuilder {
    private final DiContainer M_CONTAINER;
    private final List<DiRule> M_TARGET_RULES = new ArrayList<>();

    private Boolean m_bindDone = false;
    private Boolean m_resolutionSet = false;

    private Class<?> m_target = null;

    protected DiRuleBuilder(DiContainer containerIn) {
        this.M_CONTAINER = containerIn;
    }

    // User Facing
    public DiRuleBuilder bind(Class<?>... inClasses) {
        if (this.m_bindDone) {
            throw (new DiExceptions.RuleBuilderException());
        }

        for (Class<?> inClass : inClasses) {
            DiRule rule = new DiRule(M_CONTAINER, inClass);

            this.M_TARGET_RULES.add(rule);
            this.M_CONTAINER.rules.add(rule);
        }

        return (this);
    }

    public DiRuleBuilder bindInstance(Object instance) {
        this.bindInstances(instance);

        return (this);
    }

    public DiRuleBuilder bindInstances(Object ...instances) {
        if (this.m_bindDone || this.m_resolutionSet) {
            throw (new DiExceptions.RuleBuilderException());
        }

        this.m_bindDone = true;
        this.m_resolutionSet = true;

        for (Object instance : instances) {
            UUID uuid = UUID.randomUUID();

            this.M_CONTAINER.objectPool.put(uuid, instance);

            DiRule rule = new DiRule(M_CONTAINER, instance.getClass());

            rule.setupReturn(uuid);

            this.M_TARGET_RULES.add(rule);
            this.M_CONTAINER.rules.add(rule);
        }

        return (this);
    }

    public DiRuleBuilder bindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.bind(inClass.getInterfaces());

        this.to(inClass);

        this.asSingle();

        return this;
    }

    public DiRuleBuilder bindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Class<?>> classes = Arrays.asList(inClass.getInterfaces());
        classes.add(inClass);

        this.bind(classes.toArray(new Class<?>[0]));

        this.to(inClass);

        this.asSingle();

        return (this);
    }

    public DiRuleBuilder asSingle() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.m_resolutionSet) {
            throw (new DiExceptions.RuleBuilderException());
        }

        this.m_resolutionSet = true;

        if (this.m_target != null) {
            this.fromInstance(M_CONTAINER.instantiate(m_target));
        } else {
            for (DiRule rule : this.M_TARGET_RULES) {
                Object instance = this.M_CONTAINER.instantiate(rule.m_targetClass);
                UUID uuid = UUID.randomUUID();

                this.M_CONTAINER.objectPool.put(uuid, instance);

                rule.setupReturn(uuid);
            }
        }

        return (this);
    }

    public DiRuleBuilder asTransient() {
        if (this.m_resolutionSet) {
            throw (new DiExceptions.RuleBuilderException());
        }

        this.m_resolutionSet = true;

        if (this.m_target != null) {
            for (DiRule rule : this.M_TARGET_RULES) {
                rule.setupCreate(m_target);
            }
        } else {
            for (DiRule rule : this.M_TARGET_RULES) {
                rule.setupCreate(rule.m_targetClass);
            }
        }

        return (this);
    }

    /*public DiRuleBuilder AsCached() {
        // Create new Cached Instances
        return this;
    }*/

    public DiRuleBuilder to(Class<?> inClass) {
        if (this.m_target != null) {
            throw (new DiExceptions.RuleBuilderException());
        }

        this.m_target = inClass;

        return (this);
    }

    public DiRuleBuilder fromInstance(Object instance) {
        if (this.m_resolutionSet) {
            throw (new DiExceptions.RuleBuilderException());
        }

        this.m_resolutionSet = true;

        UUID uuid = UUID.randomUUID();

        this.M_CONTAINER.objectPool.put(uuid, instance);

        for (DiRule rule : this.M_TARGET_RULES) {
            rule.setupReturn(uuid);
        }

        return (this);
    }

    public DiRuleBuilder when(DiCondition condition) {
        for (DiRule rule: this.M_TARGET_RULES) {
            rule.m_conditions.add(condition);
        }

        return (this);
    }

    public DiRuleBuilder whenInjectedInto(Class<?> inClass) {
        for (DiRule rule : this.M_TARGET_RULES) {
            rule.m_conditions.add(new DiCondition() {
                @Override
                public Boolean check(DiContext context) {
                    return context.targetClass == inClass;
                }
            });
        }

        return (this);
    }

    public DiRuleBuilder withId(String id) {
        for (DiRule rule : this.M_TARGET_RULES) {
            rule.m_conditions.add(new DiCondition() {
                @Override
                public Boolean check(DiContext context) {
                    return context.id.equals(id);
                }
            });
        }

        return (this);
    }
}
