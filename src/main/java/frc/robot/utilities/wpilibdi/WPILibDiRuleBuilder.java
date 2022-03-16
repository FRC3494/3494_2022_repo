package frc.robot.utilities.wpilibdi;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.utilities.di.DiContainer;
import frc.robot.utilities.di.DiRule;
import frc.robot.utilities.di.DiRuleBuilder;
import frc.robot.utilities.di.DiExceptions;

public class WPILibDiRuleBuilder extends DiRuleBuilder {
    Command command;
    Subsystem subsystem;

    protected WPILibDiRuleBuilder(DiContainer containerIn) {
        super(containerIn);
    }


    
    public WPILibDiRuleBuilder bindCommand(Class<?> commandClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.bindDone || command != null || subsystem != null || !Command.class.isAssignableFrom(commandClass)) throw new DiExceptions.RuleBuilderException();

        DiRule rule = new DiRule(this.container, commandClass);

        this.targetRules.add(rule);
        this.container.rules.add(rule);

        this.asSingle();

        this.command = (Command) this.container.objectPool.get(rule.targetObject);

        return this;
    }
    
    public WPILibDiRuleBuilder bindCommandInstance(Command commandIn) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.bindDone || this.resolutionSet || command != null || subsystem != null) throw new DiExceptions.RuleBuilderException();

        this.bindDone = true;
        this.resolutionSet = true;
        UUID uuid = UUID.randomUUID();

        this.container.objectPool.put(uuid, commandIn);

        DiRule rule = new DiRule(this.container, commandIn.getClass());

        rule.setupReturn(uuid);

        this.targetRules.add(rule);
        this.container.rules.add(rule);

        this.command = commandIn;

        return this;
    }

    public Command getRawCommand() {
        if (command != null) return command;

        throw new DiExceptions.RuleBuilderException();
    }

    public WPILibDiRuleBuilder schedule() {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        this.getRawCommand().schedule();

        return this;
    }

    public WPILibDiRuleBuilder withTimeout(double seconds) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(command.raceWith(new WaitCommand(seconds)));
    }

    public WPILibDiRuleBuilder withInterrupt(BooleanSupplier condition) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(command.raceWith(new WaitUntilCommand(condition)));
    }
    
    public WPILibDiRuleBuilder beforeStarting(Runnable toRun, Subsystem... requirements) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(command.beforeStarting(new InstantCommand(toRun, requirements)));
    }
    
    public WPILibDiRuleBuilder beforeStarting(Command before) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(new SequentialCommandGroup(before, command));
    }
    
    public WPILibDiRuleBuilder andThen(Runnable toRun, Subsystem... requirements) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(command.andThen(new InstantCommand(toRun, requirements)));
    }
    
    public WPILibDiRuleBuilder andThen(Command... next) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        SequentialCommandGroup group = new SequentialCommandGroup(command);
        group.addCommands(next);
        return ruleBuilder.bindCommandInstance(group);
    }
    
    public WPILibDiRuleBuilder deadlineWith(Command... parallel) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(new ParallelDeadlineGroup(command, parallel));
    }
    
    public WPILibDiRuleBuilder alongWith(Command... parallel) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        ParallelCommandGroup group = new ParallelCommandGroup(command);
        group.addCommands(parallel);
        return ruleBuilder.bindCommandInstance(group);
    }
    
    public WPILibDiRuleBuilder raceWith(Command... parallel) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        ParallelRaceGroup group = new ParallelRaceGroup(command);
        group.addCommands(parallel);
        return ruleBuilder.bindCommandInstance(group);
    }
    
    public WPILibDiRuleBuilder perpetually() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (command == null) throw new DiExceptions.RuleBuilderException();

        WPILibDiRuleBuilder ruleBuilder = new WPILibDiRuleBuilder(this.container);

        return ruleBuilder.bindCommandInstance(new PerpetualCommand(command));
    }


    
    public WPILibDiRuleBuilder bindSubsystem(Class<?> subsystemClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.bindDone || command != null || subsystem != null || !Subsystem.class.isAssignableFrom(subsystemClass)) throw new DiExceptions.RuleBuilderException();

        DiRule rule = new DiRule(this.container, subsystemClass);

        this.targetRules.add(rule);
        this.container.rules.add(rule);

        this.asSingle();

        this.subsystem = (Subsystem) this.container.objectPool.get(rule.targetObject);

        return this;
    }
    
    public WPILibDiRuleBuilder bindSubsystemInstance(Subsystem subsystemIn) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.bindDone || this.resolutionSet || command != null || subsystem != null) throw new DiExceptions.RuleBuilderException();

        this.bindDone = true;
        this.resolutionSet = true;
        UUID uuid = UUID.randomUUID();

        this.container.objectPool.put(uuid, subsystemIn);

        DiRule rule = new DiRule(this.container, subsystemIn.getClass());

        rule.setupReturn(uuid);

        this.targetRules.add(rule);
        this.container.rules.add(rule);

        this.subsystem = subsystemIn;

        return this;
    }

    public Subsystem getRawSubsystem() {
        if (subsystem != null) return subsystem;

        throw new DiExceptions.RuleBuilderException();
    }

    public WPILibDiRuleBuilder setDefaultCommand(Command defaultCommand) {
        if (this.subsystem == null) throw new DiExceptions.RuleBuilderException();

        this.subsystem.setDefaultCommand(defaultCommand);

        return this;
    }
}
