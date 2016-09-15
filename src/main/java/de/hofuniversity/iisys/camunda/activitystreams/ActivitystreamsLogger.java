package de.hofuniversity.iisys.camunda.activitystreams;

import java.io.File;
import java.io.PrintWriter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.Task;

public class ActivitystreamsLogger
{
    private final PrintWriter fWriter;
    private final boolean fEnabled;
    
    public ActivitystreamsLogger(String logFile, boolean enabled)
        throws Exception
    {
        fEnabled = enabled;
        
        if(fEnabled)
        {
            File file = new File(logFile);
            
            if(!file.exists())
            {
                file.createNewFile();
            }
            
            fWriter = new PrintWriter(file);
        }
        else
        {
            fWriter = null;
        }
    }
    
    public void log(String message)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.println(message);
        fWriter.flush();
    }
    
    public void logGenericCmmn(PlanItem planItem, CmmnActivity caseActivity)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.write("planItem: " + planItem);
        if(planItem != null)
        {
            fWriter.write("planItem.description: "
                + planItem.getDescription());
            fWriter.write("planItem.id: " + planItem.getId());
            fWriter.write("planItem.name: " + planItem.getName());
            fWriter.write("planItem.definition: " + planItem.getDefinition());
        }
        
        fWriter.write("caseActivity: " + caseActivity);
        if(caseActivity != null)
        {
            fWriter.write("caseActivity.id: " + caseActivity.getId());
            fWriter.write("caseActivity.name: " + caseActivity.getName());
            fWriter.write("caseActivity.activities: "
                + caseActivity.getActivities());
            fWriter.write("caseActivity.activityBehavior: "
                + caseActivity.getActivityBehavior());
            fWriter.write("caseActivity.caseDefinition: "
                + caseActivity.getCaseDefinition());
            fWriter.write("caseActivity.cmmnElement: "
                + caseActivity.getCmmnElement());
            fWriter.write("caseActivity.properties: "
                + caseActivity.getProperties());
            
            //TODO: listeners available
        }
        
        fWriter.flush();
    }
    
    public void logHumanTask(HumanTask humanTask)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.write("humanTask: " + humanTask);
        if(humanTask != null)
        {
            fWriter.write("humanTask.class: " + humanTask.getClass());
            fWriter.write("humanTask.camundaAssignee: "
                + humanTask.getCamundaAssignee());
            fWriter.write("humanTask.camundaCandidateGroups: "
                + humanTask.getCamundaCandidateGroups());
            fWriter.write("humanTask.camundaCandidateUsers: "
                + humanTask.getCamundaCandidateUsers());
            fWriter.write("humanTask.camundaDueDate: "
                + humanTask.getCamundaDueDate());
            fWriter.write("humanTask.camundaFromKey: "
                + humanTask.getCamundaFormKey());
            fWriter.write("humanTask.camundaPriority: "
                + humanTask.getCamundaPriority());
            fWriter.write("humanTask.description: "
                + humanTask.getDescription());
            fWriter.write("humanTask.id: "
                + humanTask.getId());
            fWriter.write("humanTask.name: "
                + humanTask.getName());
            fWriter.write("humanTask.rawTextContent: "
                + humanTask.getRawTextContent());
            fWriter.write("humanTask.textContent: "
                + humanTask.getTextContent());
            fWriter.write("humanTask.elementType: "
                + humanTask.getElementType());
            fWriter.write("humanTask.modelInstance: "
                + humanTask.getModelInstance());
        }
        
        fWriter.flush();
    }
    
    public void logProcessTask(ProcessTask processTask)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.write("processTask: " + processTask);
        if(processTask != null)
        {
            fWriter.write("processTask.class: " + processTask.getClass());
            fWriter.write("processTask.camundaProcessBinding: "
                + processTask.getCamundaProcessBinding());
            fWriter.write("processTask.camundaProcessVersion: "
                + processTask.getCamundaProcessVersion());
            fWriter.write("processTask.description: "
                + processTask.getDescription());
            fWriter.write("processTask.id: " + processTask.getId());
            fWriter.write("processTask.name: " + processTask.getName());
            fWriter.write("processTask.process: " + processTask.getProcess());
            fWriter.write("processTask.rawTextContent: "
                + processTask.getRawTextContent());
            fWriter.write("processTask.textContent: "
                + processTask.getTextContent());
            fWriter.write("processTask.elementType: "
                + processTask.getElementType());
            fWriter.write("processTask.modelInstance: "
                + processTask.getModelInstance());
        }
        
        fWriter.flush();
    }
    
    public void logCaseTask(CaseTask caseTask)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.write("caseTask: " + caseTask);
        if(caseTask != null)
        {
            fWriter.write("caseTask.class: " + caseTask.getClass());
            fWriter.write("caseTask.camundaCaseBinding: "
                + caseTask.getCamundaCaseBinding());
            fWriter.write("caseTask.camundaCaseVersion: "
                + caseTask.getCamundaCaseVersion());
            fWriter.write("caseTask.case: " + caseTask.getCase());
            fWriter.write("caseTask.description: "
                + caseTask.getDescription());
            fWriter.write("caseTask.id: " + caseTask.getId());
            fWriter.write("caseTask.name: " + caseTask.getName());
            fWriter.write("caseTask.extensionElements: "
                + caseTask.getExtensionElements());
        }
        
        fWriter.flush();
    }
    
    public void logCmmnTask(Task task)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.write("task: " + task);
        if(task != null)
        {
            fWriter.write("task.class: " + task.getClass());
            fWriter.write("task.description: " + task.getDescription());
            fWriter.write("task.id: " + task.getId());
            fWriter.write("task.name: " + task.getName());
        }
        
        fWriter.flush();
    }
    
    public void logBpmnEvent(Element element, ScopeImpl scope,
        ActivityImpl activity)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.println("serviceTaskElement: " + element);
        fWriter.println("serviceTaskElement.class: " + element.getClass());
        fWriter.println("serviceTaskElement.column: " + element.getColumn());
        fWriter.println("serviceTaskElement.line: " + element.getLine());
        fWriter.println("serviceTaskElement.tagName: " + element.getTagName());
        fWriter.println("serviceTaskElement.text: " + element.getText());
        fWriter.println("serviceTaskElement.uri: " + element.getUri());
        
        
        fWriter.println("scope: " + scope);
        fWriter.println("scope.id: " + scope.getId());
        fWriter.println("scope.name: " + scope.getName());
        fWriter.println("scope.properties: " + scope.getProperties());
        fWriter.println("scope.processDefinition: " + scope.getProcessDefinition());
        
        
        fWriter.println("activity: " + activity);
        fWriter.println("activity.activityId: " + activity.getActivityId());
        fWriter.println("activity.height: " + activity.getHeight());
        fWriter.println("activity.id: " + activity.getId());
        fWriter.println("activity.name: " + activity.getName());
        fWriter.println("activity.width: " + activity.getWidth());
        fWriter.println("activity.x: " + activity.getX());
        fWriter.println("activity.y: " + activity.getY());
        fWriter.println("activity.properties: " + activity.getProperties());
        
        fWriter.println("activity.activityBehavior.class: "
            + activity.getActivityBehavior().getClass());
        
        fWriter.flush();
    }
    
    public void logExecution(DelegateExecution exec)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.println("exection: " + exec);
        
        fWriter.println("activityInstanceId: " + exec.getActivityInstanceId());
        fWriter.println("businessKey: " + exec.getBusinessKey());
        fWriter.println("currentActivityId: " + exec.getCurrentActivityId());
        fWriter.println("currentActivityName: "
            + exec.getCurrentActivityName());
        fWriter.println("currentTransactionId: " + exec.getCurrentTransitionId());
        fWriter.println("eventName: " + exec.getEventName());
        fWriter.println("id: " + exec.getId());
        fWriter.println("parentActivityInstance: "
            + exec.getParentActivityInstanceId());
        fWriter.println("parentId: " + exec.getParentId());
        fWriter.println("procssBusinessKey: " + exec.getProcessBusinessKey());
        fWriter.println("procssDefinitionId: " + exec.getProcessDefinitionId());
        fWriter.println("procssInstanceId: " + exec.getProcessInstanceId());
        fWriter.println("variableScopeKey: " + exec.getVariableScopeKey());
        
        //model element
        FlowElement element = exec.getBpmnModelElementInstance();
        if(element != null)
        {
            fWriter.println("bpmnModelElementInstance: " + element);
            fWriter.println("bpmnModelElementInstance.id: " + element.getId());
            fWriter.println("bpmnModelElementInstance.name: " + element.getName());
            fWriter.println("bpmnModelElementInstance.categoryValueRefs: "
                + element.getCategoryValueRefs());
            fWriter.println("bpmnModelElementInstance.diagramElement: "
                + element.getDiagramElement());
            fWriter.println("bpmnModelElementInstance.documentations: "
                + element.getDocumentations());
            fWriter.println("bpmnModelElementInstance.scope: "
                + element.getScope());
        }
        
        //analyze user tasks
        if(element != null
            && element instanceof UserTaskImpl)
        {
            UserTaskImpl task = (UserTaskImpl) element;
            fWriter.println("userTaskImpl.camundaAssignee: " + task.getCamundaAssignee());
            fWriter.println("userTaskImpl.camundaCandidateGroups: "
                + task.getCamundaCandidateGroups());
            fWriter.println("userTaskImpl.camundaCandidateUsers: "
                + task.getCamundaCandidateUsers());
            fWriter.println("userTaskImpl.camundaDueDate: "
                + task.getCamundaDueDate());
            fWriter.println("userTaskImpl.camundaFormHandlerClass: "
                + task.getCamundaFormHandlerClass());
            fWriter.println("userTaskImpl.camundaFormKey: "
                + task.getCamundaFormKey());
            fWriter.println("userTaskImpl.camundaPriority: "
                + task.getCamundaPriority());
            fWriter.println("userTaskImpl.completionQuantity: "
                + task.getCompletionQuantity());
            fWriter.println("userTaskImpl.id: "
                + task.getId());
            fWriter.println("userTaskImpl.implementation: "
                + task.getImplementation());
            fWriter.println("userTaskImpl.name: "
                + task.getName());
            fWriter.println("userTaskImpl.rawTextContent: "
                + task.getRawTextContent());
            fWriter.println("userTaskImpl.textContent: "
                + task.getTextContent());
        }
        
        //model instance
        BpmnModelInstance model = exec.getBpmnModelInstance();
        if(model != null)
        {
            fWriter.println("bpmnModelInstance: " + model);
            fWriter.println("bpmnModelInstance.definitions: " + model.getDefinitions());
            fWriter.println("bpmnModelInstance.definitions.name: "
                + model.getDefinitions().getName());
        }
        
        fWriter.flush();
    }
    
    public void logBpmnTask(DelegateTask task)
    {
        if(!fEnabled)
        {
            return;
        }
        
        fWriter.println("task: " + task);
        
        fWriter.println("asignee: " + task.getAssignee());
        fWriter.println("caseDefinitionId: " + task.getCaseDefinitionId());
        fWriter.println("caseExecutionId: " + task.getCaseExecutionId());
        fWriter.println("caseInstanceId: " + task.getCaseInstanceId());
        fWriter.println("deleteReason: " + task.getDeleteReason());
        fWriter.println("description: " + task.getDescription());
        fWriter.println("eventName: " + task.getEventName());
        fWriter.println("executionId: " + task.getExecutionId());
        fWriter.println("id: " + task.getId());
        fWriter.println("name: " + task.getName());
        fWriter.println("owner: " + task.getOwner());
        fWriter.println("priority: " + task.getPriority());
        fWriter.println("processDefinitionId: "
            + task.getProcessDefinitionId());
        fWriter.println("processInstanceId: " + task.getProcessInstanceId());
        fWriter.println("taskDefinitionKey: " + task.getTaskDefinitionKey());
        fWriter.println("variableScopeKey: " + task.getVariableScopeKey());
        fWriter.println("bpmnModelElementInstance: "
            + task.getBpmnModelElementInstance());
        fWriter.println("bpmnModelInstance: " + task.getBpmnModelInstance());
        fWriter.println("candidates: " + task.getCandidates());
        fWriter.println("caseExecution: " + task.getCaseExecution());
        fWriter.println("createTime: " + task.getCreateTime());
        fWriter.println("dueDate: " + task.getDueDate());
        fWriter.println("execution: " + task.getExecution());
        fWriter.println("variables: " + task.getVariables());
        
        fWriter.flush();
    }
}
