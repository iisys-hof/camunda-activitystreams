package de.hofuniversity.iisys.camunda.activitystreams;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;

public class ActivitystreamsBPMNEventListener
    extends AbstractBpmnParseListener
{
    private final ActivitystreamsLogger fLogger;
    
    private final Map<String, TypedExecutionListener> fExecListeners;
    private final Map<String, TypedTaskListener> fTaskListeners;
    
    private final Map<String, String> fConfig;
    
    private final boolean fLogEvents, fLogExecutions;
    private final boolean fAddExecListeners, fAddTaskListeners;
    
    public ActivitystreamsBPMNEventListener(ActivitystreamsLogger logger,
        Map<String, String> config) throws Exception
    {
        fLogger = logger;
        fConfig = config;
        
        fExecListeners = new HashMap<String, TypedExecutionListener>();
        fTaskListeners = new HashMap<String, TypedTaskListener>();
        
        //read values from configuration
        fLogEvents = Boolean.parseBoolean(fConfig.get(
            "activitystreams.log_bpmn_events"));
        fLogExecutions = Boolean.parseBoolean(
            fConfig.get("activitystreams.log_executions"));;
        
        fAddExecListeners = Boolean.parseBoolean(
            fConfig.get("activitystreams.exec_listener"));
        fAddTaskListeners = Boolean.parseBoolean(
            fConfig.get("activitystreams.task_listener"));
    }
    
    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope,
        ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseServiceTask");
            
            fLogger.logBpmnEvent(serviceTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseServiceTask", activity);
    }
    
    @Override
    public void parseSendTask(Element sendTaskElement, ScopeImpl scope,
        ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseSendTask");
            
            fLogger.logBpmnEvent(sendTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseSendTask", activity);
    }
    
    @Override
    public void parseBoundaryEvent(Element boundaryTaskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseBoundaryEvent");
            
            fLogger.logBpmnEvent(boundaryTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseBoundaryEvent", activity);
    }
    
    @Override
    public void parseBusinessRuleTask(Element businessRuleTaskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseBusinessRuleTask");
            
            fLogger.logBpmnEvent(businessRuleTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseBusinessRuleTask", activity);
    }
    
    @Override
    public void parseCallActivity(Element callActivityElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseCallActivity");
            
            fLogger.logBpmnEvent(callActivityElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseCallActivity", activity);
    }
    
    @Override
    public void parseEndEvent(Element endEventElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseEndEvent");
            
            fLogger.logBpmnEvent(endEventElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseEndEvent", activity);
    }
    
    @Override
    public void parseIntermediateCatchEvent(Element intermediateEventElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseIntermediateCatchEvent");
            
            fLogger.logBpmnEvent(intermediateEventElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseIntermediateCatchEvent", activity);
    }
    
    @Override
    public void parseIntermediateThrowEvent(Element intermediateEventElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseIntermediateThrowEvent");
            
            fLogger.logBpmnEvent(intermediateEventElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseIntermediateThrowEvent", activity);
    }
    
    @Override
    public void parseManualTask(Element manualTaskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseManualTask");
            
            fLogger.logBpmnEvent(manualTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseManualTask", activity);
    }
    
    @Override
    public void parseReceiveTask(Element receiveTaskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseReceiveTask");
            
            fLogger.logBpmnEvent(receiveTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseReceiveTask", activity);
    }
    
    @Override
    public void parseScriptTask(Element scriptTaskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseScriptTask");
            
            fLogger.logBpmnEvent(scriptTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseScriptTask", activity);
    }
    
    @Override
    public void parseStartEvent(Element startEventElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseStartEvent");
            
            fLogger.logBpmnEvent(startEventElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseStartEvent", activity);
    }
    
    @Override
    public void parseTask(Element taskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseTask");
            
            fLogger.logBpmnEvent(taskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseTask", activity);
    }
    
    @Override
    public void parseUserTask(Element userTaskElement,
        ScopeImpl scope, ActivityImpl activity)
    {
        if(fLogEvents)
        {
            fLogger.log("CALL parseUserTask");
            
            fLogger.logBpmnEvent(userTaskElement, scope, activity);
            
            fLogger.log("\n\n\n");
        }
        
        registerAll("parseUserTask", activity);
    }
    
    private void registerAll(String type, ActivityImpl activity)
    {
        //execution listeners
        if(activity != null
            && fAddExecListeners)
        {
            activity.addListener(ExecutionListener.EVENTNAME_START,
                getOrCreateExecListener(
                type + "." + ExecutionListener.EVENTNAME_START));
            activity.addListener(ExecutionListener.EVENTNAME_END,
                getOrCreateExecListener(
                type + "." + ExecutionListener.EVENTNAME_END));
            activity.addListener(ExecutionListener.EVENTNAME_TAKE,
                getOrCreateExecListener(
                type + "." + ExecutionListener.EVENTNAME_TAKE));
        }
        
        //task listeners
        if(activity != null
            && fAddTaskListeners
            && activity.getActivityBehavior() instanceof UserTaskActivityBehavior)
        {
            UserTaskActivityBehavior beh =
                (UserTaskActivityBehavior) activity.getActivityBehavior();
            registerAllTasks(type, beh.getTaskDefinition());
        }
    }
    
    private TypedExecutionListener getOrCreateExecListener(String type)
    {
        TypedExecutionListener listener = fExecListeners.get(type);
        
        if(listener == null)
        {
            listener = new TypedExecutionListener(type, fLogger,
                fLogExecutions);
            fExecListeners.put(type, listener);
        }
        
        return listener;
    }
    
    private void registerAllTasks(String type, TaskDefinition taskDef)
    {
        //only add listeners if they haven't already been added
        
        //assignments
        TaskListener listener = getOrCreateTaskListener(type + "."
            + TaskListener.EVENTNAME_ASSIGNMENT);
        
        if(taskDef.getTaskListeners(TaskListener.EVENTNAME_ASSIGNMENT) == null
            || !taskDef.getTaskListeners(TaskListener.EVENTNAME_ASSIGNMENT)
                .contains(listener))
        {
            taskDef.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT,
                listener);
        }
        
        //completions
        listener = getOrCreateTaskListener(type + "."
            + TaskListener.EVENTNAME_COMPLETE);
        
        if(taskDef.getTaskListeners(TaskListener.EVENTNAME_COMPLETE) == null
            || !taskDef.getTaskListeners(TaskListener.EVENTNAME_COMPLETE)
                .contains(listener))
        {
            taskDef.addTaskListener(TaskListener.EVENTNAME_COMPLETE,
                listener);
        }
        
        //creations
        listener = getOrCreateTaskListener(type + "."
            + TaskListener.EVENTNAME_CREATE);
        
        if(taskDef.getTaskListeners(TaskListener.EVENTNAME_CREATE) == null
            || !taskDef.getTaskListeners(TaskListener.EVENTNAME_CREATE)
                .contains(listener))
        {
            taskDef.addTaskListener(TaskListener.EVENTNAME_CREATE,
                listener);
        }

        //deletions
        listener = getOrCreateTaskListener(type + "."
            + TaskListener.EVENTNAME_DELETE);
        
        if(taskDef.getTaskListeners(TaskListener.EVENTNAME_DELETE) == null
            || !taskDef.getTaskListeners(TaskListener.EVENTNAME_DELETE)
                .contains(listener))
        {
            taskDef.addTaskListener(TaskListener.EVENTNAME_DELETE,
                listener);
        }
    }

    private TypedTaskListener getOrCreateTaskListener(String type)
    {
        TypedTaskListener listener = fTaskListeners.get(type);
        
        if(listener == null)
        {
            listener = new TypedTaskListener(type, fLogger, fConfig);
            fTaskListeners.put(type, listener);
        }
        
        return listener;
    }
}
