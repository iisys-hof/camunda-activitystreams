package de.hofuniversity.iisys.camunda.activitystreams;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;

public class ActivitystreamsPlugin extends AbstractProcessEnginePlugin
{
    private static final String PROPERTIES = "activitystreams";
    
    private final ActivitystreamsBPMNEventListener fEventListener;
    private final ActivityStreamsCmmnTransformListener fTransformListener;
    private final ActivitystreamsLogger fLogger;
    
    private final Map<String, String> fConfig;
    
    public ActivitystreamsPlugin() throws Exception
    {
        fConfig = new HashMap<String, String>();
        readConfig();
        
        boolean logging = Boolean.parseBoolean(
            fConfig.get("activitystreams.logging"));
        fLogger = new ActivitystreamsLogger(
            fConfig.get("activitystreams.log_file"), logging);
        
        fEventListener = new ActivitystreamsBPMNEventListener(
            fLogger, fConfig);
        fTransformListener = new ActivityStreamsCmmnTransformListener(
            fLogger, fConfig);
    }
    
    private void readConfig() throws Exception
    {
        //read configuration from properties file
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES,
            Locale.getDefault(), loader);

        String key = null;
        String value = null;
        Enumeration<String> keys = rb.getKeys();
        while(keys.hasMoreElements())
        {
            key = keys.nextElement();
            value = rb.getString(key);

            fConfig.put(key, value);
        }
    }
    
    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration)
    {
        //BPMN engine listener
        boolean bpmnListener = Boolean.parseBoolean(
            fConfig.get("activitystreams.bpmn_listener"));
        
        if(bpmnListener)
        {
            //get all existing BPMN postParseListeners
            List<BpmnParseListener> postParseListeners =
                processEngineConfiguration.getCustomPostBPMNParseListeners();

            if(postParseListeners == null)
            {
              // if no postParseListener exists, create new list
              postParseListeners = new ArrayList<BpmnParseListener>();
              processEngineConfiguration.setCustomPostBPMNParseListeners(
                  postParseListeners);
            }
            
            //add our own listener
            postParseListeners.add(fEventListener);
        }
        
        
        //CMMN engine listener
        boolean cmmnListener = Boolean.parseBoolean(
            fConfig.get("activitystreams.cmmn_listener"));
        
        if(cmmnListener)
        {
            //get all existing CMMN preTransformListeners
            List<CmmnTransformListener> preTransformListeners =
                processEngineConfiguration.getCustomPreCmmnTransformListeners();
            
            if(preTransformListeners == null)
            {
                // if no preTransformListener exists, create new list
                preTransformListeners = new ArrayList<CmmnTransformListener>();
                processEngineConfiguration.setCustomPreCmmnTransformListeners(
                    preTransformListeners);
            }
            
            //add our own listener
            preTransformListeners.add(fTransformListener);
        }
    }
}
