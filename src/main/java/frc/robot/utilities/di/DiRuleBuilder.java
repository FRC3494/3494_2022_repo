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
        this.container = containerIn;
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
        if (this.bindDone) throw new DiExceptions.RuleBuilderException();

        for (Class<?> inClass: inClasses) {
            DiRule rule = new DiRule(this.container, inClass);

            this.targetRules.add(rule);
            this.container.rules.add(rule);
        }

        return this;
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
        this.BindInstances(instance);

        return this;
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
        if (this.bindDone || this.resolutionSet) throw new DiExceptions.RuleBuilderException();

        this.bindDone = true;
        this.resolutionSet = true;

        for (Object instance: instances) {
            UUID uuid = UUID.randomUUID();

            this.container.objectPool.put(uuid, instance);

            DiRule rule = new DiRule(this.container, instance.getClass());

            rule.setupReturn(uuid);

            this.targetRules.add(rule);
            this.container.rules.add(rule);
        }

        return this;
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
    public DiRuleBuilder BindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Bind(inClass.getInterfaces());

        this.To(inClass);

        this.AsSingle();

        return this;
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
    public DiRuleBuilder BindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Class<?>> classes = Arrays.asList(inClass.getInterfaces());
        classes.add(inClass);

        this.Bind(classes.toArray(new Class<?>[0]));

        this.To(inClass);

        this.AsSingle();

        return this;
    }

    /**
    * Tells the rules to operate in singleton mode
    *
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder AsSingle() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.resolutionSet) throw new DiExceptions.RuleBuilderException();

        this.resolutionSet = true;

        if (this.target != null) {
            this.FromInstance(this.container.Instantiate(this.target));
        } else {
            for (DiRule rule: this.targetRules) {
                Object instance = this.container.Instantiate(rule.targetClass);
                UUID uuid = UUID.randomUUID();

                this.container.objectPool.put(uuid, instance);

                rule.setupReturn(uuid);
            }
        }

        return this;
    }


    /**
    * Tells the rules to operate in instantiation mode
    *
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder AsTransient() {
        if (this.resolutionSet) throw new DiExceptions.RuleBuilderException();

        this.resolutionSet = true;

        if (this.target != null) {
            for (DiRule rule: this.targetRules) {
                rule.setupCreate(this.target);
            }
        } else {
            for (DiRule rule: this.targetRules) {
                rule.setupCreate(rule.targetClass);
            }
        }

        return this;
    }

    /*public DiRuleBuilder AsCached() {
        // Create new Cached Instances
        return this;
    }*/


    /**
    * Sets the rules to target a new class for resolution
    *
    * @param inClass The class to target
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder To(Class<?> inClass) {
        if (this.target != null) throw new DiExceptions.RuleBuilderException();

        this.target = inClass;

        return this;
    }

    /**
    * Sets the rules to resolve a specific instance
    *
    * @param instance The instance to resolve
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public DiRuleBuilder FromInstance(Object instance) {
        if (this.resolutionSet) throw new DiExceptions.RuleBuilderException();

        this.resolutionSet = true;

        UUID uuid = UUID.randomUUID();

        this.container.objectPool.put(uuid, instance);

        for (DiRule rule: this.targetRules) {
            rule.setupReturn(uuid);
        }

        return this;
    }

    /**
    * Adds a custom resolution condition to the rules 
    *
    * @param condition The condition to add to the rules
    * @return A new RuleBuilder 
    * @see DiRule
    */
    public DiRuleBuilder When(DiCondition condition) {
        for (DiRule rule: this.targetRules) {
            rule.conditions.add(condition);
        }

        return this;
    }

    /**
    * Adds a rule to ensure the rules only resolve when injecting in to a specific class
    *
    * @param inClass The class to limit the Rules to
    * @return A new RuleBuilder 
    * @see DiRule
    */
    public DiRuleBuilder WhenInjectedInto(Class<?> inClass) {
        for (DiRule rule: this.targetRules) {
            rule.conditions.add(new DiCondition() {
                @Override
                public Boolean check(DiContext context) {
                    return context.TargetClass == inClass;
                }
            });
        }

        return this;
    }

    /**
    * Adds a rule to ensure the rules only resolve when injecting in to an annotation with the correct Id
    *
    * @param id The class to limit the Rules to
    * @return A new RuleBuilder 
    * @see DiRule
    */
    public DiRuleBuilder WithId(String id) {
        for (DiRule rule: this.targetRules) {
            rule.conditions.add(new DiCondition() {
                @Override
                public Boolean check(DiContext context) {
                    return context.Id.equals(id);
                }
            });
        }

        return this;
    }
}
