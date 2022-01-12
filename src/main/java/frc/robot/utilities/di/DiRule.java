package frc.robot.utilities.di;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiRule {
    protected enum RetrievalMode {
        None, Resolve, Return, Create
    }

    protected DiContainer m_container;
    protected RetrievalMode m_retrievalMode = RetrievalMode.None;
    protected Class<?> m_targetClass;
    protected Class<?> m_instanceClass;
    protected UUID m_targetObject;
    protected List<DiCondition> m_conditions = new ArrayList<>();

    protected DiRule(DiContainer containerIn, Class<?> targetClassIn) {
        this.m_container = containerIn;
        this.m_targetClass = targetClassIn;
    }

    protected void setupCreate(Class<?> instanceClassIn) {
        this.m_instanceClass = instanceClassIn;
        this.m_retrievalMode = RetrievalMode.Create;
    }

    protected void setupResolve(Class<?> instanceClassIn) {
        if (this.m_targetClass == this.m_instanceClass) {
            throw new DiExceptions.IncompleteBindingException();
        }

        this.m_instanceClass = instanceClassIn;
        this.m_retrievalMode = RetrievalMode.Resolve;
    }

    protected void setupReturn(UUID targetObjectIn) {
        this.m_targetObject = targetObjectIn;
        this.m_retrievalMode = RetrievalMode.Return;
    }

    protected boolean ruleApplies(DiContext context) {
        boolean canAssignContextToTarget = context.memberClass.isAssignableFrom(this.m_targetClass);
        boolean canAssignTargetToContext = this.m_targetClass.isAssignableFrom(context.memberClass);

        if (!(canAssignContextToTarget || canAssignTargetToContext)) {
            return (false);
        }

        if (this.m_conditions.size() < 1) 
        {
            return (true);
        }

        boolean applies = true;

        for (DiCondition condition : m_conditions) {
            if (!condition.check(context)) {
                applies = false;
                break;
            }
        }

        return (applies);
    }

    protected Object getObjectValue(DiContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        switch (this.m_retrievalMode) {
            case Create:
                if (this.m_instanceClass != null) 
                {
                    return (this.m_container.instantiate(this.m_instanceClass, context));
                }

                break;
            case Return:
                if (m_targetObject != null) {
                    Object instance = this.m_container.objectPool.get(this.m_targetObject);

                    if (instance != null) {
                        return (instance);
                    }
                }
                break;
            case Resolve:
                if (m_instanceClass != null) {
                    return (this.m_container.resolve(m_instanceClass, context));
                }
            case None:
                break;
        }

        throw (new DiExceptions.IncompleteBindingException());
    }
}