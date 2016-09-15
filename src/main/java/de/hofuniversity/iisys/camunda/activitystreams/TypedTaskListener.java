package de.hofuniversity.iisys.camunda.activitystreams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import de.hofuniversity.iisys.camunda.activitystreams.util.ShindigUtil;

public class TypedTaskListener implements TaskListener
{
    private static final String ACT_STR_FRAG = "social/rest/activitystreams/";
    private static final String TASK_LIST_FRAG = "app/tasklist/default/#/?task=";
    private static final String COCKPIT_FRAG = "app/cockpit/default/#/process-instance/";
    
    private final String fType;
    private final ActivitystreamsLogger fLogger;
    
    private final Map<String, String> fConfig;
    
    private final String fShindigUrl;
    private final String fCamundaUrl;
    
    private final JSONObject fGenerator;
    
    private final boolean fSendActivities;
    private final boolean fLogTasks, fLogActivities;
    
    private final boolean fTaskCreations, fTaskDeletions, fTaskAssignments,
        fTaskCompletions, fTaskDisassignments, fTaskSelfAssignments,
        fWfCancelByDelete;

    private final ShindigUtil fShindigUtil;
    
    public TypedTaskListener(String type, ActivitystreamsLogger logger,
        Map<String, String> config)
    {
        fType = type;
        fLogger = logger;
        fConfig = config;
        
        //read configuration parameters
        fShindigUrl = fConfig.get("activitystreams.shindig_url");
        fCamundaUrl = fConfig.get("activitystreams.camunda_url");
        
        fSendActivities = Boolean.parseBoolean(fConfig.get(
            "activitystreams.send_activities"));
        fLogTasks = Boolean.parseBoolean(fConfig.get(
            "activitystreams.log_tasks"));
        fLogActivities = Boolean.parseBoolean(fConfig.get(
            "activitystreams.log_activities"));
        
        fTaskCreations = Boolean.parseBoolean(fConfig.get(
            "activitystreams.task_creations"));
        fTaskDeletions = Boolean.parseBoolean(fConfig.get(
            "activitystreams.task_deletions"));
        fTaskAssignments = Boolean.parseBoolean(fConfig.get(
            "activitystreams.task_assignments"));
        fTaskCompletions = Boolean.parseBoolean(fConfig.get(
            "activitystreams.task_completions"));
        fTaskDisassignments = Boolean.parseBoolean(fConfig.get(
            "activitystreams.task_disassignments"));
        fTaskSelfAssignments = Boolean.parseBoolean(fConfig.get(
            "activitystreams.task_self_assignments"));
        fWfCancelByDelete = Boolean.parseBoolean(fConfig.get(
            "activitystreams.workflow_cancel_through_delete"));

        //generate reusable application activity object
        fGenerator = new JSONObject();
        fGenerator.put("id", "camunda");
        fGenerator.put("objectType", "application");
        fGenerator.put("displayName", "Camunda");
        fGenerator.put("url", fCamundaUrl);
        
        fShindigUtil = new ShindigUtil(fShindigUrl);
    }

    @Override
    public void notify(DelegateTask task)
    {
        String eventName = task.getEventName();
        
        //debug logging output
        if(fLogTasks)
        {
            log(task);
        }
        
        //get authenticated user's identity
        ReadOnlyIdentityProvider idProv =
            Context.getCommandContext().getReadOnlyIdentityProvider();
        String authUserId = Context.getCommandContext().getAuthenticatedUserId();
        User authUser = null;
        if(authUserId != null)
        {
            authUser = idProv.findUserById(authUserId);
        }
        
        //TODO: read permissions missing
//        fWriter.write(idProv.findUserById(task.getAssignee()).getFirstName() + "\n");
        
        //generate activity
        JSONObject activity = new JSONObject();
        
        String verb = "Post";
        //only this eventType differs from the verb
        if(eventName.equals("assignment"))
        {
            verb = "assign";
        }
        else
        {
            //others: create, delete, complete
            verb = eventName;
        }
        
        /*
         * task deletions: change to cancel and object to workflow if no tasks
         * are left
         */
        if(fWfCancelByDelete
            && "delete".equals(verb))
        {
            //get workflow and its definition
            String procDefId = task.getProcessDefinitionId();
            String procInstanceId = task.getProcessInstanceId();
            
            ProcessDefinition procDef = task.getProcessEngineServices()
                .getRepositoryService().getProcessDefinition(procDefId);
            
            ProcessInstance procInst = task.getProcessEngineServices()
                .getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(procInstanceId).singleResult();

            List<String> activeActivityIds = task.getProcessEngineServices()
                .getRuntimeService().getActiveActivityIds(procInstanceId);
            
            //check whether task being deleted is included or not
            if(activeActivityIds.size() <= 1)
            {
                //if it's the only one, the whole workflow is cancelled
                verb = "cancel";
                
                //construct object from workflow ID and definition
                JSONObject object = new JSONObject();
                object.put("id", procInst.getId());
                object.put("objectType", "camunda-workflow");
                object.put("displayName", procDef.getName());
                
                String objectUrl = fCamundaUrl + COCKPIT_FRAG + procInst.getId();
                object.put("url", objectUrl);

                activity.put("object", object);
            }
        }
        
        //set verb
        activity.put("verb", verb);
        
        //actor
        //TODO: reliable acting person with id, displayName
        if(authUser != null)
        {
            //authenticated user as actor
            JSONObject actor = new JSONObject();
            actor.put("id", authUserId);
            actor.put("objectType", "person");
            actor.put("displayName",
                authUser.getFirstName() + " " + authUser.getLastName());
          
            activity.put("actor", actor); 
        }
        else if(eventName.equals("complete")
            && task.getAssignee() != null)
        {
            //assigned person as actor for completions
            //TODO: semantically accurate?
            JSONObject actor = new JSONObject();
            actor.put("id", task.getAssignee());
            actor.put("objectType", "person");

            //retrieve name from Shindig
            try
            {
                actor.put("displayName", fShindigUtil.getDisplayName(
                    task.getAssignee()));
            }
            catch(Exception e)
            {
                fLogger.log("ERROR: could not retrieve display name\n"
                    + e.getLocalizedMessage());
            }
            
            activity.put("actor", actor);
        }
        else
        {
            //"System" as actor
            activity.put("actor", fGenerator);
        }
        
        
        //object
        //check first if object has already been set
        if(activity.opt("object") == null)
        {
            JSONObject object = new JSONObject();
            object.put("id", task.getId());
            object.put("objectType", "camunda-task");
            object.put("displayName", task.getName());
            
            String objectUrl = fCamundaUrl + TASK_LIST_FRAG + task.getId();
            object.put("url", objectUrl);
            
            activity.put("object", object);
        }
        
        
        //target
        if(eventName.equals("assignment"))
        {
            JSONObject target = new JSONObject();
            
            if(task.getAssignee() != null)
            {
                //assignee as target
                target.put("id", task.getAssignee());
                target.put("objectType", "person");
                String displayName = null;
                
                if(authUser != null
                    && authUserId.equals(task.getAssignee()))
                {
                    displayName = authUser.getFirstName() + " "
                        + authUser.getLastName();
                }
                else
                {
                    //retrieve name from Shindig
                    try
                    {
                        displayName = fShindigUtil.getDisplayName(
                            task.getAssignee());
                    }
                    catch(Exception e)
                    {
                        fLogger.log("ERROR: could not retrieve display name\n"
                            + e.getLocalizedMessage());
                    }
                }
                
                if(displayName != null)
                {
                    target.put("displayName", displayName);
                }
            }
            else
            {
                //assigned to nobody / removed assignment
                //TODO: better solution
                target.put("id", "nobody");
                target.put("displayName", "Nobody");
                target.put("objectType", "person");
            }
            
            activity.put("target", target);
        }
        
        //add Camunda as the activity generator
        activity.put("generator", fGenerator);
        
        //filter out activities as configured
        boolean send = filter(task, authUserId, verb);
        
        //send activity
        try
        {
            if(fSendActivities
                && send)
            {
                //TODO: more elegant way
                if(authUserId != null)
                {
                    sendActivity(activity, authUserId);
                }
                else if(task.getAssignee() != null)
                {
                    sendActivity(activity, task.getAssignee());
                }
            }
        }
        catch(Exception e)
        {
            fLogger.log(e + "\n\n\n");
        }
        
        //log activity
        if(fLogActivities)
        {
            //mark activity as unsent
            if(!send)
            {
                activity.put("unsent", true);
            }
            
            fLogger.log(activity.toString() + "\n\n\n");
        }
    }
    
    private boolean filter(DelegateTask task, String authUserId, String verb)
    {
        boolean send = true;
        String eventName = task.getEventName();
        
        if(eventName.equals("create"))
        {
            //block creations if there also was an assignment
            if(task.getAssignee() != null)
            {
                send = false;
            }
            //syntactically redundant for clarity
            else if(!fTaskCreations)
            {
                send = false;
            }
        }
        else if(eventName.equals("delete")
            && !fTaskDeletions
            && !"cancel".equals(verb))
        {
            send = false;
        }
        else if(eventName.equals("assignment"))
        {
            if(task.getAssignee() == null)
            {
                if(!fTaskDisassignments)
                {
                    send = false;
                }
            }
            else if(task.getAssignee().equals(authUserId)
                && !fTaskSelfAssignments)
            {
                send = false;
            }
            else if(!fTaskAssignments)
            {
                send = false;
            }
        }
        else if(eventName.equals("complete")
            && !fTaskCompletions)
        {
            send = false;
        }
        
        return send;
    }
    
    /**
     * Sends an activity to the configured Apache Shindig server, in the name
     * the given user.
     * 
     * @param activity activity to send to the server
     * @param user user the activity is for
     * @throws Exception if sending fails
     */
    public void sendActivity(JSONObject activity, String user) throws Exception
    {
        final String json = activity.toString();
        
        //set parameters for sending
        URL shindigUrl = new URL(fShindigUrl + ACT_STR_FRAG + user +
                "/@self");
        final HttpURLConnection connection =
            (HttpURLConnection) shindigUrl.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(
            json.length()));
        
        //send JSON activity
        OutputStreamWriter writer = new OutputStreamWriter(
            connection.getOutputStream(), "UTF-8");
        writer.write(json);
        writer.flush();
        
        //read reply
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
        
        String line = reader.readLine();
        while(line != null)
        {
            line = reader.readLine();
        }
        //TODO: evaluate answer?
        
        reader.close();
    }
    
    private void log(DelegateTask task)
    {
        fLogger.log("CALL notify(DelegateTask)");
        fLogger.log("TYPE: " + fType);

        fLogger.logBpmnTask(task);
        
        fLogger.log("\n\n\n");
    }
}
