# camunda-activitystreams
ActivityStreams plugin for Camunda

Related documentation: https://github.com/camunda/camunda-bpm-examples/tree/master/process-engine-plugin/bpmn-parse-listener

Camunda Artifacts: https://app.camunda.com/nexus/content/groups/public/org/camunda/bpm/

Installation:

1. Edit /src/main/resources/activitystreams.properties to match your setup
2. Build using Maven
3. Add jar to Camunda engine classpath
4. Activate in Camunda's main configuration (differs depending on deployment):

camunda.cfg.xml:
```
<property name="processEnginePlugins">
  <list>
    <bean class="de.hofuniversity.iisys.camunda.activitystreams.ActivitystreamsPlugin" />
  </list>
</property>
```

applicationContext.xml:
```
<property name="processEnginePlugins">
  <list>
    ...
    <bean id="activitystreamsPlugin" class="de.hofuniversity.iisys.camunda.activitystreams.ActivitystreamsPlugin" />
  </list>
</property>
```

bpm-platform.xml:
```
<process-engine name="default">
...
  <plugins>
  ...
    <plugin>
      <class>de.hofuniversity.iisys.camunda.activitystreams.ActivitystreamsPlugin</class>
    </plugin>
  ...
  </plugins>
```

Configuration file: /src/main/resources/activitystreams.properties