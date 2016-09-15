package de.hofuniversity.iisys.camunda.activitystreams;

import java.util.Map;

import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.cmmn.transformer.AbstractCmmnTransformListener;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.Task;

public class ActivityStreamsCmmnTransformListener
    extends AbstractCmmnTransformListener
{
    private final ActivitystreamsLogger fLogger;
    
    private final Map<String, String> fConfig;
    
    public ActivityStreamsCmmnTransformListener(ActivitystreamsLogger logger,
        Map<String, String> config)
    {
        fLogger = logger;
        fConfig = config;
    }
    
    @Override
    public void transformHumanTask(PlanItem planItem, HumanTask humanTask,
        CmmnActivity caseActivity)
    {
        fLogger.log("CALL transformHumanTask");

        fLogger.logHumanTask(humanTask);
        
        fLogger.logGenericCmmn(planItem, caseActivity);
        
        fLogger.log("\n\n\n");
    }
    
    @Override
    public void transformProcessTask(PlanItem planItem,
        ProcessTask processTask, CmmnActivity caseActivity)
    {
        fLogger.log("CALL transformProcessTask");

        fLogger.logProcessTask(processTask);
        
        fLogger.logGenericCmmn(planItem, caseActivity);
        
        fLogger.log("\n\n\n");
    }
    
    @Override
    public void transformCaseTask(PlanItem planItem, CaseTask caseTask,
        CmmnActivity caseActivity)
    {
        fLogger.log("CALL transformCaseTask");

        fLogger.logCaseTask(caseTask);
        
        fLogger.logGenericCmmn(planItem, caseActivity);
        
        fLogger.log("\n\n\n");
    }
    
    @Override
    public void transformTask(PlanItem planItem, Task task,
        CmmnActivity caseActivity)
    {
        fLogger.log("CALL transformTask");
        
        fLogger.logCmmnTask(task);
        
        fLogger.logGenericCmmn(planItem, caseActivity);
        
        fLogger.log("\n\n\n");
    }
}
