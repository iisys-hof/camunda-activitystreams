package de.hofuniversity.iisys.camunda.activitystreams;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class TypedExecutionListener implements ExecutionListener
{
    private final String fType;
    private final ActivitystreamsLogger fLogger;
    private final boolean fLog;
    
    public TypedExecutionListener(String type, ActivitystreamsLogger logger,
        boolean log)
    {
        fType = type;
        fLogger = logger;
        fLog = log;
    }
    
    @Override
    public void notify(DelegateExecution exec) throws Exception
    {
        if(fLog)
        {
            fLogger.log("CALL notify(DelegateExecution)");
            fLogger.log("TYPE: " + fType);

            fLogger.logExecution(exec);
            
            fLogger.log("\n\n\n");
        }
    }
}
