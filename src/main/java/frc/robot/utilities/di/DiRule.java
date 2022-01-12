package frc.robot.utilities.di;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiRule {
    protected DiContainer container;
    protected RetrievalMode retrievalMode = RetrievalMode.None;
    protected Class<?> targetClass;
    protected Class<?> instanceClass;
    protected UUID targetObject;
    protected List<DiCondition> conditions = new ArrayList<>();

    protected DiRule(DiContainer containerIn, Class<?> targetClassIn) {
        container = containerIn;
        targetClass = targetClassIn;
    }

    protected void setupCreate(Class<?> instanceClassIn) {
        instanceClass = instanceClassIn;
        retrievalMode = RetrievalMode.Create;
    }

    protected void setupResolve(Class<?> instanceClassIn) {
        if (targetClass == instanceClass) throw new DiExceptions.IncompleteBindingException();

        instanceClass = instanceClassIn;
        retrievalMode = RetrievalMode.Resolve;
    }

    protected void setupReturn(UUID targetObjectIn) {
        targetObject = targetObjectIn;
        retrievalMode = RetrievalMode.Return;
    }

    protected boolean ruleApplies(DiContext context) {
        boolean canAssignContextToTarget = context.memberClass.isAssignableFrom(targetClass);
        boolean canAssignTargetToContext = targetClass.isAssignableFrom(context.memberClass);

        if (!(canAssignContextToTarget || canAssignTargetToContext)) return false;

        if (conditions.size() < 1) return true;

        boolean applies = true;

        for (DiCondition condition: conditions) {
            if (!condition.check(context)) {
                applies = false;
                break;
            }
        }

        return applies;
    }

    protected Object getObjectValue(DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        switch (retrievalMode) {
            case Create:
                if (instanceClass != null) return container.Instantiate(instanceClass, context);
                break;
            case Return:
                if (targetObject != null) {
                    Object instance = container.objectPool.get(targetObject);

                    if (instance != null) return instance;
                }
                break;
            case Resolve:
                if (instanceClass != null) return container.Resolve(instanceClass, context);
            default:
                break;
        }
        throw new DiExceptions.IncompleteBindingException();
    }

    protected enum RetrievalMode {
        None, Resolve, Return, Create
    }
}